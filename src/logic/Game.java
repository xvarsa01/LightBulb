package logic;

import static logic.NodeType.Empty;

import common.AbstractObservable;
import common.Observable;
import logic.interfaces.IGame;
import seeds.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends AbstractObservable implements Observable.Observer, IGame
{

    private final int ColSize;
    private final int RowSize;
    private boolean PowerNodeAlreadyPlaced;
    private boolean BulbNodeAlreadyPlaced;
    private final GameNode[][] Board;
    private Position PowerNodePosition;
    private final List<Position> BulbNodePositions = new ArrayList<>();

    public Game(int rows, int cols){
        if(rows <= 0 || cols <= 0){
            throw new IllegalArgumentException();
        }
        ColSize = cols;
        RowSize = rows;
        GameNode[][] board = new GameNode[rows][cols];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                GameNode node =  new GameNode();
                node.nodeType = Empty;
                node.position = new Position(i, j);
                board[i][j] = node;
            }
        }
        Board = board;
    }

    public void SeedBoard(Difficulty difficulty, int easyGamesPlayed, int mediumGamesPlayed, int hardGamesPlayed){
        Object[][] seed = switch (difficulty) {
            case easy -> SeedEasyBoard(easyGamesPlayed);
            case medium -> SeedMediumBoard(mediumGamesPlayed);
            case hard -> SeedHardBoard(hardGamesPlayed);
        };
        ApplySeed(seed);
    }
    private Object[][] SeedEasyBoard(int lastlyGenerated){
        if (lastlyGenerated >= 1) {
            return EasyGameSeeds.allSeeds[(lastlyGenerated % EasyGameSeeds.allSeeds.length)];
        } else {
            return EasyGameSeeds.allSeeds[0]; // default seed
        }
    }
    private Object[][] SeedMediumBoard(int lastlyGenerated){
        if (lastlyGenerated >= 1) {
            return MediumGameSeeds.allSeeds[lastlyGenerated % MediumGameSeeds.allSeeds.length];
        } else {
            return MediumGameSeeds.allSeeds[0]; // default seed
        }
    }
    private Object[][] SeedHardBoard(int lastlyGenerated){
        if (lastlyGenerated >= 1) {
            return HardGameSeeds.allSeeds[lastlyGenerated % HardGameSeeds.allSeeds.length];
        } else {
            return HardGameSeeds.allSeeds[0]; // default seed
        }
    }

    private void ApplySeed(Object[][] seed){
        for (Object[] n : seed) {
            String type = (String) n[0];
            int row = (Integer) n[1];
            int col = (Integer) n[2];
            Position p = new Position(row, col);
            Side[] sides = new Side[n.length - 3];
            for (int i = 3; i < n.length; i++) {
                sides[i - 3] = (Side) n[i];
            }
            switch (type) {
                case "L" -> createLinkNode(p, sides);
                case "B" -> createBulbNode(p, sides[0]);
                case "P" -> createPowerNode(p, sides);
            }
        }
    }

    public void init(){
        if (!PowerNodeAlreadyPlaced){
            throw new RuntimeException();
        }
        if (!BulbNodeAlreadyPlaced){
            throw new RuntimeException();
        }
        relightBoard();
    }

//    Turns off power for whole board and then turns it on only for nodes connected to power supply
    private void relightBoard(){
        for (GameNode[] gameNodes : Board) {
            for (GameNode node : gameNodes) {
                node.setOnLighted(false);
            }
        }
        GameNode PowerNode = Board[PowerNodePosition.getRow()-1][PowerNodePosition.getCol()-1];
        ServiceNode(PowerNode);

        // will notify UI to rerender
        notifyObservers();
    }
    private void ServiceNode(GameNode NodeToService){

        if (NodeToService.nodeType == Empty){
            return;
        }
        if (NodeToService.light()){
            return;
        }

        NodeToService.setOnLighted(true);
        if (NodeToService.isBulb()){
            return;
        }
        ServiceNeighbourNodes(NodeToService);
    }
    private void ServiceNeighbourNodes(GameNode actualNode){
        Position actual = actualNode.position;
        if (actualNode.north() && actualNode.position.getRow() != 1){
            Position positionNorth = new Position(actual.getRow()-1, actual.getCol());
            GameNode NodeToService = node(positionNorth);
            if (NodeToService.south()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.east() && actualNode.position.getCol() != ColSize){
            Position positionEast = new Position(actual.getRow(), actual.getCol()+1);
            GameNode NodeToService = node(positionEast);
            if (NodeToService.west()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.south() && actualNode.position.getRow() != RowSize){
            Position positionSouth = new Position(actual.getRow()+1, actual.getCol());
            GameNode NodeToService = node(positionSouth);
            if (NodeToService.north()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.west() && actualNode.position.getCol() != 1){
            Position positionWest = new Position(actual.getRow(), actual.getCol()-1);
            GameNode NodeToService = node(positionWest);
            if (NodeToService.east()){
                ServiceNode(NodeToService);
            }
        }
    }

    /**
     * Randomly rotates one node after other, until no Bulb is connected to power
     * @param affectedNodesPercentage probability, that node will be affected
     * @param ms number of ms that thread will sleep after node is rotated
     */
    public void randomlyTurnSomeNodes(float affectedNodesPercentage, int ms, int rotatedNodesAtSameTime){
        int nodesAlreadyRotated = 0;
        for(int i = 1; i <= RowSize; i++){
            for(int j = 1; j <= ColSize; j++){
                Random rand = new Random();
                if (rand.nextFloat() <= affectedNodesPercentage){
                    Position p = new Position(i, j);

                    // rotate node 0-3 times
                    int numberOfTurns = rand.nextInt(4);
                    if (numberOfTurns == 0) break;

                    for (int k = 0; k < numberOfTurns; k++){
                        node(p).turn();
                    }
                    nodesAlreadyRotated++;
                }

                if (nodesAlreadyRotated % rotatedNodesAtSameTime == 0) {
                    sleep(ms);
                }
            }
        }
    }
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks if game has been won - all Bulbs have power
     */
    public boolean GameFinished(){
        boolean finished = true;
        for(Position p : BulbNodePositions){
            GameNode node = node(p);
            if (!node.light()){
                finished = false;
            }
        }
        return finished;
    }

    public int cols(){
        return ColSize;
    }

    public int rows(){
        return RowSize;
    }

    public GameNode node(Position p){
        checkIfPositionIsCorrect(p);

        int row = p.getRow() - 1;
        int col = p.getCol() - 1;
        return Board[row][col];
    }

    public void createBulbNode(Position p, Side side){
        checkIfPositionIsCorrect(p);
        checkIfNodeIsEmpty(p);

        BulbNodeAlreadyPlaced = true;

        GameNode node =  new GameNode();
        node.nodeType = NodeType.Bulb;
        node.position = p;
        node.setSide(side);
        node.addObserver(this);

        Board[p.getRow()-1][p.getCol()-1] = node;
        BulbNodePositions.add(p);
    }

    public void createPowerNode(Position p, Side... sides){
        checkIfPositionIsCorrect(p);
        checkIfNodeIsEmpty(p);
        if (PowerNodeAlreadyPlaced || sides.length == 0){
            throw new RuntimeException("Power node has already been placed");
        }

        PowerNodeAlreadyPlaced = true;
        GameNode node =  new GameNode();
        node.nodeType = NodeType.Power;
        node.position = p;
        node.setMultipleSides(sides);
        node.addObserver(this);

        Board[p.getRow()-1][p.getCol()-1] = node;
        PowerNodePosition = p;
    }

    public void createLinkNode(Position p, Side... sides){
        checkIfPositionIsCorrect(p);
        checkIfNodeIsEmpty(p);
        if (sides.length < 2 || sides.length > 4){
           throw new RuntimeException("Link node must have between 2 and 4 sides");
        }

        GameNode node =  new GameNode();
        node.nodeType = NodeType.Link;
        node.position = p;
        node.setMultipleSides(sides);
        node.addObserver(this);

        Board[p.getRow()-1][p.getCol()-1] = node;
    }

    private void checkIfPositionIsCorrect(Position p){
        if(p.getRow() < 1 || p.getRow() > RowSize || p.getCol() < 1 || p.getCol() > ColSize){
            throw new IndexOutOfBoundsException("Position out of bounds: " + p);
        }
    }

    private void checkIfNodeIsEmpty(Position p) {
        GameNode existing = node(p);
        if (existing.nodeType != Empty) {
            throw new RuntimeException("Bulb already exists at this place");
        }
    }

    @Override
    public void update(Observable observable) {
        relightBoard();
    }
}

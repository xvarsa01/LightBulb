/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Main game logic class managing the game state.
 */

package logic;

import static enums.NodeType.Empty;

import common.AbstractObservable;
import common.Observable;
import enums.NodeType;
import enums.Side;
import logic.interfaces.IGame;

import java.util.ArrayList;
import java.util.List;

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
        GameNode PowerNode = Board[PowerNodePosition.row()-1][PowerNodePosition.col()-1];
        ServiceNode(PowerNode);

        // will notify UI to rerender
        notifyObservers();
    }
    private void ServiceNode(GameNode NodeToService){

        if (NodeToService.nodeType == Empty){
            return;
        }
        if (NodeToService.isLighted()){
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
        if (actualNode.north() && actualNode.position.row() != 1){
            Position positionNorth = new Position(actual.row()-1, actual.col());
            GameNode NodeToService = node(positionNorth);
            if (NodeToService.south()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.east() && actualNode.position.col() != ColSize){
            Position positionEast = new Position(actual.row(), actual.col()+1);
            GameNode NodeToService = node(positionEast);
            if (NodeToService.west()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.south() && actualNode.position.row() != RowSize){
            Position positionSouth = new Position(actual.row()+1, actual.col());
            GameNode NodeToService = node(positionSouth);
            if (NodeToService.north()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.west() && actualNode.position.col() != 1){
            Position positionWest = new Position(actual.row(), actual.col()-1);
            GameNode NodeToService = node(positionWest);
            if (NodeToService.east()){
                ServiceNode(NodeToService);
            }
        }
    }

    /**
     * Checks if game has been won - all Bulbs have power
     */
    public boolean GameFinished(){
        boolean finished = true;
        for(Position p : BulbNodePositions){
            GameNode node = node(p);
            if (!node.isLighted()){
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

    public int bulbNodesCount(){
        return BulbNodePositions.size();
    }

    public GameNode node(Position p){
        checkIfPositionIsCorrect(p);

        int row = p.row() - 1;
        int col = p.col() - 1;
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

        Board[p.row()-1][p.col()-1] = node;
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

        Board[p.row()-1][p.col()-1] = node;
        PowerNodePosition = p;
    }

    public void createLinkNode(Position p, Side... sides){
        checkIfPositionIsCorrect(p);
        checkIfNodeIsEmpty(p);
        if (sides.length < 2 || sides.length > 4){
           throw new RuntimeException("Link node must have between 2 and 4 sides. Actual size is " + sides.length);
        }

        GameNode node =  new GameNode();
        node.nodeType = NodeType.Link;
        node.position = p;
        node.setMultipleSides(sides);
        node.addObserver(this);

        Board[p.row()-1][p.col()-1] = node;
    }

    private void createEmptyNode(Position p){
        checkIfPositionIsCorrect(p);
        GameNode node =  new GameNode();
        node.nodeType = Empty;
        node.position = p;
        Board[p.row()-1][p.col()-1] = node;
    }

    public void clearMap(){
        PowerNodeAlreadyPlaced = false;
        BulbNodeAlreadyPlaced = false;
        BulbNodePositions.clear();

        for (int i = 1; i <= RowSize; i++) {
            for (int j = 1; j <= ColSize; j++) {
                createEmptyNode(new Position(i, j));
            }
        }
    }

    private void checkIfPositionIsCorrect(Position p){
        if(p.row() < 1 || p.row() > RowSize || p.col() < 1 || p.col() > ColSize){
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

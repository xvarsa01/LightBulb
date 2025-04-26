package logic;

import static logic.NodeType.Empty;

public class Game
{

    private final int ColSize;
    private final int RowSize;
    private boolean PowerNodeAlreadyPlaced;
    private boolean BulbNodeAlreadyPlaced;
    private final GameNode[][] Board;
    private Position PowerNodePosition;

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

    public GameNode fieldAt(int x, int y) {
        return Board[x-1][y-1];
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

    private void relightBoard(){
        for (GameNode[] gameNodes : Board) {
            for (GameNode node : gameNodes) {
                node.setOnLighted(false);
            }
        }
        GameNode PowerNode = Board[PowerNodePosition.getRow()-1][PowerNodePosition.getCol()-1];
        ServiceNode(PowerNode);

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
        if (actualNode.north()){
            Position positionNorth = new Position(actual.getRow()-1, actual.getCol());
            GameNode NodeToService = node(positionNorth);
            if (NodeToService.south()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.east()){
            Position positionEast = new Position(actual.getRow(), actual.getCol()+1);
            GameNode NodeToService = node(positionEast);
            if (NodeToService.west()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.south()){
            Position positionSouth = new Position(actual.getRow()+1, actual.getCol());
            GameNode NodeToService = node(positionSouth);
            if (NodeToService.north()){
                ServiceNode(NodeToService);
            }
        }
        if (actualNode.west()){
            Position positionWest = new Position(actual.getRow(), actual.getCol()-1);
            GameNode NodeToService = node(positionWest);
            if (NodeToService.east()){
                ServiceNode(NodeToService);
            }
        }
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
//        node.addObserver(this);

        Board[p.getRow()-1][p.getCol()-1] = node;
    }

    public void createPowerNode(Position p, Side... sides){
        checkIfPositionIsCorrect(p);
        checkIfNodeIsEmpty(p);
        if (PowerNodeAlreadyPlaced || sides.length == 0){
            throw new RuntimeException("Power node has already been placed or it has too many sides (only one side allowed)");
        }

        PowerNodeAlreadyPlaced = true;
        GameNode node =  new GameNode();
        node.nodeType = NodeType.Power;
        node.position = p;
        node.setMultipleSides(sides);
//        node.addObserver(this);

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
//        node.addObserver(this);

        Board[p.getRow()-1][p.getCol()-1] = node;
    }

    private void checkIfPositionIsCorrect(Position p){
        if(p.getRow() < 1 || p.getRow() > RowSize || p.getCol() < 1 || p.getCol() > ColSize){
            throw new IndexOutOfBoundsException("Position out of bounds: " + p);
        }
        return false;
    }

    private void checkIfNodeIsEmpty(Position p) {
        GameNode existing = node(p);
        if (existing.nodeType != Empty) {
            throw new RuntimeException("Bulb already exists at this place");
        }
    }

//    @Override
//    public void update(Observable observable) {
//        if (skippNotification){
//            return;
//        }
//        skippNotification = true;
//        relightBoard();
//    }
}

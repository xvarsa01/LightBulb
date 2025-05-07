package logic;

import enums.Side;

import java.util.*;

public class MapGenerator {

    @SuppressWarnings("FieldCanBeLocal")
    private final float biasConstant = 0.45f;
    public MapGenerator() {

    }


    public void generateMap(Game game, int rows, int cols) {
        Set<Position> inTree = new HashSet<>();
        List<Position[]> edges = new ArrayList<>();
        Random rand = new Random();

        // Start from a random position
        Position start = new Position(rand.nextInt(rows) + 1, rand.nextInt(cols) + 1);
        inTree.add(start);

        // Add all edges from the starting node
        for (Position neighbor : getNeighbors(start, rows, cols)) {
            edges.add(new Position[]{start, neighbor});
        }

        while (!edges.isEmpty()) {
            Position[] edge;

            if (rand.nextFloat() < biasConstant) {
                // Bias: pick the *last* added edge (chain-like growth)
                edge = edges.removeLast();
            } else {
                // Occasionally: pick random edge (creates branches)
                edge = edges.remove(rand.nextInt(edges.size()));
            }

            Position from = edge[0];
            Position to = edge[1];

            if (inTree.contains(to)) continue;

            connectNodes(game, from, to);
            inTree.add(to);

            for (Position neighbor : getNeighbors(to, rows, cols)) {
                if (!inTree.contains(neighbor)) {
                    edges.add(new Position[]{to, neighbor});
                }
            }
        }
        markPowerAndBulbsAndLinks(game, rows, cols);
    }

    private List<Position> getNeighbors(Position pos, int rows, int cols) {
        List<Position> neighbors = new ArrayList<>();
        int r = pos.getRow(), c = pos.getCol();

        if (r > 1) neighbors.add(new Position(r - 1, c));
        if (r < rows) neighbors.add(new Position(r + 1, c));
        if (c > 1) neighbors.add(new Position(r, c - 1));
        if (c < cols) neighbors.add(new Position(r, c + 1));

        return neighbors;
    }

    private void connectNodes(Game game, Position a, Position b) {
        GameNode nodeA = game.node(a);
        GameNode nodeB = game.node(b);

        if (a.getRow() == b.getRow()) {
            if (a.getCol() < b.getCol()) {
                nodeA.setSide(Side.EAST);
                nodeB.setSide(Side.WEST);
            } else {
                nodeA.setSide(Side.WEST);
                nodeB.setSide(Side.EAST);
            }
        } else {
            if (a.getRow() < b.getRow()) {
                nodeA.setSide(Side.SOUTH);
                nodeB.setSide(Side.NORTH);
            } else {
                nodeA.setSide(Side.NORTH);
                nodeB.setSide(Side.SOUTH);
            }
        }
    }
    private void markPowerAndBulbsAndLinks(Game game, int rows, int cols) {
        List<Position> allPositions = new ArrayList<>();

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                allPositions.add(new Position(i, j));
            }
        }

        // 1. Choose one random node as the power node
        Collections.shuffle(allPositions);
        Position powerPos = allPositions.getFirst();
        GameNode powerNode = game.node(powerPos);

        Side[] powerSides = getConnectedSides(powerNode);
        game.createPowerNode(powerPos, powerSides);

        // 2. Mark all other nodes as bulbs or links
        for (Position pos : allPositions) {
            if (pos.equals(powerPos)) continue;

            GameNode node = game.node(pos);
            Side[] sides = getConnectedSides(node);

            if (sides.length == 1) {
                game.createBulbNode(pos, sides[0]); // Only one side, safe to access index 0
            } else {
                game.createLinkNode(pos, sides);
            }
        }
    }

    private Side[] getConnectedSides(GameNode node) {
        List<Side> sides = new ArrayList<>();
        if (node.north()) sides.add(Side.NORTH);
        if (node.east())  sides.add(Side.EAST);
        if (node.south()) sides.add(Side.SOUTH);
        if (node.west())  sides.add(Side.WEST);
        return sides.toArray(new Side[0]);
    }

}

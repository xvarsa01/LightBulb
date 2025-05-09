package logger;

import enums.NodeType;
import logic.Game;
import logic.GameNode;
import logic.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LogLoader {
    public static MoveHistory loadLogFile(Game game, String filePath) throws IOException {
        MoveHistory history = new MoveHistory();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("-")) continue; // skip header

                // parse log line: {L[1@2][NORTH,EAST]}
                if (!line.startsWith("{") || !line.endsWith("}")) continue;
                String content = line.substring(1, line.length() - 1); // strip {}

                String[] parts = content.split("\\[");
                String typeCode = parts[0]; // L, B, P, E
                String posPart = parts[1].split("]")[0]; // 1@2

                String[] posTokens = posPart.split("@");
                int row = Integer.parseInt(posTokens[0]);
                int col = Integer.parseInt(posTokens[1]);
                Position pos = new Position(row, col);

                NodeType nodeType = switch (typeCode) {
                    case "L" -> NodeType.Link;
                    case "B" -> NodeType.Bulb;
                    case "P" -> NodeType.Power;
                    case "E" -> NodeType.Empty;
                    default -> throw new IllegalArgumentException("Unknown node type: " + typeCode);
                };

                // current node from game
                GameNode node = game.node(pos);
                boolean wasLighted = node.isLighted();
                int rot = node.getActualRotation();

                MoveRecord move = new MoveRecord(pos, nodeType, wasLighted, rot);
                history.addMove(move);
            }
        }
        return history;
    }

    public static void applyMove(Game game, MoveRecord move) {
        GameNode node = game.node(move.position);

        // Reset rotation to 0
        while (node.getActualRotation() != 0) {
            node.turn(false);
        }

        // Rotate node to recorded rotation
        for (int i = 0; i < move.previousRotation; i++) {
            node.turn(false);
        }

        // Update lighting
        node.setOnLighted(move.wasLighted);
    }
}

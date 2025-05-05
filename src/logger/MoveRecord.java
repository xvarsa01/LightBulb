package logger;

import logic.GameNode;
import logic.Position;
import enums.NodeType;

public class MoveRecord {
    public final Position position;
    public final NodeType nodeType;
    public final boolean wasLighted;
    public final int previousRotation;

    public MoveRecord(Position position, NodeType nodeType, boolean wasLighted, int previousRotation) {
        this.position = position;
        this.nodeType = nodeType;
        this.wasLighted = wasLighted;
        this.previousRotation = previousRotation;
    }
}

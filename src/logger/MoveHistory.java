package logger;

import java.util.ArrayList;
import java.util.List;

public class MoveHistory {
    private final List<MoveRecord> moves = new ArrayList<>();
    private int pointer = -1;

    public void addMove(MoveRecord move) {
        // Cut off future moves if we go backward
        while (moves.size() > pointer + 1) {
            moves.remove(moves.size() - 1);
        }
        moves.add(move);
        pointer++;
    }

    public MoveRecord undo() {
        if (pointer >= 0) {
            return moves.get(pointer--);
        }
        return null;
    }

    public MoveRecord redo() {
        if (pointer + 1 < moves.size()) {
            return moves.get(++pointer);
        }
        return null;
    }

    public boolean canUndo() { return pointer >= 0; }
    public boolean canRedo() { return pointer + 1 < moves.size(); }
}

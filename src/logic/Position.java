package logic;

public class Position {

    private int row;
    private int col;
    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same reference
        if (obj == null || getClass() != obj.getClass()) return false; // Check type

        Position position = (Position) obj;
        return row == position.row && col == position.col; // Compare values
    }

    @Override
    public int hashCode() {
        return 31 * row + col; // Unique hash based on row and column
    }
}

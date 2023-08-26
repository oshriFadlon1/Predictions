package pointCoord;

public class PointCoord {
    private int row;
    private int col;

    public PointCoord(int x, int y) {
        this.row = x;
        this.col = y;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

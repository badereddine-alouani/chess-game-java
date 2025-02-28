package pieces;

import main.Board;

public class Knight extends Piece {

    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board, col, row, isWhite, "Knight");
    }

    @Override
    public boolean isValidPieceMove(int row, int col) {
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }

    @Override
    public boolean isCollision(int row, int col) {

        return false;
    }

}

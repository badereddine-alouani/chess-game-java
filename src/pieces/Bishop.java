package pieces;

import main.Board;

public class Bishop extends Piece {

    public Bishop(Board board, int col, int row, boolean isWhite) {
        super(board, col, row, isWhite, "Bishop");
    }

    @Override
    public boolean isValidPieceMove(int row, int col) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

}

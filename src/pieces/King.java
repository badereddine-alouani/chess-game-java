package pieces;

import main.Board;

public class King extends Piece {

    public King(Board board, int col, int row, boolean isWhite) {
        super(board, col, row, isWhite, "King");
    }

    @Override
    public boolean isValidPieceMove(int row, int col) {
        return Math.abs(col - this.col) <= 1 && Math.abs(row - this.row) <= 1;
    }

}

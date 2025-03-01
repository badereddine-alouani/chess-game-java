package pieces;

import main.Board;

public class Pawn extends Piece {

    public Pawn(Board board, int col, int row, boolean isWhite) {
        super(board, col, row, isWhite, "Pawn");
    }

    @Override
    public boolean isValidPieceMove(int row, int col) {

        int colorIndex = isWhite ? 1 : -1;

        if (col == this.col && row == this.row - colorIndex && board.getPiece(col, row) == null) {
            return true;
        }
        if (isFirstMove && col == this.col && row == this.row - colorIndex * 2
                && board.getPiece(col, row) == null && board.getPiece(col, row + colorIndex) == null) {
            return true;
        }

        if (Math.abs(col - this.col) == 1 && row == this.row - colorIndex && board.getPiece(col, row) != null) {
            return true;
        }

        // En Passant

        if (Math.abs(col - this.col) == 1 && row == this.row - colorIndex
                && board.getTile(col, row) == board.enPassantTile && board.getPiece(col, row + colorIndex) != null) {
            return true;
        }

        return false;

    }

}

package pieces;

import main.Board;

public class King extends Piece {

    public King(Board board, int col, int row, boolean isWhite) {
        super(board, col, row, isWhite, "King");
    }

    @Override
    public boolean isValidPieceMove(int row, int col) {
        return Math.abs(col - this.col) <= 1 && Math.abs(row - this.row) <= 1 || canCastle(row, col);
    }

    private boolean canCastle(int row, int col) {
        if (this.row == row) {
            if (col == 6) {
                Piece rook = board.getPiece(7, row);
                if (rook != null && rook.isFirstMove && isFirstMove) {
                    return board.getPiece(5, row) == null && board.getPiece(6, row) == null
                            && !board.isKingInCheck(this);
                }
            } else if (col == 2) {
                Piece rook = board.getPiece(0, row);
                if (rook != null && rook.isFirstMove && isFirstMove) {
                    return board.getPiece(1, row) == null && board.getPiece(2, row) == null
                            && board.getPiece(3, row) == null
                            && !board.isKingInCheck(this);
                }
            }
        }

        return false;

    }

}

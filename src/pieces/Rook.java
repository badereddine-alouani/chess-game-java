package pieces;

import main.Board;

public class Rook extends Piece {

    public Rook(Board board, int col, int row, boolean isWhite) {
        super(board, col, row, isWhite, "Rook");
    }

    @Override
    public boolean isValidPieceMove(int row, int col) {
        return this.row == row || this.col == col;
    }

}

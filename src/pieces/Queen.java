package pieces;

import main.Board;

public class Queen extends Piece {

    public Queen(Board board, int col, int row, boolean isWhite) {
        super(board, col, row, isWhite, "Queen");
    }

    @Override
    public boolean isValidPieceMove(int row, int col) {
        return (this.row == row || this.col == col) || (Math.abs(this.col - col) == Math.abs(this.row - row));
    }

}

package main;

import pieces.Piece;

public class Move {
    int oldCol;
    int oldRow;

    int newCol;
    int newRow;

    Piece piece;
    Piece capture;

    public Move(Board board, Piece piece, int newCol, int newRow) {
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;
        this.piece = piece;
        this.capture = board.getPiece(newCol, newRow);
    }

    public static Move toMove(Board board, String moveStr) {
        int oldCol = ((int) moveStr.charAt(0)) - 97;
        int oldRow = 8 - Integer.parseInt("" + moveStr.charAt(1));
        int newCol = ((int) moveStr.charAt(2)) - 97;
        int newRow = 8 - Integer.parseInt("" + moveStr.charAt(3));

        Piece piece = board.getPiece(oldCol, oldRow);

        return new Move(board, piece, newCol, newRow);
    }

    @Override
    public String toString() {
        return String.format("%c%s%c%s", (char) oldCol + 97, 8 - oldRow, (char) newCol + 97, 8 - newRow);
    }

}
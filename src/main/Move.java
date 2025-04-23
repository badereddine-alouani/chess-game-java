package main;

import pieces.Bishop;
import pieces.Knight;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Move {
    int oldCol;
    int oldRow;

    int newCol;
    int newRow;

    Piece piece;
    Piece capture;
    Piece promotionPiece;

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

        Move move = new Move(board, piece, newCol, newRow);

        int len = moveStr.length();
        if (len == 5) {
            switch (moveStr.charAt(len - 1)) {
                case 'n':
                    move.promotionPiece = new Knight(board, move.newCol, move.newRow, move.piece.isWhite);
                    break;
                case 'b':
                    move.promotionPiece = new Bishop(board, move.newCol, move.newRow, move.piece.isWhite);
                    break;
                case 'r':
                    move.promotionPiece = new Rook(board, move.newCol, move.newRow, move.piece.isWhite);
                    break;
                case 'q':
                default:
                    move.promotionPiece = new Queen(board, move.newCol, move.newRow, move.piece.isWhite);
                    break;
            }
        }
        return move;
    }

    @Override
    public String toString() {
        return String.format("%c%s%c%s", (char) oldCol + 97, 8 - oldRow, (char) newCol + 97, 8 - newRow);
    }

}
package main;

import javax.swing.*;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {

    public int tileSize = 80;
    private int cols = 8;
    private int rows = 8;
    public int enPassantTile = -1;
    private boolean isWhiteTurn = true;
    private boolean isGameOver = false;

    public ArrayList<Piece> pieces = new ArrayList<>();

    public Piece selectedPiece;
    Input input = new Input(this);

    public Board() {
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    public Piece getPiece(int col, int row) {
        for (Piece piece : pieces) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }

        return null;
    }

    public void makeMove(Move move) {
        if (move.piece.name.equals("Pawn")) {
            movePawn(move);
        } else if (move.piece.name.equals("King")) {
            moveKing(move);
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;
        move.piece.isFirstMove = false;
        capture(move);
        isWhiteTurn = !isWhiteTurn;
        updateGameState();

    }

    private void moveKing(Move move) {

        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook = null;
            if (move.newCol == 6) {
                rook = getPiece(7, move.piece.row);
                if (rook != null) {
                    rook.col = 5;
                }
            } else if (move.newCol == 2) {
                rook = getPiece(0, move.piece.row);
                if (rook != null) {
                    rook.col = 3;
                }
            }

            rook.xPos = rook.col * tileSize;
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;
    }

    private void movePawn(Move move) {
        int colorIndex = move.piece.isWhite ? 1 : -1;

        // Capture en passant

        if (getTile(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }

        // Get the last en passant tile

        if (Math.abs(move.newRow - move.piece.row) == 2) {
            enPassantTile = getTile(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }
        int promotionRow = move.piece.isWhite ? 0 : 7;
        if (promotionRow == move.newRow) {
            promotePawn(move);
        }
    }

    private void promotePawn(Move move) {
        String[] options = { "Queen", "Rook", "Bishop", "Knight" };
        String option = (String) JOptionPane.showInputDialog(
                null,
                "Choose a piece for promotion:",
                "Pawn Promotion",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                "Queen");

        if (option == null) {
            option = "Queen";
        }

        Piece promotionPiece = null;
        switch (option.toLowerCase()) {
            case "knight":
                promotionPiece = new Knight(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
            case "bishop":
                promotionPiece = new Bishop(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
            case "rook":
                promotionPiece = new Rook(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
            case "queen":
            default:
                promotionPiece = new Queen(this, move.newCol, move.newRow, move.piece.isWhite);
                break;
        }

        pieces.add(promotionPiece);
        pieces.remove(move.piece);
    }

    public int getTile(int col, int row) {
        return row * rows + col;
    }

    public Piece capture(Move move) {
        pieces.remove(move.capture);
        return move.capture;

    }

    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public boolean isvalidMove(Move move) {
        if (move.newRow > 7 || move.newRow < 0 || move.newCol > 7 || move.newCol < 0) {
            return false;
        }

        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidPieceMove(move.newRow, move.newCol)) {
            return false;
        }

        if (move.piece.isCollision(move.newRow, move.newCol)) {
            return false;
        }

        if (doesMovePutKingInCheck(move)) {
            return false;
        }

        if (move.piece.isWhite != isWhiteTurn) {
            return false;
        }
        return true;
    }

    private King findKing(boolean isWhite) {
        King king = null;

        for (Piece piece : pieces) {
            if (piece.name.equals("King") && piece.isWhite == isWhite) {
                king = (King) piece;
                break;
            }
        }

        return king;
    }

    public boolean isKingInCheck(King king) {

        if (king == null)
            return false;

        for (Piece piece : pieces) {
            if (!sameTeam(piece, king)) {
                if (piece.isValidPieceMove(king.row, king.col) && !piece.isCollision(king.row, king.col)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean doesMovePutKingInCheck(Move move) {
        Piece movingPiece = move.piece;
        boolean isWhite = movingPiece.isWhite;

        // Save current position and captured piece
        int oldCol = movingPiece.col;
        int oldRow = movingPiece.row;

        // Simulate the move
        movingPiece.col = move.newCol;
        movingPiece.row = move.newRow;
        Piece capturedPiece = capture(move);

        // Check if the king is in check after the move
        King king = findKing(isWhite);
        boolean kingInCheck = isKingInCheck(king);

        // Undo the move
        movingPiece.col = oldCol;
        movingPiece.row = oldRow;
        if (capturedPiece != null) {
            pieces.add(capturedPiece);
        }

        return kingInCheck;
    }

    private boolean isGameOver(King king) {
        List<Piece> piecesCopy = new ArrayList<>(pieces);
        for (Piece piece : piecesCopy) {
            if (sameTeam(piece, king)) {
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        Move move = new Move(this, piece, col, row);

                        if (isvalidMove(move)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public void addPieces() {

        // Black Pieces
        pieces.add(new Rook(this, 0, 0, false));
        pieces.add(new Knight(this, 1, 0, false));
        pieces.add(new Bishop(this, 2, 0, false));
        pieces.add(new Queen(this, 3, 0, false));
        pieces.add(new King(this, 4, 0, false));
        pieces.add(new Bishop(this, 5, 0, false));
        pieces.add(new Knight(this, 6, 0, false));
        pieces.add(new Rook(this, 7, 0, false));

        pieces.add(new Pawn(this, 0, 1, false));
        pieces.add(new Pawn(this, 1, 1, false));
        pieces.add(new Pawn(this, 2, 1, false));
        pieces.add(new Pawn(this, 3, 1, false));
        pieces.add(new Pawn(this, 4, 1, false));
        pieces.add(new Pawn(this, 5, 1, false));
        pieces.add(new Pawn(this, 6, 1, false));
        pieces.add(new Pawn(this, 7, 1, false));

        // White Pieces
        pieces.add(new Rook(this, 0, 7, true));
        pieces.add(new Knight(this, 1, 7, true));
        pieces.add(new Bishop(this, 2, 7, true));
        pieces.add(new Queen(this, 3, 7, true));
        pieces.add(new King(this, 4, 7, true));
        pieces.add(new Bishop(this, 5, 7, true));
        pieces.add(new Knight(this, 6, 7, true));
        pieces.add(new Rook(this, 7, 7, true));

        pieces.add(new Pawn(this, 0, 6, true));
        pieces.add(new Pawn(this, 1, 6, true));
        pieces.add(new Pawn(this, 2, 6, true));
        pieces.add(new Pawn(this, 3, 6, true));
        pieces.add(new Pawn(this, 4, 6, true));
        pieces.add(new Pawn(this, 5, 6, true));
        pieces.add(new Pawn(this, 6, 6, true));
        pieces.add(new Pawn(this, 7, 6, true));
    }

    private void updateGameState() {
        King king = findKing(isWhiteTurn);
        if (isGameOver(king)) {
            String message;
            if (isKingInCheck(king)) {
                message = (isWhiteTurn ? "White" : "Black") + " Wins!";
            } else {
                message = "Stalemate!";
            }
            JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draws the board
        boolean light = true;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                g.setColor(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                light = !light;
            }
            light = !light;
        }
        if (selectedPiece != null) {
            // Draws valid moves
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isvalidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(50, 150, 255, 150));
                        int circleSize = tileSize / 3;
                        int offset = (tileSize - circleSize) / 2;

                        g2d.fillOval(c * tileSize + offset, r * tileSize + offset, circleSize, circleSize);
                    }
                }
            }
        }

        // Draws pieces
        for (Piece piece : pieces) {
            piece.paint(g2d);
        }
    }
}
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

public class Board extends JPanel {

    public int tileSize = 80;
    private int cols = 8;
    private int rows = 8;
    public int enPassantTile = -1;

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
            capture(move);
        } else {
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * tileSize;
            move.piece.yPos = move.newRow * tileSize;
            move.piece.isFirstMove = false;
            capture(move);
        }

    }

    private void movePawn(Move move) {
        int colorIndex = move.piece.isWhite ? 1 : -1;

        // capture en passant

        if (getTile(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }

        // get the last en passant tile

        if (Math.abs(move.newRow - move.piece.row) == 2) {
            enPassantTile = getTile(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;
        move.piece.isFirstMove = false;
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

    public void capture(Move move) {
        pieces.remove(move.capture);

    }

    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public boolean isvalidMove(Move move) {
        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidPieceMove(move.newRow, move.newCol)) {
            return false;
        }

        if (move.piece.isCollision(move.newRow, move.newCol)) {
            return false;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

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

        for (Piece piece : pieces) {
            piece.paint(g2d);
        }
    }
}
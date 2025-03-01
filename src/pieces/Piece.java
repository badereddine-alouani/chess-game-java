package pieces;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Board;

public abstract class Piece {

    public int col, row, xPos, yPos;
    public boolean isWhite;
    public String name;
    Image sprite;
    Board board;
    public boolean isFirstMove = true;

    private static BufferedImage sheet;

    {
        try {
            sheet = ImageIO.read(getClass().getResourceAsStream("/resources/chess-pieces.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int sheetScale = sheet.getWidth() / 6;

    protected Piece(Board board, int col, int row, boolean isWhite, String name) {
        this.board = board;
        this.col = col;
        this.row = row;
        this.xPos = col * board.tileSize;
        this.yPos = row * board.tileSize;
        this.isWhite = isWhite;
        this.name = name;
        this.sprite = getSprite(name, isWhite, board.tileSize);
    }

    private Image getSprite(String name, boolean isWhite, int tileSize) {
        if (sheet == null)
            return null;

        int x = getPieceColumn(name) * sheetScale;
        int y = (isWhite ? 0 : 1) * sheetScale;

        return sheet.getSubimage(x, y, sheetScale, sheetScale).getScaledInstance(tileSize, tileSize,
                BufferedImage.SCALE_SMOOTH);

    }

    private int getPieceColumn(String name) {
        switch (name.toLowerCase()) {
            case "king":
                return 0;
            case "queen":
                return 1;
            case "bishop":
                return 2;
            case "knight":
                return 3;
            case "rook":
                return 4;
            case "pawn":
                return 5;
            default:
                return 0;
        }
    }

    public abstract boolean isValidPieceMove(int row, int col);

    public boolean isCollision(int row, int col) {

        int rowStep = Integer.compare(row, this.row);
        int colStep = Integer.compare(col, this.col);

        int r = this.row + rowStep;
        int c = this.col + colStep;

        while (r != row || c != col) {
            if (board.getPiece(c, r) != null) {
                return true;
            }

            r += rowStep;
            c += colStep;
        }

        return false;
    }

    public void paint(Graphics2D g2d) {
        g2d.drawImage(sprite, xPos, yPos, null);
    }

}

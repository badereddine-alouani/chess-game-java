package pieces;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Board;

public class Piece {

    int col, row, xPos, yPos;
    boolean isWhite;
    String name;
    Image sprite;
    Board board;

    private static BufferedImage sheet;

    {
        try {
            sheet = ImageIO.read(getClass().getResourceAsStream("/resources/chess-pieces.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int sheetScale = sheet.getWidth() / 6;

    public Piece(Board board, int col, int row, boolean isWhite, String name) {
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

    public void paint(Graphics2D g2d) {
        g2d.drawImage(sprite, xPos, yPos, null);
    }

}

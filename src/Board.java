
import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    private int tileSize = 80;
    private int cols = 8;
    private int rows = 8;

    public Board() {
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean light = true;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                g.setColor(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                light = !light;
            }
            light = !light;
        }
    }
}

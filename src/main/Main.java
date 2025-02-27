package main;

import javax.swing.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Board board = new Board();

        frame.add(board);
        frame.setMinimumSize(new Dimension(850, 850));
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(51, 51, 51));
        frame.setVisible(true);
        frame.setTitle("Java Chess Game");
        frame.setDefaultCloseOperation(3);
    }
}

package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Bot bot = new Bot();

        SwingUtilities.invokeLater(() -> {
            GameModeSelector gameModeSelector = new GameModeSelector(bot, (difficulty) -> {
                try {
                    JFrame frame = new JFrame();
                    Board board = new Board(bot);
                    if (difficulty != null) {
                        System.out.println("Starting game against bot with difficulty: " + difficulty);
                        board.isBot = true;
                    } else {
                        System.out.println("Starting game in player vs player mode");
                        board.isBot = false;
                    }

                    frame.add(board);
                    frame.setMinimumSize(new Dimension(850, 850));
                    frame.setLayout(new GridBagLayout());
                    frame.setLocationRelativeTo(null);
                    frame.getContentPane().setBackground(new Color(51, 51, 51));
                    frame.setVisible(true);
                    frame.setTitle("Java Chess Game");

                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            try {
                                if (board.isBot) {
                                    System.out.println("Stopping chess engine...");
                                    bot.stopEngine();
                                }
                            } catch (IOException ex) {
                                System.err.println("Error stopping chess engine: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    });

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            gameModeSelector.setVisible(true);
        });
    }
}
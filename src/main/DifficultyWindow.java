package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.function.Consumer;

public class DifficultyWindow extends JFrame {
    private static final Color DARK_BG = new Color(40, 40, 40);
    private static final Color LIGHT_TEXT = new Color(230, 230, 230);
    private static final Color BUTTON_COLOR = new Color(0, 153, 153);

    public DifficultyWindow(Bot bot, Consumer<String> onDifficultyChosen) {

        setTitle("Select Difficulty");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setBackground(DARK_BG);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Choose Bot Difficulty");
        titleLabel.setForeground(LIGHT_TEXT);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] difficultyLevels = { "Easy", "Medium", "Hard", "Master" };
        JComboBox<String> difficultySelector = new JComboBox<>(difficultyLevels);
        difficultySelector.setSelectedIndex(1);
        difficultySelector.setMaximumSize(new Dimension(150, 25));
        difficultySelector.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultySelector.setBackground(new Color(60, 60, 60));
        difficultySelector.setForeground(LIGHT_TEXT);
        difficultySelector.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        ((JComponent) difficultySelector.getRenderer()).setOpaque(true);

        JButton startButton = new JButton("Start Game") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BUTTON_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        startButton.setForeground(LIGHT_TEXT);
        startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(150, 35));

        startButton.addActionListener((ActionEvent e) -> {
            String selected = (String) difficultySelector.getSelectedItem();
            try {
                bot.startEngine("stockfish\\stockfish-windows-x86-64.exe");
                bot.setDifficulty(selected.toLowerCase());
                System.out.println("Difficulty set to: " + selected);
                onDifficultyChosen.accept(selected.toLowerCase());
                dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error starting engine or setting difficulty: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(difficultySelector);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(startButton);

        add(mainPanel);
    }
}
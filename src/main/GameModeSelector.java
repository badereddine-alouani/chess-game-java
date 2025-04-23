package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class GameModeSelector extends JFrame {
    private static final Color DARK_BG = new Color(40, 40, 40);
    private static final Color LIGHT_TEXT = new Color(230, 230, 230);
    private static final Color BUTTON_COLOR = new Color(0, 153, 153);
    private static final Color SECONDARY_BUTTON_COLOR = new Color(75, 75, 85);

    public GameModeSelector(Bot bot, Consumer<String> onGameStart) {

        setTitle("Chess Game");
        setSize(300, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Select Game Mode");
        titleLabel.setForeground(LIGHT_TEXT);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton pvpButton = createStyledButton("Player vs Player", BUTTON_COLOR);
        pvpButton.addActionListener((ActionEvent e) -> {
            onGameStart.accept(null);
            dispose();
        });

        JButton botButton = createStyledButton("Player vs Bot", SECONDARY_BUTTON_COLOR);
        botButton.addActionListener((ActionEvent e) -> {
            setVisible(false);

            SwingUtilities.invokeLater(() -> {
                DifficultyWindow difficultyWindow = new DifficultyWindow(bot, (difficulty) -> {
                    onGameStart.accept(difficulty);
                    dispose();
                });
                difficultyWindow.setVisible(true);
            });
        });

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(pvpButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(botButton);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color buttonColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(buttonColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        button.setForeground(LIGHT_TEXT);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));

        return button;
    }
}
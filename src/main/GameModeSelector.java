package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class GameModeSelector extends JFrame {
    private static final Color DARK_BG = new Color(40, 40, 40);
    private static final Color LIGHT_TEXT = new Color(230, 230, 230);
    private static final Color BUTTON_COLOR = new Color(0, 153, 153); // Teal
    private static final Color SECONDARY_BUTTON_COLOR = new Color(75, 75, 85); // Dark bluish-gray

    private Bot bot;
    private Consumer<String> onGameStart;

    public GameModeSelector(Bot bot, Consumer<String> onGameStart) {
        this.bot = bot;
        this.onGameStart = onGameStart;

        // Basic window setup
        setTitle("Chess Game");
        setSize(300, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create a main panel with dark background and padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create title label with custom font and styling
        JLabel titleLabel = new JLabel("Select Game Mode");
        titleLabel.setForeground(LIGHT_TEXT);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create player vs player button
        JButton pvpButton = createStyledButton("Player vs Player", BUTTON_COLOR);
        pvpButton.addActionListener((ActionEvent e) -> {
            // Pass null to indicate PvP mode (no difficulty needed)
            onGameStart.accept(null);
            dispose();
        });

        // Create player vs bot button
        JButton botButton = createStyledButton("Player vs Bot", SECONDARY_BUTTON_COLOR);
        botButton.addActionListener((ActionEvent e) -> {
            // Hide this window and show difficulty selector
            setVisible(false);

            SwingUtilities.invokeLater(() -> {
                DifficultyWindow difficultyWindow = new DifficultyWindow(bot, (difficulty) -> {
                    // The difficulty window will call the onGameStart consumer with chosen
                    // difficulty
                    onGameStart.accept(difficulty);
                    dispose();
                });
                difficultyWindow.setVisible(true);
            });
        });

        // Add components to the panel with spacing
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(pvpButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(botButton);

        // Add the panel to the frame
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

        // Style the button
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
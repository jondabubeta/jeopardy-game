package com.game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class QuestionWindow extends JDialog {

    public QuestionWindow(Frame parent, String title, String content) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Set window size to full screen
        setSize(screenWidth, screenHeight);
        setLocationRelativeTo(parent); // Center the window

        // Load custom font dynamically based on screen size
        Font customFont = loadCustomFont(screenWidth / 30f); // Scale font with screen size

        // Create JTextArea for the question
        JTextArea questionArea = new JTextArea(content);
        questionArea.setFont(customFont);               // Custom font
        questionArea.setForeground(Color.WHITE);        // White text
        questionArea.setBackground(new Color(0x060CE9));// Dark blue background
        questionArea.setLineWrap(true);                 // Enable word wrapping
        questionArea.setWrapStyleWord(true);            // Wrap at word boundaries
        questionArea.setEditable(false);                // Non-editable
        questionArea.setFocusable(false);               // Remove focus outline

        // Dynamically calculate padding based on screen size
        int verticalPadding = screenHeight / 3;   // 10% of screen height
        int horizontalPadding = screenWidth / 15; // 10% of screen width
        questionArea.setMargin(new Insets(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding)); // Set dynamic padding

        // Use GridBagLayout to center the text
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x060CE9)); // Match background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH; // Fill available space
        gbc.anchor = GridBagConstraints.CENTER; // Center horizontally and vertically

        panel.add(questionArea, gbc);

        // Add the panel to the content pane
        getContentPane().add(panel);
    }

    // Load custom font or fallback to SansSerif
    private Font loadCustomFont(float size) {
        String currentDirectory = System.getProperty("user.dir");
        String fontFilePath = currentDirectory + "/src/main/java/com/assets/fonts/itc-korrina.ttf";

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int) size); // Fallback font
        }
    }
}

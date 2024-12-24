package com.game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class Question extends JButton {
    private final Category category;

    public Question(Category category, String buttonText) {
        if (category == null || buttonText == null || buttonText.isEmpty()) {
            throw new IllegalArgumentException("Category and buttonText must not be null or empty.");
        }

        this.category = category;
        setText(buttonText);

        Font customFont = loadCustomFont("/src/main/java/com/assets/fonts/swiss-911.ttf", 80);
        if (customFont != null) {
            setFont(customFont);
        } else {
            setFont(new Font("SansSerif", Font.BOLD, 30));
        }

        setBackground(new Color(0x060CE9));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public Category getCategory() {
        return category;
    }

    public void markAnswered() {
        setBackground(Color.GRAY);
        setEnabled(false);
    }

    public void highlight() {
        setBackground(new Color(0xFFD700));
    }

    public void resetStyle() {
        setBackground(new Color(0x060CE9));
        setEnabled(true);
    }

    private Font loadCustomFont(String fontPath, float size) {
        try {
            String currentDirectory = System.getProperty("user.dir");
            File fontFile = new File(currentDirectory + fontPath);
            if (fontFile.exists()) {
                return Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
            } else {
                System.err.println("Font file not found: " + fontFile.getAbsolutePath());
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

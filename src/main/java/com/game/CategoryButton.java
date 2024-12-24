package com.game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class CategoryButton extends JButton {
	private Category category;

	public CategoryButton(Category category) {
		this.category = category;
		String currentDirectory = System.getProperty("user.dir");
		String fontFilePath = currentDirectory + "/src/main/java/com/assets/fonts/swiss-911.ttf";

		try {
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(50f);
			customFont = customFont.deriveFont(40f);
			setFont(customFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		setText(category.getName());

		setBackground(Color.WHITE);

		setForeground(Color.WHITE);

		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setFocusPainted(false);
		setEnabled(true);

		Color backgroundColor = new Color(0x060CE9);
		setBackground(backgroundColor);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
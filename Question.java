import javax.swing.*;
import java.awt.*;

public class Question extends JButton {
    private Category category;

    public Question(Category category, String buttonText) {
        this.category = category;
        setText(buttonText);

        Color backgroundColor = new Color(0x060CE9);
        setBackground(backgroundColor);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setForeground(Color.WHITE);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

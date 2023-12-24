import javax.swing.*;

public class Question extends JButton {
    private Category category;

    public Question(Category category, String buttonText) {
        this.category = category;
        setText(buttonText);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

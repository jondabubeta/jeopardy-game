import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class QuestionWindow extends JDialog {
    public QuestionWindow(Frame parent, String title, String content) {
        super(parent, title, Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Set the background color of the QuestionWindow
        getContentPane().setBackground(new Color(0x060CE9));

        // Create a JTextArea to display the content
        JTextArea textArea = new JTextArea(content);
        textArea.setBackground(new Color(6, 12, 233));
        textArea.setFont(getCustomFont());
        textArea.setEditable(false);
        textArea.setForeground(Color.WHITE); // Set text color to white

        // Create a JPanel to center the text vertically and horizontally
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x060CE9));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(textArea, gbc);

        // Create a JPanel to hold the closeButton
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Add the panels to the dialog
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Set the background color for the button panel
        buttonPanel.setBackground(new Color(0x060CE9));

        // Get the screen dimensions and set it as the preferred size
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Dimension screenSize = gd.getDefaultConfiguration().getBounds().getSize();
        setPreferredSize(screenSize);

        pack(); // Calculate preferred size
        setLocationRelativeTo(parent); // Center the dialog relative to its parent
    }

    private Font getCustomFont() {
        String currentDirectory = System.getProperty("user.dir");
        String fontFilePath = currentDirectory + "/src/assets/fonts/itc-korrina.ttf";

        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(40f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

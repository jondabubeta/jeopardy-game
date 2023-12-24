import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class QuestionWindow extends JDialog {
    public QuestionWindow(Frame parent, String title, String content) {
        super(parent, title, Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        getContentPane().setBackground(new Color(0x060CE9));

        JTextArea textArea = new JTextArea(content);
        textArea.setBackground(new Color(6, 12, 233));
        textArea.setFont(getCustomFont());
        textArea.setEditable(false);
        textArea.setForeground(Color.WHITE); 
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x060CE9));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(textArea, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.setBackground(new Color(0x060CE9));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Dimension screenSize = gd.getDefaultConfiguration().getBounds().getSize();
        setPreferredSize(screenSize);

        pack();
        setLocationRelativeTo(parent);
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

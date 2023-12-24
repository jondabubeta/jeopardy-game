import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;


public class GameBoard extends JFrame implements ActionListener {
    private CategoryButton[] categoryButtons;
    private Question[][] buttons;
    private DefaultTableModel tableModel;

    static String currentDirectory = System.getProperty("user.dir");
    String fontFilePath = currentDirectory + "/src/assets/fonts/swiss-911.ttf";
    static String questionnairePath = currentDirectory + "/src/assets/fonts/questionnaires/q_genz.txt";

    public GameBoard(Category[] categories) {
        setTitle("Jeopardy Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());

        try {
        	Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(40f);

            JToolBar toolbar = new JToolBar();

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            JMenuItem setupMenuItem = new JMenuItem("Setup");

            setupMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    openSetupDialog();
                }
            });

            fileMenu.add(setupMenuItem);
            menuBar.add(fileMenu);
            setJMenuBar(menuBar);

            JPanel gamePanel = new JPanel();
            gamePanel.setLayout(new GridLayout(7, 6)); 

            categoryButtons = new CategoryButton[6];
            buttons = new Question[5][6]; 

            for (int col = 0; col < 6; col++) {
                categoryButtons[col] = new CategoryButton(categories[col]);
                categoryButtons[col].setFont(customFont);
                gamePanel.add(categoryButtons[col]);
            }

            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 6; col++) {
                    int moneyValue = 100 * (row + 1);
                    buttons[row][col] = new Question(categories[col], "$" + moneyValue);
                    buttons[row][col].setFont(customFont);
                    buttons[row][col].addActionListener(this);
                    gamePanel.add(buttons[row][col]);
                }
            }

            add(gamePanel, BorderLayout.CENTER);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();

        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Question) {
            Question clickedButton = (Question) e.getSource();
            Category category = clickedButton.getCategory();

            String categoryName = category.getName();
            String question = category.getNextQuestion();
            String answer = category.getAnswer(question);

            JOptionPane.showMessageDialog(this, categoryName + "\n\nQuestion: " + question + "\nAnswer: " + answer);

            clickedButton.setBackground(Color.GRAY); 
        }
    }

    private void openSetupDialog() {

        JDialog setupDialog = new JDialog(this, "Setup Categories and Questions", true);
        setupDialog.setSize(800, 600);
        setupDialog.setLayout(new BorderLayout());

        JPanel setupPanel = new JPanel(new GridLayout(6, 7));

        JTextField[] categoryFields = new JTextField[6];

        for (int col = 0; col < 6; col++) {
            String currentCategoryName = categoryButtons[col].getText();
            categoryFields[col] = new JTextField(currentCategoryName);
            setupPanel.add(categoryFields[col]);
        }

        JTextField[][] questionFields = new JTextField[5][6]; 

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                questionFields[row][col] = new JTextField();
                setupPanel.add(questionFields[row][col]);
            }
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int col = 0; col < 6; col++) {
                    categoryButtons[col].setText(categoryFields[col].getText());
                }

                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 6; col++) {
                        questionFields[row][col].setText("");
                    }
                }

                setupDialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);

        setupDialog.add(setupPanel, BorderLayout.CENTER);
        setupDialog.add(buttonPanel, BorderLayout.SOUTH);

        setupDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Category[] categories = createQuestionnaire();
            GameBoard gameBoard = new GameBoard(categories);
            gameBoard.setVisible(true);
        });
    }

    private void updateGameBoard(Category[] categories) {
        for (int col = 0; col < 6; col++) {
            categoryButtons[col].setCategory(categories[col]);
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                buttons[row][col].setCategory(categories[col]);
            }
        }
    }

    private static Category[] createQuestionnaire() {
        Category[] Questionnaire = createQuestionnaireFromFile(questionnairePath);
        return Questionnaire;
    }
    
    private static Category[] createQuestionnaireFromFile(String filePath) {
        List<Category> categoryList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String categoryName = null;
            List<String> questions = new ArrayList<>();
            List<String> answers = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Category ")) {
                    // Create a new category
                    if (categoryName != null) {
                        categoryList.add(new Category(categoryName, questions.toArray(new String[0]), answers.toArray(new String[0])));
                        questions.clear();
                        answers.clear();
                    }
                    categoryName = line.substring("Category ".length());
                } else {
                    // Parse question and answer
                    String[] parts = line.split(", ");
                    if (parts.length == 2) {
                        questions.add(parts[0]);
                        answers.add(parts[1]);
                    }
                }
            }

            if (categoryName != null) {
                categoryList.add(new Category(categoryName, questions.toArray(new String[0]), answers.toArray(new String[0])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categoryList.toArray(new Category[0]);
    }
   
}
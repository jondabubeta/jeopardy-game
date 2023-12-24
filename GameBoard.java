import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.io.BufferedWriter;

@SuppressWarnings("serial")
public class GameBoard extends JFrame implements ActionListener {
    private CategoryButton[] categoryButtons;
    private Question[][] buttons;
    private Category[] categories;
    static String currentDirectory = System.getProperty("user.dir");
    String fontFilePath = currentDirectory + "/src/assets/fonts/swiss-911.ttf";
    static String questionnairePath = currentDirectory + "/src/assets/fonts/questionnaires/q_genz.txt";

    public GameBoard(Category[] categories) {
        setTitle("Jeopardy Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Get the screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); 

        setLayout(new BorderLayout());
        this.categories = categories;
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(50f);

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
            gamePanel.setLayout(new GridLayout(6, 6));

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
    private void saveQuestionnaireToFile(Category[] categories, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Category category : categories) {
                writer.write("Category " + category.getName());
                writer.newLine();
                String[] questions = category.getQuestions();
                String[] answers = category.getAnswers();
                for (int i = 0; i < questions.length; i++) {
                    writer.write(questions[i] + ", " + answers[i]);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Question) {
            Question clickedButton = (Question) e.getSource();
            Category category = clickedButton.getCategory();

            String categoryName = category.getName();
            String question = category.getNextQuestion();
            // Create and display the question window
            QuestionWindow questionWindow = new QuestionWindow(this, categoryName, question);
            questionWindow.setVisible(true);

            // Change button color here if needed
            clickedButton.setBackground(Color.GRAY);
        }
    }

    private void updateCategoriesAndFile(Category[] categories, JTextField[][] questionFields) {
        for (int col = 0; col < 6; col++) {
            String[] newQuestions = new String[5];
            for (int row = 0; row < 5; row++) {
                newQuestions[row] = questionFields[row][col].getText();
            }
            categories[col].setQuestions(newQuestions);
        }

        // Save the updated Category objects to the file
        saveQuestionnaireToFile(categories, questionnairePath);
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
                String[] currentCategoryQuestions = categories[col].getQuestions();
                String currentQuestion = (currentCategoryQuestions != null && row < currentCategoryQuestions.length)
                        ? currentCategoryQuestions[row]
                        : "";
                questionFields[row][col] = new JTextField(currentQuestion);
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

                updateCategoriesAndFile(categories, questionFields);
                
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
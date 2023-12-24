import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JFrame implements ActionListener {
    private CategoryButton[] categoryButtons;
    private Question[][] buttons;
    private DefaultTableModel tableModel;

    public GameBoard(Category[] categories) {
        setTitle("Jeopardy Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());

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
            gamePanel.add(categoryButtons[col]);
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                int moneyValue = 100 * (row + 1);
                
                buttons[row][col] = new Question(categories[col], "$" + moneyValue);
                buttons[row][col].addActionListener(this);
                gamePanel.add(buttons[row][col]);
            }
        }

        add(gamePanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Question) {
            Question clickedButton = (Question) e.getSource();
            Category category = clickedButton.getCategory();

            // Display the question and answer in a dialog box
            String categoryName = category.getName();
            String question = category.getNextQuestion();
            String answer = category.getAnswer(question);

            JOptionPane.showMessageDialog(this, categoryName + "\n\nQuestion: " + question + "\nAnswer: " + answer);

            clickedButton.setBackground(Color.GRAY); 
        }
    }

    private void openSetupDialog() {
        // Create a setup dialog
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
            Category[] categories = createCategories();
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
    
    private static Category[] createCategories() {
        // Create an array of Category objects with 6 categories, each containing 5 questions
        Category[] categories = new Category[6];

        categories[0] = new Category("Category 1",
                new String[]{"Question 1-1", "Question 1-2", "Question 1-3", "Question 1-4", "Question 1-5"},
                new String[]{"Answer 1-1", "Answer 1-2", "Answer 1-3", "Answer 1-4", "Answer 1-5"});

        categories[1] = new Category("Category 2",
                new String[]{"Question 2-1", "Question 2-2", "Question 2-3", "Question 2-4", "Question 2-5"},
                new String[]{"Answer 2-1", "Answer 2-2", "Answer 2-3", "Answer 2-4", "Answer 2-5"});

        categories[2] = new Category("Category 3",
                new String[]{"Question 3-1", "Question 3-2", "Question 3-3", "Question 3-4", "Question 3-5"},
                new String[]{"Answer 3-1", "Answer 3-2", "Answer 3-3", "Answer 3-4", "Answer 3-5"});

        categories[3] = new Category("Category 4",
                new String[]{"Question 4-1", "Question 4-2", "Question 4-3", "Question 4-4", "Question 4-5"},
                new String[]{"Answer 4-1", "Answer 4-2", "Answer 4-3", "Answer 4-4", "Answer 4-5"});

        categories[4] = new Category("Category 5",
                new String[]{"Question 5-1", "Question 5-2", "Question 5-3", "Question 5-4", "Question 5-5"},
                new String[]{"Answer 5-1", "Answer 5-2", "Answer 5-3", "Answer 5-4", "Answer 5-5"});

        categories[5] = new Category("Category 6",
                new String[]{"Question 6-1", "Question 6-2", "Question 6-3", "Question 6-4", "Question 6-5"},
                new String[]{"Answer 6-1", "Answer 6-2", "Answer 6-3", "Answer 6-4", "Answer 6-5"});

        return categories;
    }
}

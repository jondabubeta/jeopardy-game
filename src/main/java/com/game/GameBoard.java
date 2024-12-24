package com.game;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.google.gson.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

@SuppressWarnings("serial")
public class GameBoard extends JFrame implements ActionListener {

    private CategoryButton[] categoryButtons;
    private Question[][] buttons;
    private Category[] categories;
    static String currentDirectory = System.getProperty("user.dir");
    static String jsonOutputPath = currentDirectory + "/src/main/java/com/assets/questions/importedQuestions.json";

    public GameBoard(Category[] categories) {
        setTitle("Jeopardy Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLayout(new BorderLayout());

        this.categories = categories;

        // Setup menu bar
        setupMenuBar();

        // Initialize the game panel
        JPanel gamePanel = new JPanel(new GridLayout(6, 6)); // Adjust grid for categories and questions
        initializeGamePanel(gamePanel);

        add(gamePanel, BorderLayout.CENTER);
    }

    // Menu bar setup
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        // Import menu item
        JMenuItem importMenuItem = new JMenuItem("Import");
        importMenuItem.addActionListener(e -> importQuestions());

        fileMenu.add(importMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    // Import questions from JSON or CSV file
    private void importQuestions() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Questions");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON or CSV files", "json", "csv"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            if (filePath.endsWith(".json")) {
                categories = loadQuestionsFromJSON(filePath);
            } else if (filePath.endsWith(".csv")) {
                categories = CSVReader.readCSVFile(filePath);
                saveToJSON(categories, jsonOutputPath); // Save CSV as JSON
            } else {
                JOptionPane.showMessageDialog(this, "Unsupported file format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            refreshGameBoard(); // Update GameBoard
        }
    }

    // Load questions from a JSON file
    private static Category[] loadQuestionsFromJSON(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            java.lang.reflect.Type categoryListType = new com.google.gson.reflect.TypeToken<List<Category>>() {}.getType();
            List<Category> categoryList = gson.fromJson(reader, categoryListType);
            return categoryList.toArray(new Category[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new Category[0];
        }
    }

    // Save imported categories to JSON
    private void saveToJSON(Category[] categories, String outputPath) {
        try (Writer writer = new FileWriter(outputPath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(categories, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Refresh the game board
    private void refreshGameBoard() {
        getContentPane().removeAll();

        JPanel gamePanel = new JPanel(new GridLayout(6, 6));
        initializeGamePanel(gamePanel);

        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Initialize the Game Panel
    private void initializeGamePanel(JPanel gamePanel) {
        categoryButtons = new CategoryButton[categories.length];
        buttons = new Question[5][categories.length];

        for (int col = 0; col < categories.length; col++) {
            categoryButtons[col] = new CategoryButton(categories[col]);
            categoryButtons[col].setFont(new Font("SansSerif", Font.BOLD, 30));
            gamePanel.add(categoryButtons[col]);
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < categories.length; col++) {
                if (row < categories[col].getQuestions().length) {
                    int score = categories[col].getQuestions()[row].getScore();
                    buttons[row][col] = new Question(categories[col], "$" + score);
                } else {
                    buttons[row][col] = new Question(categories[col], "N/A");
                    buttons[row][col].setEnabled(false);
                }
                buttons[row][col].addActionListener(this);
                gamePanel.add(buttons[row][col]);
            }
        }
    }

    // Handle button clicks for questions
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Question) {
            Question clickedButton = (Question) e.getSource();
            Category category = clickedButton.getCategory();

            String categoryName = category.getName();
            int row = Integer.parseInt(clickedButton.getText().replaceAll("[^0-9]", "")) / 100 - 1;
            String question = category.getQuestions()[row].getQuestion();
            String answer = category.getQuestions()[row].getAnswer();

            // Show the QuestionWindow
            QuestionWindow questionWindow = new QuestionWindow(this, categoryName, question);
            questionWindow.setVisible(true);

            // After QuestionWindow closes, show the AnswerWindow
            questionWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    AnswerWindow answerWindow = new AnswerWindow(GameBoard.this, categoryName + " - Answer", answer);
                    answerWindow.setVisible(true);
                }
            });

            clickedButton.markAnswered(); // Mark as answered
        }
    }

    // Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Category[] categories = new Category[0];
            new GameBoard(categories).setVisible(true);
        });
    }
}

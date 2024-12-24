package com.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    // Reads a CSV file and parses it into an array of Category objects
    public static Category[] readCSVFile(String filePath) {
        List<Category> categories = new ArrayList<>(); // Stores categories
        String categoryName = null; // Current category name
        List<QuestionData> questions = new ArrayList<>(); // Questions for the category

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;

            // Handle BOM (Byte Order Mark) if present
            line = br.readLine();
            if (line != null && line.startsWith("\uFEFF")) {
                line = line.substring(1); // Remove BOM
            }

            while (line != null) {
                line = line.trim(); // Trim whitespace

                // Skip completely empty lines or separators (",,")
                if (line.isEmpty() || line.equals(",,")) {
                    // If we have a valid category, save it
                    if (categoryName != null && !questions.isEmpty()) {
                        categories.add(new Category(categoryName, questions.toArray(new QuestionData[0])));
                        questions.clear(); // Clear questions for the next category
                    }
                    categoryName = null; // Reset category name
                    line = br.readLine(); // Move to the next line
                    continue;
                }

                // Parse the line
                String[] values = parseCSVLine(line);

                // Detect Category Declaration
                if (values.length == 3 && values[1].trim().isEmpty() && values[2].trim().isEmpty()) {
                    // Save the previous category if it exists
                    if (categoryName != null && !questions.isEmpty()) {
                        categories.add(new Category(categoryName, questions.toArray(new QuestionData[0])));
                        questions.clear(); // Reset for the new category
                    }
                    categoryName = values[0].trim(); // Set the new category name
                }
                // Process Question Rows (Score, Question, Answer)
                else if (values.length == 3) {
                    try {
                        // Validate if the first value is a score
                        int score = Integer.parseInt(values[0].trim()); // Score
                        String question = values[1].trim();            // Question
                        String answer = values[2].trim();              // Answer

                        // Add the question to the list
                        questions.add(new QuestionData(question, answer, score));
                    } catch (NumberFormatException e) {
                        // Handle invalid score rows gracefully
                        System.err.println("Skipping invalid row: " + line);
                    }
                }

                // Read the next line
                line = br.readLine();
            }

            // Add the last category if it exists
            if (categoryName != null && !questions.isEmpty()) {
                categories.add(new Category(categoryName, questions.toArray(new QuestionData[0])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories.toArray(new Category[0]);
    }

    // Helper method to parse CSV lines, handling quotes and commas
    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes; // Toggle quotes
            } else if (c == ',' && !insideQuotes) {
                values.add(current.toString().trim());
                current.setLength(0); // Reset buffer
            } else {
                current.append(c); // Add character
            }
        }

        values.add(current.toString().trim()); // Add the last value
        return values.toArray(new String[0]);
    }
}

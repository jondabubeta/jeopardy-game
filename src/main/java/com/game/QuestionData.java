package com.game;

public class QuestionData {
    private String question;
    private String answer;
    private int score;

    public QuestionData(String question, String answer, int score) {
        this.question = question;
        this.answer = answer;
        this.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }
}

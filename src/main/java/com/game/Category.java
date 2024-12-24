package com.game;

public class Category {
    private String name;
    private QuestionData[] questions;

    public Category(String name, QuestionData[] questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuestionData[] getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionData[] questions) {
        this.questions = questions;
    }

    public QuestionData getQuestionByIndex(int index) {
        if (index >= 0 && index < questions.length) {
            return questions[index];
        }
        return null;
    }
}

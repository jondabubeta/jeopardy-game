public class Category {
    private String name;
    private String[] questions;
    private String[] answers;
    private int currentQuestionIndex;

    public Category(String name, String[] questions, String[] answers) {
        this.name = name;
        this.questions = questions;
        this.answers = answers;
        this.currentQuestionIndex = 0;
    }

    public String getName() {
        return name;
    }

    public String getNextQuestion() {
        if (currentQuestionIndex < questions.length) {
            return questions[currentQuestionIndex++];
        }
        return null; 
    }
    public String[] getQuestions() {
        return questions;
    }
    public String getAnswer(String question) {
        int index = -1;
        for (int i = 0; i < questions.length; i++) {
            if (questions[i].equals(question)) {
                index = i;
                break;
            }
        }
        if (index >= 0 && index < answers.length) {
            return answers[index];
        }
        return null; 
    }
    
    public String[] getAnswers() {
        return answers;
    }
    
    public void setQuestions(String[] newQuestions) {
        this.questions = newQuestions;
        this.currentQuestionIndex = 0;
    }

    public void setAnswers(String[] newAnswers) {
        this.answers = newAnswers;
    }
}

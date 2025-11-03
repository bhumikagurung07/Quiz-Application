package com.example.quizapp;

public class Question {
    private String qId, question, optionA, optionB, optionC, optionD, correctAnswer;

    public Question(String qId, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.qId = qId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    public String getQId() { return qId; }
    public String getQuestion() { return question; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
}

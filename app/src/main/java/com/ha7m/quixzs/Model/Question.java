package com.ha7m.quixzs.Model;

public class Question {
    private String answer;
    private boolean answerTure;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTure() {
        return answerTure;
    }

    public void setAnswerTure(boolean answerTure) {
        this.answerTure = answerTure;
    }

    public Question() {
    }

    public Question(String answer, boolean answerTure) {
        this.answer = answer;
        this.answerTure = answerTure;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerTure=" + answerTure +
                '}';
    }
}

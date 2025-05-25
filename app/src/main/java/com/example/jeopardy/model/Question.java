package com.example.jeopardy.model;

import java.util.List;

public class Question {
    private String question;
    private List<String> answer;

    public Question(){}

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswer() {
        return answer;
    }
}
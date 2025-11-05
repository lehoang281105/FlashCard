package com.example.flashcard_quiz;

import java.io.Serializable;

public class Word implements Serializable {
    private String english;
    private String vietnamese;

    public Word() {
    }

    public Word(String english, String vietnamese) {
        this.english = english;
        this.vietnamese = vietnamese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}

package com.example.flashcardnnn.model;

import com.google.gson.annotations.SerializedName;

public class Word {
    @SerializedName("id")
    private String id;

    @SerializedName("english")
    private String english;

    @SerializedName("vietnamese")
    private String vietnamese;

    @SerializedName("example")
    private String example;

    @SerializedName("type")
    private String type;

    // Constructor
    public Word() {
    }

    public Word(String english, String vietnamese, String example, String type) {
        this.english = english;
        this.vietnamese = vietnamese;
        this.example = example;
        this.type = type;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

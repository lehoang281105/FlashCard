package com.example.flashcard_manager.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Word {
    @SerializedName("id")
    @Expose(serialize = false, deserialize = true) // Chỉ nhận từ API, không gửi lên
    private String id;

    // API sử dụng "english" - đây là trường chính trong MockAPI
    @SerializedName("english")
    @Expose
    private String word;

    // API sử dụng "vietnamese" - đây là trường chính trong MockAPI
    @SerializedName("vietnamese")
    @Expose
    private String meaning;

    // Pronunciation KHÔNG có trong MockAPI schema - không gửi lên API
    @SerializedName("pronunciation")
    @Expose(serialize = false, deserialize = false)
    private String pronunciation;

    // API sử dụng "example" - đây là trường chính trong MockAPI
    @SerializedName("example")
    @Expose
    private String example;

    // API sử dụng "field" - đây là trường chính trong MockAPI (thay cho topicId)
    @SerializedName("field")
    @Expose
    private String topicId;

    // topicName KHÔNG có trong MockAPI schema - không gửi lên API
    @SerializedName("topicName")
    @Expose(serialize = false, deserialize = false)
    private String topicName;

    public Word() {
    }

    public Word(String word, String meaning, String pronunciation, String example, String topicId, String topicName) {
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.example = example;
        this.topicId = topicId;
        this.topicName = topicName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}

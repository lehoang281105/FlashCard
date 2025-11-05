package com.example.flashcardnnn.api;

import com.example.flashcardnnn.model.Word;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Get all words
    @GET("words")
    Call<List<Word>> getWords();

    // Get a single word by ID
    @GET("words/{id}")
    Call<Word> getWord(@Path("id") String id);

    // Add new word (for Người 3)
    @POST("words")
    Call<Word> addWord(@Body Word word);

    // Update word (for Người 3)
    @PUT("words/{id}")
    Call<Word> updateWord(@Path("id") String id, @Body Word word);

    // Delete word (for Người 3)
    @DELETE("words/{id}")
    Call<Void> deleteWord(@Path("id") String id);
}

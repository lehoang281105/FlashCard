package com.example.flashcard_manager.api;

import com.example.flashcard_manager.models.Word;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WordApiService {

    @GET("words")
    Call<List<Word>> getAllWords();

    // MockAPI query theo tên field thực tế trong database: "field" chứ không phải "topicId"
    @GET("words")
    Call<List<Word>> getWordsByTopic(@Query("field") String topicId);

    @GET("words/{id}")
    Call<Word> getWordById(@Path("id") String id);

    @POST("words")
    Call<Word> createWord(@Body Word word);

    @PUT("words/{id}")
    Call<Word> updateWord(@Path("id") String id, @Body Word word);

    @DELETE("words/{id}")
    Call<Void> deleteWord(@Path("id") String id);
}

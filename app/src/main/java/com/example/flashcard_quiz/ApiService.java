package com.example.flashcard_quiz;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("words")
    Call<List<Word>> getWords();
}

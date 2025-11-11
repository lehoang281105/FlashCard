package com.example.flashcardnnn.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardnnn.R;
import com.example.flashcardnnn.adapter.FlashcardTopicAdapter;
import com.example.flashcard_manager.api.RetrofitClient;
import com.example.flashcard_manager.models.Topic;
import com.example.flashcard_manager.models.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardTopicSelectionActivity extends AppCompatActivity
        implements FlashcardTopicAdapter.OnTopicClickListener {

    private RecyclerView rvTopics;
    private FlashcardTopicAdapter adapter;
    private LinearLayout loadingView, emptyView, errorView;
    private TextView tvError;
    private Button btnRetry;
    private ImageView btnBack;

    private List<Topic> topicList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TopicPrefs";
    private static final String TOPICS_KEY = "topics";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_topic_selection);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        initViews();
        setupRecyclerView();
        loadTopics();
    }

    private void initViews() {
        rvTopics = findViewById(R.id.rvTopics);
        loadingView = findViewById(R.id.loadingView);
        emptyView = findViewById(R.id.emptyView);
        errorView = findViewById(R.id.errorView);
        tvError = findViewById(R.id.tvError);
        btnRetry = findViewById(R.id.btnRetry);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        btnRetry.setOnClickListener(v -> loadTopics());
    }

    private void setupRecyclerView() {
        adapter = new FlashcardTopicAdapter(topicList, this);
        rvTopics.setLayoutManager(new GridLayoutManager(this, 1));
        rvTopics.setAdapter(adapter);
    }

    private void loadTopics() {
        showLoading();

        // Load topics from SharedPreferences
        String json = sharedPreferences.getString(TOPICS_KEY, null);
        if (json != null) {
            Type type = new TypeToken<List<Topic>>() {}.getType();
            topicList = new Gson().fromJson(json, type);

            if (topicList == null) {
                topicList = new ArrayList<>();
            }
        } else {
            topicList = new ArrayList<>();
        }

        // Load words and update topic word counts
        loadWordsAndUpdateTopics();
    }

    private void loadWordsAndUpdateTopics() {
        RetrofitClient.getWordApiService().getAllWords().enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Word> words = response.body();
                    updateTopicWordCounts(words);

                    if (topicList.isEmpty()) {
                        showEmpty();
                    } else {
                        // Filter out topics with no words
                        List<Topic> topicsWithWords = new ArrayList<>();
                        for (Topic topic : topicList) {
                            if (topic.getWordCount() > 0) {
                                topicsWithWords.add(topic);
                            }
                        }

                        if (topicsWithWords.isEmpty()) {
                            showEmpty();
                        } else {
                            topicList.clear();
                            topicList.addAll(topicsWithWords);
                            adapter.notifyDataSetChanged();
                            showContent();
                        }
                    }
                } else {
                    showError("Lỗi tải dữ liệu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void updateTopicWordCounts(List<Word> words) {
        Map<String, Integer> countMap = new HashMap<>();

        for (Word word : words) {
            String topicId = word.getTopicId();
            if (topicId != null && !topicId.isEmpty()) {
                countMap.put(topicId, countMap.getOrDefault(topicId, 0) + 1);
            }
        }

        for (Topic topic : topicList) {
            Integer count = countMap.get(topic.getId());
            topic.setWordCount(count != null ? count : 0);
        }
    }

    @Override
    public void onTopicClick(Topic topic) {
        if (topic.getWordCount() == 0) {
            Toast.makeText(this, "Chủ đề này chưa có từ vựng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Open FlashcardActivity with selected topic
        Intent intent = new Intent(this, FlashcardActivity.class);
        intent.putExtra("topicId", topic.getId());
        intent.putExtra("topicName", topic.getName());
        startActivity(intent);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        rvTopics.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingView.setVisibility(View.GONE);
        rvTopics.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loadingView.setVisibility(View.GONE);
        rvTopics.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    private void showError(String message) {
        loadingView.setVisibility(View.GONE);
        rvTopics.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTopics();
    }
}

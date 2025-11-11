package com.example.flashcardnnn.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardnnn.R;
import com.example.flashcardnnn.activity.FlashcardActivity;
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

public class FlashcardTopicSelectionFragment extends Fragment
        implements FlashcardTopicAdapter.OnTopicClickListener {

    private RecyclerView rvTopics;
    private FlashcardTopicAdapter adapter;
    private LinearLayout loadingView, emptyView, errorView;
    private TextView tvError;
    private Button btnRetry;

    private List<Topic> topicList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TopicPrefs";
    private static final String TOPICS_KEY = "topics";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_topic_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        initViews(view);
        setupRecyclerView();
        loadTopics();
    }

    private void initViews(View view) {
        rvTopics = view.findViewById(R.id.rvTopics);
        loadingView = view.findViewById(R.id.loadingView);
        emptyView = view.findViewById(R.id.emptyView);
        errorView = view.findViewById(R.id.errorView);
        tvError = view.findViewById(R.id.tvError);
        btnRetry = view.findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(v -> loadTopics());
    }

    private void setupRecyclerView() {
        adapter = new FlashcardTopicAdapter(topicList, this);
        rvTopics.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvTopics.setAdapter(adapter);
    }

    private void loadTopics() {
        showLoading();

        // Load topics from SharedPreferences
        String json = sharedPreferences.getString(TOPICS_KEY, null);
        android.util.Log.d("FlashcardTopic", "JSON from SharedPreferences: " + json);

        if (json != null) {
            Type type = new TypeToken<List<Topic>>() {}.getType();
            topicList = new Gson().fromJson(json, type);

            if (topicList == null) {
                topicList = new ArrayList<>();
            }
        } else {
            topicList = new ArrayList<>();
        }

        android.util.Log.d("FlashcardTopic", "Loaded " + topicList.size() + " topics from SharedPreferences");

        // Load words and update topic word counts
        loadWordsAndUpdateTopics();
    }

    private void loadWordsAndUpdateTopics() {
        RetrofitClient.getWordApiService().getAllWords().enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Word> words = response.body();
                    android.util.Log.d("FlashcardTopic", "Loaded " + words.size() + " words from API");
                    updateTopicWordCounts(words);

                    if (topicList.isEmpty()) {
                        android.util.Log.d("FlashcardTopic", "Topic list is empty, showing empty view");
                        showEmpty();
                    } else {
                        // Filter out topics with no words
                        List<Topic> topicsWithWords = new ArrayList<>();
                        for (Topic topic : topicList) {
                            android.util.Log.d("FlashcardTopic", "Topic: " + topic.getName() + " - Words: " + topic.getWordCount());
                            if (topic.getWordCount() > 0) {
                                topicsWithWords.add(topic);
                            }
                        }

                        android.util.Log.d("FlashcardTopic", "Topics with words: " + topicsWithWords.size());

                        if (topicsWithWords.isEmpty()) {
                            showEmpty();
                        } else {
                            topicList.clear();
                            topicList.addAll(topicsWithWords);
                            adapter.updateTopics(topicList);
                            showContent();
                        }
                    }
                } else {
                    android.util.Log.e("FlashcardTopic", "API response failed: " + response.code());
                    showError("Lỗi tải dữ liệu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                android.util.Log.e("FlashcardTopic", "API call failed: " + t.getMessage());
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
            Toast.makeText(getContext(), "Chủ đề này chưa có từ vựng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Open FlashcardActivity with selected topic
        Intent intent = new Intent(getActivity(), FlashcardActivity.class);
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
    public void onResume() {
        super.onResume();
        loadTopics();
    }
}

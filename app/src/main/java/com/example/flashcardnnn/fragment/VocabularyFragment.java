package com.example.flashcardnnn.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardnnn.R;
import com.example.flashcard_manager.AddEditTopicActivity;
import com.example.flashcard_manager.TopicWordsActivity;
import com.example.flashcard_manager.adapters.TopicAdapter;
import com.example.flashcard_manager.api.RetrofitClient;
import com.example.flashcard_manager.models.Topic;
import com.example.flashcard_manager.models.Word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class VocabularyFragment extends Fragment implements TopicAdapter.OnTopicClickListener {

    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    private FloatingActionButton fabAddTopic;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TopicPrefs";
    private static final String TOPICS_KEY = "topics";
    private List<Topic> topicList = new ArrayList<>();
    private List<Topic> filteredTopicList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        initViews(view);
        setupRecyclerView();
        loadTopics();
        loadWordsAndUpdateTopics();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rvTopics);
        fabAddTopic = view.findViewById(R.id.fabAddTopic);
        progressBar = view.findViewById(R.id.progressBar);
        androidx.appcompat.widget.SearchView searchViewTopic = view.findViewById(R.id.searchViewTopic);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        fabAddTopic.setVisibility(View.VISIBLE);

        fabAddTopic.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditTopicActivity.class);
            startActivity(intent);
        });

        searchViewTopic.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTopics(newText);
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new TopicAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadTopics() {
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

        if (topicList.isEmpty()) {
            createDefaultTopics();
        }

        filteredTopicList = new ArrayList<>(topicList);
        adapter.setTopics(filteredTopicList);
    }

    private void createDefaultTopics() {
        Topic topic1 = new Topic("animal", "Động vật", "Từ vựng về các loài động vật");
        Topic topic2 = new Topic("food", "Thức ăn", "Từ vựng về đồ ăn và thức uống");
        Topic topic3 = new Topic("job", "Nghề nghiệp", "Từ vựng về các nghề nghiệp");
        Topic topic4 = new Topic("emotion", "Cảm xúc", "Từ vựng về cảm xúc");
        Topic topic5 = new Topic("school", "Trường học", "Từ vựng về trường học");
        Topic topic6 = new Topic("technology", "Công nghệ", "Từ vựng về công nghệ");
        Topic topic7 = new Topic("transport", "Phương tiện", "Từ vựng về phương tiện giao thông");
        Topic topic8 = new Topic("home", "Nhà cửa", "Từ vựng về nhà cửa");
        Topic topic9 = new Topic("nature", "Thiên nhiên", "Từ vựng về thiên nhiên");
        Topic topic10 = new Topic("sport", "Thể thao", "Từ vựng về thể thao");
        Topic topic11 = new Topic("hobby", "Sở thích", "Từ vựng về sở thích");
        Topic topic12 = new Topic("weather", "Thời tiết", "Từ vựng về thời tiết");
        Topic topic13 = new Topic("color", "Màu sắc", "Từ vựng về màu sắc");
        Topic topic14 = new Topic("people", "Con người", "Từ vựng về con người");

        topicList.add(topic1);
        topicList.add(topic2);
        topicList.add(topic3);
        topicList.add(topic4);
        topicList.add(topic5);
        topicList.add(topic6);
        topicList.add(topic7);
        topicList.add(topic8);
        topicList.add(topic9);
        topicList.add(topic10);
        topicList.add(topic11);
        topicList.add(topic12);
        topicList.add(topic13);
        topicList.add(topic14);

        saveTopics();
    }

    private void saveTopics() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(topicList);
        editor.putString(TOPICS_KEY, json);
        editor.apply();
    }

    private void loadWordsAndUpdateTopics() {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getWordApiService().getAllWords().enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Word> words = response.body();
                    updateTopicWordCounts(words);
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),
                        "Lỗi khi tải dữ liệu: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTopicWordCounts(List<Word> words) {
        Map<String, Integer> wordCounts = new HashMap<>();

        for (Word word : words) {
            String topicId = word.getTopicId();
            if (topicId != null && !topicId.isEmpty()) {
                wordCounts.put(topicId, wordCounts.getOrDefault(topicId, 0) + 1);
            }
        }

        for (Topic topic : topicList) {
            int count = wordCounts.getOrDefault(topic.getId(), 0);
            topic.setWordCount(count);
        }

        adapter.setTopics(filteredTopicList);
    }

    private void filterTopics(String query) {
        filteredTopicList.clear();

        if (query == null || query.trim().isEmpty()) {
            filteredTopicList.addAll(topicList);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Topic topic : topicList) {
                if (topic.getName().toLowerCase().contains(lowerCaseQuery) ||
                    topic.getDescription().toLowerCase().contains(lowerCaseQuery)) {
                    filteredTopicList.add(topic);
                }
            }
        }

        adapter.setTopics(filteredTopicList);
        tvEmptyState.setVisibility(filteredTopicList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTopicClick(Topic topic) {
        Intent intent = new Intent(getActivity(), TopicWordsActivity.class);
        intent.putExtra("topicId", topic.getId());
        intent.putExtra("topicName", topic.getName());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Topic topic) {
        Intent intent = new Intent(getActivity(), AddEditTopicActivity.class);
        intent.putExtra("topicId", topic.getId());
        intent.putExtra("topicName", topic.getName());
        intent.putExtra("topicDescription", topic.getDescription());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Topic topic) {
        topicList.remove(topic);
        filteredTopicList.remove(topic);
        saveTopics();
        adapter.setTopics(filteredTopicList);
        Toast.makeText(getContext(), "Đã xóa: " + topic.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTopics();
        loadWordsAndUpdateTopics();
    }
}

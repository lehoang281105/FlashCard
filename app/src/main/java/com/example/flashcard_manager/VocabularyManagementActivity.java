package com.example.flashcard_manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard_manager.adapters.TopicAdapter;
import com.example.flashcard_manager.api.RetrofitClient;
import com.example.flashcard_manager.models.Topic;
import com.example.flashcard_manager.models.Word;
import com.example.flashcard_manager.utils.SampleDataGenerator;
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

public class VocabularyManagementActivity extends AppCompatActivity implements TopicAdapter.OnTopicClickListener {

    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    private FloatingActionButton fabAddTopic;
    private ProgressBar progressBar;
    private androidx.appcompat.widget.SearchView searchViewTopic;
    private TextView tvEmptyState;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TopicPrefs";
    private static final String TOPICS_KEY = "topics";
    private List<Topic> topicList = new ArrayList<>();
    private List<Topic> filteredTopicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_management);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initViews();
        setupRecyclerView();
        loadTopics();
        loadWordsAndUpdateTopics();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvTopics);
        fabAddTopic = findViewById(R.id.fabAddTopic);
        progressBar = findViewById(R.id.progressBar);
        androidx.appcompat.widget.SearchView searchViewTopic = findViewById(R.id.searchViewTopic);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        // Kích hoạt nút thêm topic
        fabAddTopic.setVisibility(View.VISIBLE);

        fabAddTopic.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTopicActivity.class);
            startActivity(intent);
        });

        // Setup search functionality
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        // Nếu chưa có topic nào, tạo các topic mặc định
        if (topicList.isEmpty()) {
            createDefaultTopics();
        }

        filteredTopicList = new ArrayList<>(topicList);
        adapter.setTopics(filteredTopicList);
    }

    private void createDefaultTopics() {
        // Các chủ đề phù hợp với field trong API
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

        showFirstTimeDialog();
    }

    private void showFirstTimeDialog() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            new AlertDialog.Builder(this)
                    .setTitle("🎉 Chào mừng đến với FlashLearn!")
                    .setMessage("Bạn đã có 14 chủ đề với 70 từ vựng!\n\n" +
                            "📚 Click vào chủ đề để xem từ vựng\n" +
                            "➕ Nhấn nút + để thêm chủ đề/từ vựng mới\n" +
                            "🔍 Sử dụng thanh tìm kiếm để tìm nhanh\n\n" +
                            "Chúc bạn học tập vui vẻ!")
                    .setPositiveButton("Bắt đầu", null)
                    .setCancelable(false)
                    .show();

            prefs.edit().putBoolean("isFirstTime", false).apply();
        }
    }

    private void saveTopics() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(topicList);
        editor.putString(TOPICS_KEY, json);
        editor.apply();
    }

    private void loadWordsAndUpdateTopics() {
        progressBar.setVisibility(View.VISIBLE);

        android.util.Log.d("VocabManagement", "===== BẮT ĐẦU LOAD ALL WORDS =====");

        RetrofitClient.getWordApiService().getAllWords().enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                progressBar.setVisibility(View.GONE);
                android.util.Log.d("VocabManagement", "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<Word> words = response.body();
                    android.util.Log.d("VocabManagement", "Tổng số từ: " + words.size());

                    // Log 3 từ đầu tiên để kiểm tra structure
                    for (int i = 0; i < Math.min(words.size(), 3); i++) {
                        Word w = words.get(i);
                        android.util.Log.d("VocabManagement", "Word " + i + ": " +
                                w.getWord() + " | " + w.getMeaning() + " | field: " + w.getTopicId());
                    }

                    updateTopicWordCounts(words);
                } else {
                    android.util.Log.e("VocabManagement", "Response KHÔNG thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                android.util.Log.e("VocabManagement", "API FAILURE: " + t.getMessage(), t);
                Toast.makeText(VocabularyManagementActivity.this,
                        "Lỗi khi tải dữ liệu: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTopicWordCounts(List<Word> words) {
        Map<String, Integer> wordCounts = new HashMap<>();

        android.util.Log.d("VocabManagement", "===== PHÂN TÍCH FIELD CỦA CÁC TỪ =====");

        // Đếm từng field value
        Map<String, Integer> fieldStats = new HashMap<>();

        for (Word word : words) {
            String topicId = word.getTopicId();

            // Log chi tiết 10 từ đầu
            if (wordCounts.size() < 10) {
                android.util.Log.d("VocabManagement",
                    "Word: " + word.getWord() + " | field: '" + topicId + "'");
            }

            if (topicId != null && !topicId.isEmpty()) {
                wordCounts.put(topicId, wordCounts.getOrDefault(topicId, 0) + 1);
                fieldStats.put(topicId, fieldStats.getOrDefault(topicId, 0) + 1);
            }
        }

        android.util.Log.d("VocabManagement", "===== THỐNG KÊ FIELD VALUES =====");
        for (Map.Entry<String, Integer> entry : fieldStats.entrySet()) {
            android.util.Log.d("VocabManagement",
                "Field: '" + entry.getKey() + "' → " + entry.getValue() + " từ");
        }

        android.util.Log.d("VocabManagement", "===== DANH SÁCH TOPICS HIỆN TẠI =====");
        for (Topic topic : topicList) {
            android.util.Log.d("VocabManagement",
                "Topic ID: '" + topic.getId() + "' | Name: " + topic.getName());
        }

        for (Topic topic : topicList) {
            topic.setWordCount(wordCounts.getOrDefault(topic.getId(), 0));
        }

        filteredTopicList = new ArrayList<>(topicList);
        adapter.setTopics(filteredTopicList);
    }

    @Override
    public void onTopicClick(Topic topic) {
        if (topic == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin chủ đề", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, TopicWordsActivity.class);
        intent.putExtra("topicId", topic.getId() != null ? topic.getId() : "");
        intent.putExtra("topicName", topic.getName() != null ? topic.getName() : "Chủ đề");
        startActivity(intent);
    }

    @Override
    public void onEditClick(Topic topic) {
        Intent intent = new Intent(this, AddEditTopicActivity.class);
        intent.putExtra("topicId", topic.getId());
        intent.putExtra("topicName", topic.getName());
        intent.putExtra("topicDescription", topic.getDescription());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Topic topic) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Topic")
                .setMessage("Bạn có chắc muốn xóa topic \"" + topic.getName() + "\"?\nTất cả từ vựng trong topic này sẽ bị xóa.")
                .setPositiveButton("Xóa", (dialog, which) -> deleteTopic(topic))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteTopic(Topic topic) {
        progressBar.setVisibility(View.VISIBLE);

        // Xóa tất cả từ vựng trong topic
        RetrofitClient.getWordApiService().getWordsByTopic(topic.getId()).enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Word> words = response.body();
                    deleteWordsInTopic(words, topic);
                } else {
                    progressBar.setVisibility(View.GONE);
                    removeTopicFromList(topic);
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                removeTopicFromList(topic);
            }
        });
    }

    private void deleteWordsInTopic(List<Word> words, Topic topic) {
        if (words.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            removeTopicFromList(topic);
            return;
        }

        for (Word word : words) {
            RetrofitClient.getWordApiService().deleteWord(word.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });
        }

        progressBar.setVisibility(View.GONE);
        removeTopicFromList(topic);
    }

    private void removeTopicFromList(Topic topic) {
        topicList.remove(topic);
        filteredTopicList.remove(topic);
        saveTopics();
        adapter.setTopics(filteredTopicList);
        Toast.makeText(this, "Đã xóa topic", Toast.LENGTH_SHORT).show();
    }

    private void filterTopics(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredTopicList = new ArrayList<>(topicList);
        } else {
            filteredTopicList = new ArrayList<>();
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Topic topic : topicList) {
                if (topic.getName().toLowerCase().contains(lowerCaseQuery) ||
                    topic.getDescription().toLowerCase().contains(lowerCaseQuery)) {
                    filteredTopicList.add(topic);
                }
            }
        }

        adapter.setTopics(filteredTopicList);

        if (filteredTopicList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vocabulary_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_all_words) {
            Intent intent = new Intent(this, AllWordsActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_load_sample_data) {
            loadSampleData();
            return true;
        } else if (item.getItemId() == R.id.action_reset_topics) {
            resetTopicsToDefault();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetTopicsToDefault() {
        new AlertDialog.Builder(this)
                .setTitle("Reset chủ đề")
                .setMessage("⚠️ Điều này sẽ:\n\n" +
                        "✓ Xóa TẤT CẢ các chủ đề hiện tại\n" +
                        "✓ Tạo lại 14 chủ đề mặc định khớp với MockAPI\n" +
                        "✓ Các từ vựng trong API sẽ KHÔNG bị xóa\n\n" +
                        "Bạn có chắc chắn không?")
                .setPositiveButton("Reset", (dialog, which) -> {
                    // Xóa topics cũ
                    topicList.clear();

                    // Tạo lại topics mặc định
                    createDefaultTopics();

                    // Load lại word counts
                    loadWordsAndUpdateTopics();

                    Toast.makeText(this,
                            "Đã reset về 14 chủ đề mặc định!",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadSampleData() {
        new AlertDialog.Builder(this)
                .setTitle("Tải dữ liệu mẫu")
                .setMessage("Bạn có muốn tải 50 từ vựng mẫu vào 5 chủ đề không?\n\n" +
                        "• 10 từ cho mỗi chủ đề\n" +
                        "• Dữ liệu sẽ được thêm vào MockAPI")
                .setPositiveButton("Tải", (dialog, which) -> {
                    progressBar.setVisibility(View.VISIBLE);
                    SampleDataGenerator.generateSampleWords(new SampleDataGenerator.OnDataLoadedListener() {
                        @Override
                        public void onSuccess(int count) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(VocabularyManagementActivity.this,
                                        "Đã thêm " + count + " từ vựng mẫu!",
                                        Toast.LENGTH_LONG).show();
                                loadWordsAndUpdateTopics();
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(VocabularyManagementActivity.this,
                                        "Lỗi: " + error,
                                        Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTopics();
        loadWordsAndUpdateTopics();
    }
}

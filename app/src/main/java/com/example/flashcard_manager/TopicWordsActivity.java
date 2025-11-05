package com.example.flashcard_manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard_manager.adapters.WordAdapter;
import com.example.flashcard_manager.api.RetrofitClient;
import com.example.flashcard_manager.models.Word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicWordsActivity extends AppCompatActivity implements WordAdapter.OnWordClickListener {

    private RecyclerView recyclerView;
    private WordAdapter adapter;
    private FloatingActionButton fabAddWord;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private String topicId;
    private String topicName;
    private List<Word> wordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_words);

        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("topicName");

        setTitle(topicName);

        initViews();
        setupRecyclerView();
        loadWords();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvWords);
        fabAddWord = findViewById(R.id.fabAddWord);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        fabAddWord.setOnClickListener(v -> {
            Intent intent = new Intent(TopicWordsActivity.this, AddEditWordActivity.class);
            intent.putExtra("topicId", topicId);
            intent.putExtra("topicName", topicName);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new WordAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadWords() {
        if (topicId == null || topicId.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có thông tin chủ đề", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);

        android.util.Log.d("TopicWords", "===== BẮT ĐẦU LOAD WORDS =====");
        android.util.Log.d("TopicWords", "Topic ID (field): " + topicId);
        android.util.Log.d("TopicWords", "Topic Name: " + topicName);
        android.util.Log.d("TopicWords", "Lấy TẤT CẢ words rồi filter theo field: " + topicId);

        // MockAPI KHÔNG hỗ trợ query by field, phải lấy tất cả rồi filter ở client
        RetrofitClient.getWordApiService().getAllWords().enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                progressBar.setVisibility(View.GONE);

                android.util.Log.d("TopicWords", "Response code: " + response.code());
                android.util.Log.d("TopicWords", "Response successful: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    List<Word> allWords = response.body();
                    android.util.Log.d("TopicWords", "Tổng số từ: " + allWords.size());

                    // Filter words theo topicId (field)
                    wordList = new ArrayList<>();
                    for (Word word : allWords) {
                        String wordField = word.getTopicId();
                        android.util.Log.d("TopicWords", "Checking word: " + word.getWord() + " | field: " + wordField);

                        if (wordField != null && wordField.equals(topicId)) {
                            wordList.add(word);
                        }
                    }

                    android.util.Log.d("TopicWords", "Số từ sau khi filter: " + wordList.size());

                    adapter.setWords(wordList);

                    if (wordList.isEmpty()) {
                        tvEmptyState.setVisibility(View.VISIBLE);
                        android.util.Log.d("TopicWords", "Không có từ nào cho field: " + topicId);
                    }
                } else {
                    android.util.Log.e("TopicWords", "Response KHÔNG thành công!");
                    tvEmptyState.setVisibility(View.VISIBLE);
                    Toast.makeText(TopicWordsActivity.this,
                            "Lỗi khi tải từ vựng (code: " + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
                android.util.Log.e("TopicWords", "API FAILURE: " + t.getMessage(), t);
                Toast.makeText(TopicWordsActivity.this,
                        "Lỗi: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(Word word) {
        Intent intent = new Intent(this, AddEditWordActivity.class);
        intent.putExtra("wordId", word.getId());
        intent.putExtra("word", word.getWord());
        intent.putExtra("meaning", word.getMeaning());
        intent.putExtra("pronunciation", word.getPronunciation());
        intent.putExtra("example", word.getExample());
        intent.putExtra("topicId", word.getTopicId());
        intent.putExtra("topicName", word.getTopicName());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Word word) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa từ vựng")
                .setMessage("Bạn có chắc muốn xóa từ \"" + word.getWord() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteWord(word))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteWord(Word word) {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getWordApiService().deleteWord(word.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(TopicWordsActivity.this,
                            "Đã xóa từ vựng",
                            Toast.LENGTH_SHORT).show();
                    loadWords();
                } else {
                    Toast.makeText(TopicWordsActivity.this,
                            "Lỗi khi xóa từ vựng",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TopicWordsActivity.this,
                        "Lỗi: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWords();
    }
}

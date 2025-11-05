package com.example.flashcard_manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class AllWordsActivity extends AppCompatActivity implements WordAdapter.OnWordClickListener {

    private RecyclerView recyclerView;
    private WordAdapter adapter;
    private FloatingActionButton fabAddWord;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private TextView tvWordCount;
    private List<Word> wordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_words);

        setTitle("Tất cả từ vựng");

        initViews();
        setupRecyclerView();
        loadAllWords();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvWords);
        fabAddWord = findViewById(R.id.fabAddWord);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvWordCount = findViewById(R.id.tvWordCount);

        fabAddWord.setOnClickListener(v -> {
            Intent intent = new Intent(AllWordsActivity.this, AddEditWordActivity.class);
            intent.putExtra("topicId", "");
            intent.putExtra("topicName", "Chưa phân loại");
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new WordAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadAllWords() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        tvWordCount.setVisibility(View.GONE);

        RetrofitClient.getWordApiService().getAllWords().enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    wordList = response.body();

                    // Log để debug
                    Log.d("AllWordsActivity", "Số lượng từ: " + wordList.size());
                    if (!wordList.isEmpty()) {
                        Word firstWord = wordList.get(0);
                        Log.d("AllWordsActivity", "Từ đầu tiên - ID: " + firstWord.getId());
                        Log.d("AllWordsActivity", "Word: " + firstWord.getWord());
                        Log.d("AllWordsActivity", "Meaning: " + firstWord.getMeaning());
                        Log.d("AllWordsActivity", "Pronunciation: " + firstWord.getPronunciation());
                        Log.d("AllWordsActivity", "Example: " + firstWord.getExample());
                    }

                    adapter.setWords(wordList);

                    if (wordList.isEmpty()) {
                        tvEmptyState.setVisibility(View.VISIBLE);
                        tvWordCount.setVisibility(View.GONE);
                    } else {
                        tvEmptyState.setVisibility(View.GONE);
                        tvWordCount.setVisibility(View.VISIBLE);
                        tvWordCount.setText("Tổng số: " + wordList.size() + " từ");
                    }
                } else {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    Toast.makeText(AllWordsActivity.this,
                            "Lỗi khi tải từ vựng",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
                Toast.makeText(AllWordsActivity.this,
                        "Lỗi: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                t.printStackTrace();
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
        intent.putExtra("topicId", word.getTopicId() != null ? word.getTopicId() : "");
        intent.putExtra("topicName", word.getTopicName() != null ? word.getTopicName() : "Chưa phân loại");
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
                    Toast.makeText(AllWordsActivity.this,
                            "Đã xóa từ vựng",
                            Toast.LENGTH_SHORT).show();
                    loadAllWords();
                } else {
                    Toast.makeText(AllWordsActivity.this,
                            "Lỗi khi xóa từ vựng",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AllWordsActivity.this,
                        "Lỗi: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllWords();
    }
}

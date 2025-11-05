package com.example.flashcard_manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flashcardnnn.R;
import com.example.flashcard_manager.api.RetrofitClient;
import com.example.flashcard_manager.models.Word;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditWordActivity extends AppCompatActivity {
    private EditText etWord, etMeaning, etPronunciation, etExample;
    private Button btnSave, btnCancel;
    private ProgressBar progressBar;
    private android.widget.TextView tvTopicName;
    private String wordId, topicId, topicName;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_word);
        initViews();
        setupListeners();
        new Handler(Looper.getMainLooper()).postDelayed(() -> loadAndFillData(), 300);
    }

    private void initViews() {
        etWord = findViewById(R.id.etWord);
        etMeaning = findViewById(R.id.etMeaning);
        etPronunciation = findViewById(R.id.etPronunciation);
        etExample = findViewById(R.id.etExample);
        btnSave = findViewById(R.id.btnSaveWord);
        btnCancel = findViewById(R.id.btnCancelWord);
        progressBar = findViewById(R.id.progressBar);
        tvTopicName = findViewById(R.id.tvTopicName);

        wordId = getIntent().getStringExtra("wordId");
        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("topicName");

        Log.d("AddEditWord", "wordId: " + wordId);
        tvTopicName.setText(topicName != null ? topicName : "Chưa chọn chủ đề");

        String word = getIntent().getStringExtra("word");
        if ((wordId != null && !wordId.isEmpty()) || word != null) {
            isEditMode = true;
            setTitle("Chỉnh sửa từ vựng");
        } else {
            setTitle("Thêm từ mới");
        }
    }

    private void loadAndFillData() {
        if (!isEditMode) return;

        String word = getIntent().getStringExtra("word");
        String meaning = getIntent().getStringExtra("meaning");
        String pronunciation = getIntent().getStringExtra("pronunciation");
        String example = getIntent().getStringExtra("example");

        Log.d("AddEditWord", "Pre-fill: word='" + word + "', meaning='" + meaning + "'");

        int filled = 0;
        if (word != null && !word.isEmpty()) {
            etWord.setText(word);
            filled++;
        }
        if (meaning != null && !meaning.isEmpty()) {
            etMeaning.setText(meaning);
            filled++;
        }
        if (pronunciation != null && !pronunciation.isEmpty()) {
            etPronunciation.setText(pronunciation);
            filled++;
        }
        if (example != null && !example.isEmpty()) {
            etExample.setText(example);
            filled++;
        }

        if (filled > 0) {
            Toast.makeText(this, "✓ Đã tải " + filled + " trường", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveWord());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveWord() {
        String word = etWord.getText().toString().trim();
        String meaning = etMeaning.getText().toString().trim();
        if (word.isEmpty() || meaning.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Word wordObj = new Word(word, meaning,
            etPronunciation.getText().toString().trim(),
            etExample.getText().toString().trim(), topicId, topicName);
        progressBar.setVisibility(View.VISIBLE);

        if (isEditMode) {
            updateWord(wordObj);
        } else {
            createWord(wordObj);
        }
    }

    private void createWord(Word word) {
        RetrofitClient.getWordApiService().createWord(word).enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditWordActivity.this, "✓ Đã thêm", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddEditWordActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWord(Word word) {
        RetrofitClient.getWordApiService().updateWord(wordId, word).enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditWordActivity.this, "✓ Đã cập nhật", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddEditWordActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


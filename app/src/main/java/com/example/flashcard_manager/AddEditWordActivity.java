package com.example.flashcard_manager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private String wordId;
    private String topicId;
    private String topicName;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_word);

        initViews();
        loadWordData();
        setupListeners();
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

        // Hiển thị tên chủ đề
        if (topicName != null && !topicName.isEmpty()) {
            tvTopicName.setText(topicName);
        } else {
            tvTopicName.setText("Chưa chọn chủ đề");
        }

        if (wordId != null) {
            isEditMode = true;
            setTitle("Sửa từ vựng");
        } else {
            setTitle("Thêm từ vựng mới - " + topicName);
        }
    }

    private void loadWordData() {
        if (isEditMode) {
            String word = getIntent().getStringExtra("word");
            String meaning = getIntent().getStringExtra("meaning");
            String pronunciation = getIntent().getStringExtra("pronunciation");
            String example = getIntent().getStringExtra("example");

            etWord.setText(word);
            etMeaning.setText(meaning);
            etPronunciation.setText(pronunciation);
            etExample.setText(example);
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveWord());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveWord() {
        String word = etWord.getText().toString().trim();
        String meaning = etMeaning.getText().toString().trim();
        String pronunciation = etPronunciation.getText().toString().trim();
        String example = etExample.getText().toString().trim();

        if (word.isEmpty() || meaning.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập từ và nghĩa", Toast.LENGTH_SHORT).show();
            return;
        }

        android.util.Log.d("AddEditWord", "===== SAVE WORD =====");
        android.util.Log.d("AddEditWord", "Topic ID (field sẽ gửi lên API): '" + topicId + "'");
        android.util.Log.d("AddEditWord", "Topic Name: '" + topicName + "'");
        android.util.Log.d("AddEditWord", "⚠️ CHÚ Ý: field phải là 'animal', 'food', 'people', etc. KHÔNG phải '1', '2', '3' hay UUID!");

        Word wordObj = new Word(word, meaning, pronunciation, example, topicId, topicName);

        progressBar.setVisibility(View.VISIBLE);

        if (isEditMode) {
            updateWord(wordObj);
        } else {
            createWord(wordObj);
        }
    }

    private void createWord(Word word) {
        Log.d("AddEditWord", "===== TẠO TỪ MỚI =====");
        Log.d("AddEditWord", "Word (english): " + word.getWord());
        Log.d("AddEditWord", "Meaning (vietnamese): " + word.getMeaning());
        Log.d("AddEditWord", "Topic ID (field): " + word.getTopicId());
        Log.d("AddEditWord", "Topic Name: " + word.getTopicName());
        Log.d("AddEditWord", "Pronunciation: " + word.getPronunciation());
        Log.d("AddEditWord", "Example: " + word.getExample());

        RetrofitClient.getWordApiService().createWord(word).enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Word createdWord = response.body();
                    Log.d("AddEditWord", "✅ THÀNH CÔNG!");
                    Log.d("AddEditWord", "Created word ID: " + createdWord.getId());
                    Log.d("AddEditWord", "Created word: " + createdWord.getWord());
                    Log.d("AddEditWord", "Created meaning: " + createdWord.getMeaning());
                    Log.d("AddEditWord", "Created field: " + createdWord.getTopicId());

                    Toast.makeText(AddEditWordActivity.this,
                            "Đã thêm từ vựng vào chủ đề " + topicName,
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("AddEditWord", "❌ Response không thành công: " + response.code());
                    Toast.makeText(AddEditWordActivity.this,
                            "Lỗi khi thêm từ vựng (code: " + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("AddEditWord", "❌ API FAILURE: " + t.getMessage(), t);
                Toast.makeText(AddEditWordActivity.this,
                        "Lỗi: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWord(Word word) {
        RetrofitClient.getWordApiService().updateWord(wordId, word).enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditWordActivity.this,
                            "Đã cập nhật từ vựng",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEditWordActivity.this,
                            "Lỗi khi cập nhật từ vựng",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddEditWordActivity.this,
                        "Lỗi: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

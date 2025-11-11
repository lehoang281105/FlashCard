package com.example.flashcardnnn.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flashcardnnn.R;
import com.example.flashcardnnn.adapter.FlashcardAdapter;
import com.example.flashcardnnn.api.RetrofitClient;
import com.example.flashcardnnn.model.Word;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardActivity extends AppCompatActivity {

    private static final String TAG = "FlashcardActivity";

    private ViewPager2 viewPager;
    private FlashcardAdapter adapter;
    private TextView tvProgress, tvTopicTitle, tvTopicSubtitle;
    private MaterialButton btnPrevious, btnNext;
    private ImageView btnBack;

    private LinearLayout loadingView, errorView, emptyView;
    private TextView tvError;
    private Button btnRetry;

    private List<Word> currentWordList = new ArrayList<>();
    private String topicId;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Get topic info from intent
        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("topicName");

        initViews();
        setupListeners();
        loadWords();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tvProgress = findViewById(R.id.tvProgress);
        tvTopicTitle = findViewById(R.id.tvTopicTitle);
        tvTopicSubtitle = findViewById(R.id.tvTopicSubtitle);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        loadingView = findViewById(R.id.loadingView);
        errorView = findViewById(R.id.errorView);
        emptyView = findViewById(R.id.emptyView);
        tvError = findViewById(R.id.tvError);
        btnRetry = findViewById(R.id.btnRetry);

        // Set topic name
        if (topicName != null && !topicName.isEmpty()) {
            tvTopicTitle.setText("Flashcard");
            tvTopicSubtitle.setText(topicName);
        }

        // Initialize adapter
        adapter = new FlashcardAdapter(currentWordList);
        viewPager.setAdapter(adapter);

        // Setup ViewPager callback
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateProgress(position);
                updateNavigationButtons(position);
            }
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPrevious.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true);
            }
        });

        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < currentWordList.size() - 1) {
                viewPager.setCurrentItem(currentItem + 1, true);
            }
        });

        btnRetry.setOnClickListener(v -> loadWords());
    }

    private void loadWords() {
        showLoading();

        // Load words by topic
        com.example.flashcard_manager.api.RetrofitClient.getWordApiService()
                .getWordsByTopic(topicId)
                .enqueue(new Callback<List<com.example.flashcard_manager.models.Word>>() {
            @Override
            public void onResponse(Call<List<com.example.flashcard_manager.models.Word>> call,
                                 Response<List<com.example.flashcard_manager.models.Word>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.flashcard_manager.models.Word> words = response.body();

                    if (words.isEmpty()) {
                        showEmpty();
                    } else {
                        currentWordList.clear();

                        // Convert to flashcard Word model
                        for (com.example.flashcard_manager.models.Word w : words) {
                            Word word = new Word();
                            word.setId(w.getId());
                            word.setEnglish(w.getWord());
                            word.setVietnamese(w.getMeaning());
                            word.setExample(w.getExample());
                            word.setType(w.getPronunciation() != null ? w.getPronunciation() : "");
                            currentWordList.add(word);
                        }

                        showContent();

                        adapter.notifyDataSetChanged();

                        // Set to first item
                        viewPager.post(() -> {
                            viewPager.setCurrentItem(0, false);
                            updateProgress(0);
                            updateNavigationButtons(0);
                        });

                        Log.d(TAG, "Loaded " + currentWordList.size() + " words for topic: " + topicName);
                    }
                } else {
                    showError("Lỗi tải dữ liệu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<com.example.flashcard_manager.models.Word>> call, Throwable t) {
                Log.e(TAG, "Error loading words", t);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void updateProgress(int position) {
        if (currentWordList == null || currentWordList.isEmpty()) {
            tvProgress.setText("0 / 0");
        } else {
            if (position < 0) position = 0;
            if (position >= currentWordList.size()) position = currentWordList.size() - 1;

            tvProgress.setText((position + 1) + " / " + currentWordList.size());
        }
    }

    private void updateNavigationButtons(int position) {
        if (currentWordList == null || currentWordList.isEmpty()) {
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
            btnPrevious.setAlpha(0.5f);
            btnNext.setAlpha(0.5f);
            return;
        }

        btnPrevious.setEnabled(position > 0);
        btnNext.setEnabled(position < currentWordList.size() - 1);

        btnPrevious.setAlpha(position > 0 ? 1.0f : 0.5f);
        btnNext.setAlpha(position < currentWordList.size() - 1 ? 1.0f : 0.5f);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);
    }

    private void showError(String message) {
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);

        tvError.setText(message);
    }

    private void showEmpty() {
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        tvProgress.setVisibility(View.VISIBLE);
    }
}

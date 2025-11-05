package com.example.flashcardnnn.fragment;

import android.os.Bundle;
import android.util.Log;
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

public class FlashcardFragment extends Fragment {

    private static final String TAG = "FlashcardFragment";

    private ViewPager2 viewPager;
    private FlashcardAdapter adapter;
    private TextView tvProgress;
    private MaterialButton btnPrevious, btnNext;

    private LinearLayout loadingView, errorView, emptyView;
    private Button btnRetry;

    private List<Word> currentWordList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupListeners();
        loadWords();
    }

    private void initViews(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tvProgress = view.findViewById(R.id.tvProgress);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);

        loadingView = view.findViewById(R.id.loadingView);
        errorView = view.findViewById(R.id.errorView);
        emptyView = view.findViewById(R.id.emptyView);
        btnRetry = view.findViewById(R.id.btnRetry);

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

        RetrofitClient.getApiService().getWords().enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Word> words = response.body();

                    if (words.isEmpty()) {
                        showEmpty();
                    } else {
                        currentWordList.clear();
                        currentWordList.addAll(words);

                        showContent();

                        adapter.notifyDataSetChanged();

                        // Set to first item
                        viewPager.post(() -> {
                            viewPager.setCurrentItem(0, false);
                            updateProgress(0);
                            updateNavigationButtons(0);
                        });

                        Log.d(TAG, "Loaded " + currentWordList.size() + " words");
                    }
                } else {
                    showError("Lỗi tải dữ liệu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                Log.e(TAG, "Error loading words", t);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void updateProgress(int position) {
        if (currentWordList == null || currentWordList.isEmpty()) {
            tvProgress.setText("0 / 0");
        } else {
            // Validate position
            if (position < 0) position = 0;
            if (position >= currentWordList.size()) position = currentWordList.size() - 1;

            tvProgress.setText(getString(R.string.card_progress,
                    position + 1, currentWordList.size()));
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

        TextView tvError = errorView.findViewById(R.id.tvError);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.shutdown();
        }
    }
}

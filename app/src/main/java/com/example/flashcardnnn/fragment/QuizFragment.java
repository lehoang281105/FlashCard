package com.example.flashcardnnn.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.flashcardnnn.R;
import com.example.flashcard_quiz.QuizActivity;

public class QuizFragment extends Fragment {

    private Button btn10Questions, btn15Questions, btn20Questions, btn30Questions;
    private TextView tvBestScore10, tvBestScore15, tvBestScore20, tvBestScore30;
    private CardView card10, card15, card20, card30;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "QuizPrefs";
    private static final String KEY_BEST_SCORE = "best_score_";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        initViews(view);
        loadBestScores();
        setupClickListeners();
        animateCards();
    }

    private void initViews(View view) {
        btn10Questions = view.findViewById(R.id.btn_10_questions);
        btn15Questions = view.findViewById(R.id.btn_15_questions);
        btn20Questions = view.findViewById(R.id.btn_20_questions);
        btn30Questions = view.findViewById(R.id.btn_30_questions);

        tvBestScore10 = view.findViewById(R.id.tv_best_score_10);
        tvBestScore15 = view.findViewById(R.id.tv_best_score_15);
        tvBestScore20 = view.findViewById(R.id.tv_best_score_20);
        tvBestScore30 = view.findViewById(R.id.tv_best_score_30);

        card10 = view.findViewById(R.id.card_10);
        card15 = view.findViewById(R.id.card_15);
        card20 = view.findViewById(R.id.card_20);
        card30 = view.findViewById(R.id.card_30);
    }

    private void loadBestScores() {
        int bestScore10 = sharedPreferences.getInt(KEY_BEST_SCORE + "10", 0);
        int bestScore15 = sharedPreferences.getInt(KEY_BEST_SCORE + "15", 0);
        int bestScore20 = sharedPreferences.getInt(KEY_BEST_SCORE + "20", 0);
        int bestScore30 = sharedPreferences.getInt(KEY_BEST_SCORE + "30", 0);

        tvBestScore10.setText("Kỷ lục: " + bestScore10 + "/10");
        tvBestScore15.setText("Kỷ lục: " + bestScore15 + "/15");
        tvBestScore20.setText("Kỷ lục: " + bestScore20 + "/20");
        tvBestScore30.setText("Kỷ lục: " + bestScore30 + "/30");
    }

    private void setupClickListeners() {
        btn10Questions.setOnClickListener(v -> {
            animateButton(v);
            startQuiz(10);
        });
        btn15Questions.setOnClickListener(v -> {
            animateButton(v);
            startQuiz(15);
        });
        btn20Questions.setOnClickListener(v -> {
            animateButton(v);
            startQuiz(20);
        });
        btn30Questions.setOnClickListener(v -> {
            animateButton(v);
            startQuiz(30);
        });
    }

    private void animateCards() {
        animateCardEntry(card10, 0);
        animateCardEntry(card15, 100);
        animateCardEntry(card20, 200);
        animateCardEntry(card30, 300);
    }

    private void animateCardEntry(View view, long delay) {
        view.setAlpha(0f);
        view.setTranslationY(100f);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(delay)
                .start();
    }

    private void animateButton(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 0.95f, 1.0f, 0.95f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(100);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(scaleAnimation);
    }

    private void startQuiz(int totalQuestions) {
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra("total_questions", totalQuestions);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBestScores();
    }
}

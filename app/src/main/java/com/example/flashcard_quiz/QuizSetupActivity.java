package com.example.flashcard_quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class QuizSetupActivity extends AppCompatActivity {

    private Button btn10Questions, btn15Questions, btn20Questions, btn30Questions;
    private TextView tvBestScore10, tvBestScore15, tvBestScore20, tvBestScore30;
    private CardView card10, card15, card20, card30;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "QuizPrefs";
    private static final String KEY_BEST_SCORE = "best_score_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_setup);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        initViews();
        loadBestScores();
        setupClickListeners();
        animateCards();
    }

    private void initViews() {
        btn10Questions = findViewById(R.id.btn_10_questions);
        btn15Questions = findViewById(R.id.btn_15_questions);
        btn20Questions = findViewById(R.id.btn_20_questions);
        btn30Questions = findViewById(R.id.btn_30_questions);

        tvBestScore10 = findViewById(R.id.tv_best_score_10);
        tvBestScore15 = findViewById(R.id.tv_best_score_15);
        tvBestScore20 = findViewById(R.id.tv_best_score_20);
        tvBestScore30 = findViewById(R.id.tv_best_score_30);

        card10 = findViewById(R.id.card_10);
        card15 = findViewById(R.id.card_15);
        card20 = findViewById(R.id.card_20);
        card30 = findViewById(R.id.card_30);
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
        // Animate cards entering from bottom
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
        Intent intent = new Intent(QuizSetupActivity.this, QuizActivity.class);
        intent.putExtra("total_questions", totalQuestions);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật điểm khi quay lại màn hình này
        loadBestScores();
    }
}

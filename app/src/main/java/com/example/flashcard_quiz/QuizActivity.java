package com.example.flashcard_quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.flashcardnnn.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvScore, tvBestScore, tvQuestion;
    private TextView tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4;
    private CardView cardAnswer1, cardAnswer2, cardAnswer3, cardAnswer4;
    private Button btnNext;
    private ImageView btnBack;
    private ProgressBar progressBarQuiz;

    private List<Word> wordList = new ArrayList<>();
    private List<Word> quizWords = new ArrayList<>();
    private ArrayList<Integer> wrongAnswerIndices = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int totalQuestions = 10;
    private String correctAnswer = "";
    private boolean hasAnswered = false;
    private boolean isRedoMode = false;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "QuizPrefs";
    private static final String KEY_BEST_SCORE = "best_score_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Xử lý nút Back theo cách mới
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Khi nhấn back trong quiz, quay về trang chủ MainActivity
                Intent intent = new Intent(QuizActivity.this, com.example.flashcardnnn.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        totalQuestions = getIntent().getIntExtra("total_questions", 10);
        isRedoMode = getIntent().getBooleanExtra("is_redo_mode", false);

        if (isRedoMode) {
            wrongAnswerIndices = getIntent().getIntegerArrayListExtra("wrong_indices");
            quizWords = (ArrayList<Word>) getIntent().getSerializableExtra("quiz_words");
        }

        initViews();
        loadBestScore();
        loadWordsFromAPI();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvScore = findViewById(R.id.tv_score);
        tvBestScore = findViewById(R.id.tv_best_score);
        tvQuestion = findViewById(R.id.tv_question);

        tvAnswer1 = findViewById(R.id.tv_answer1);
        tvAnswer2 = findViewById(R.id.tv_answer2);
        tvAnswer3 = findViewById(R.id.tv_answer3);
        tvAnswer4 = findViewById(R.id.tv_answer4);

        cardAnswer1 = findViewById(R.id.card_answer1);
        cardAnswer2 = findViewById(R.id.card_answer2);
        cardAnswer3 = findViewById(R.id.card_answer3);
        cardAnswer4 = findViewById(R.id.card_answer4);

        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);
        progressBarQuiz = findViewById(R.id.progress_bar_quiz);

        if (isRedoMode) {
            tvBestScore.setText("Làm lại câu sai");
            totalQuestions = wrongAnswerIndices.size();
        }

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Set click listeners cho từng card
        cardAnswer1.setOnClickListener(v -> checkAnswer(tvAnswer1, cardAnswer1));
        cardAnswer2.setOnClickListener(v -> checkAnswer(tvAnswer2, cardAnswer2));
        cardAnswer3.setOnClickListener(v -> checkAnswer(tvAnswer3, cardAnswer3));
        cardAnswer4.setOnClickListener(v -> checkAnswer(tvAnswer4, cardAnswer4));

        btnNext.setOnClickListener(v -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < totalQuestions) {
                displayQuestion();
            } else {
                showResult();
            }
        });
    }

    private void loadBestScore() {
        if (!isRedoMode) {
            int bestScore = sharedPreferences.getInt(KEY_BEST_SCORE + totalQuestions, 0);
            tvBestScore.setText("Kỷ lục: " + bestScore + "/" + totalQuestions);
        }
    }

    private void loadWordsFromAPI() {
        if (isRedoMode) {
            // Nếu là chế độ redo, chỉ load wordList để có đáp án sai
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<List<Word>> call = apiService.getWords();

            call.enqueue(new Callback<List<Word>>() {
                @Override
                public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        wordList = response.body();
                        prepareRedoQuiz();
                        displayQuestion();
                    }
                }

                @Override
                public void onFailure(Call<List<Word>> call, Throwable t) {
                    Toast.makeText(QuizActivity.this,
                            "Lỗi kết nối: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        } else {
            // Chế độ bình thường
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<List<Word>> call = apiService.getWords();

            call.enqueue(new Callback<List<Word>>() {
                @Override
                public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        wordList = response.body();
                        if (wordList.size() >= totalQuestions) {
                            prepareQuiz();
                            displayQuestion();
                        } else {
                            Toast.makeText(QuizActivity.this,
                                    "Không đủ từ vựng! Cần ít nhất " + totalQuestions + " từ.",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Word>> call, Throwable t) {
                    Toast.makeText(QuizActivity.this,
                            "Lỗi kết nối: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    }

    private void prepareQuiz() {
        Collections.shuffle(wordList);
        quizWords = new ArrayList<>(wordList.subList(0, Math.min(totalQuestions, wordList.size())));
    }

    private void prepareRedoQuiz() {
        // Tạo danh sách chỉ chứa các câu sai
        List<Word> redoWords = new ArrayList<>();
        for (int index : wrongAnswerIndices) {
            redoWords.add(quizWords.get(index));
        }
        quizWords = redoWords;
        totalQuestions = quizWords.size();
    }

    private void displayQuestion() {
        hasAnswered = false;
        btnNext.setVisibility(View.GONE);

        Word currentWord = quizWords.get(currentQuestionIndex);
        correctAnswer = currentWord.getVietnamese();

        // Update progress bar
        int progress = (int) (((currentQuestionIndex + 1) * 100.0) / totalQuestions);
        progressBarQuiz.setProgress(progress);

        tvQuestionNumber.setText((currentQuestionIndex + 1) + "/" + totalQuestions);
        tvScore.setText("Điểm: " + score);
        tvQuestion.setText(currentWord.getEnglish());

        List<String> answers = generateAnswers(currentWord);
        tvAnswer1.setText(answers.get(0));
        tvAnswer2.setText(answers.get(1));
        tvAnswer3.setText(answers.get(2));
        tvAnswer4.setText(answers.get(3));

        // Reset màu sắc các card
        resetCardColors();

        // Enable tất cả các card
        cardAnswer1.setEnabled(true);
        cardAnswer2.setEnabled(true);
        cardAnswer3.setEnabled(true);
        cardAnswer4.setEnabled(true);
    }

    private void resetCardColors() {
        cardAnswer1.setCardBackgroundColor(Color.parseColor("#F8F4FB"));
        cardAnswer2.setCardBackgroundColor(Color.parseColor("#F8F4FB"));
        cardAnswer3.setCardBackgroundColor(Color.parseColor("#F8F4FB"));
        cardAnswer4.setCardBackgroundColor(Color.parseColor("#F8F4FB"));

        tvAnswer1.setTextColor(Color.parseColor("#000000"));
        tvAnswer2.setTextColor(Color.parseColor("#000000"));
        tvAnswer3.setTextColor(Color.parseColor("#000000"));
        tvAnswer4.setTextColor(Color.parseColor("#000000"));
    }

    private List<String> generateAnswers(Word correctWord) {
        List<String> answers = new ArrayList<>();
        answers.add(correctWord.getVietnamese());

        List<Word> wrongWords = new ArrayList<>(wordList);
        wrongWords.remove(correctWord);
        Collections.shuffle(wrongWords);

        for (int i = 0; i < 3 && i < wrongWords.size(); i++) {
            answers.add(wrongWords.get(i).getVietnamese());
        }

        Collections.shuffle(answers);
        return answers;
    }

    private void checkAnswer(TextView selectedAnswer, CardView selectedCard) {
        if (hasAnswered) return;

        hasAnswered = true;
        String userAnswer = selectedAnswer.getText().toString();

        // Disable tất cả các card sau khi chọn
        cardAnswer1.setEnabled(false);
        cardAnswer2.setEnabled(false);
        cardAnswer3.setEnabled(false);
        cardAnswer4.setEnabled(false);

        if (userAnswer.equals(correctAnswer)) {
            // Đáp án đúng - màu xanh lá
            selectedCard.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            selectedAnswer.setTextColor(Color.WHITE);
            score++;
            tvScore.setText("Điểm: " + score);
        } else {
            // Đáp án sai - màu đỏ
            selectedCard.setCardBackgroundColor(Color.parseColor("#EF5350"));
            selectedAnswer.setTextColor(Color.WHITE);

            // Lưu index của câu sai (chỉ trong chế độ bình thường)
            if (!isRedoMode) {
                wrongAnswerIndices.add(currentQuestionIndex);
            }

            // Highlight đáp án đúng màu xanh
            highlightCorrectAnswer();
        }

        btnNext.setVisibility(View.VISIBLE);
    }

    private void highlightCorrectAnswer() {
        if (tvAnswer1.getText().toString().equals(correctAnswer)) {
            cardAnswer1.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            tvAnswer1.setTextColor(Color.WHITE);
        } else if (tvAnswer2.getText().toString().equals(correctAnswer)) {
            cardAnswer2.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            tvAnswer2.setTextColor(Color.WHITE);
        } else if (tvAnswer3.getText().toString().equals(correctAnswer)) {
            cardAnswer3.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            tvAnswer3.setTextColor(Color.WHITE);
        } else if (tvAnswer4.getText().toString().equals(correctAnswer)) {
            cardAnswer4.setCardBackgroundColor(Color.parseColor("#4CAF50"));
            tvAnswer4.setTextColor(Color.WHITE);
        }
    }

    private void showResult() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("total", totalQuestions);
        intent.putExtra("is_redo_mode", isRedoMode);

        if (!isRedoMode) {
            // Chỉ cập nhật best score trong chế độ bình thường
            int bestScore = sharedPreferences.getInt(KEY_BEST_SCORE + totalQuestions, 0);
            if (score > bestScore) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_BEST_SCORE + totalQuestions, score);
                editor.apply();
            }
            intent.putExtra("best_score", Math.max(score, bestScore));

            // Gửi danh sách câu sai và quiz words
            if (!wrongAnswerIndices.isEmpty()) {
                intent.putIntegerArrayListExtra("wrong_indices", wrongAnswerIndices);
                intent.putExtra("quiz_words", (ArrayList<Word>) quizWords);
            }
        }

        startActivity(intent);
        finish();
    }
}
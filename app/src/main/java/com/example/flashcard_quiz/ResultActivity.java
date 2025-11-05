package com.example.flashcard_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashcardnnn.R;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResult, tvScore, tvMessage, tvGrade;
    private Button btnRetry, btnHome, btnRedoWrong;
    private ArrayList<Integer> wrongAnswerIndices;
    private ArrayList<Word> quizWords;
    private boolean isRedoMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        isRedoMode = getIntent().getBooleanExtra("is_redo_mode", false);
        wrongAnswerIndices = getIntent().getIntegerArrayListExtra("wrong_indices");
        quizWords = (ArrayList<Word>) getIntent().getSerializableExtra("quiz_words");

        initViews();
        displayResult();
    }

    private void initViews() {
        tvResult = findViewById(R.id.tv_result);
        tvScore = findViewById(R.id.tv_score);
        tvMessage = findViewById(R.id.tv_message);
        tvGrade = findViewById(R.id.tv_grade);
        btnRetry = findViewById(R.id.btn_retry);
        btnHome = findViewById(R.id.btn_home);
        btnRedoWrong = findViewById(R.id.btn_redo_wrong);

        btnRetry.setOnClickListener(v -> retryQuiz());
        btnHome.setOnClickListener(v -> goHome());
        btnRedoWrong.setOnClickListener(v -> redoWrongAnswers());

        // Ẩn nút Retry (không cần thiết nữa)
        btnRetry.setVisibility(View.GONE);

        // Hiển thị nút Redo Wrong Answers nếu có câu sai và không phải chế độ redo
        if (!isRedoMode && wrongAnswerIndices != null && !wrongAnswerIndices.isEmpty()) {
            btnRedoWrong.setVisibility(View.VISIBLE);
        } else {
            btnRedoWrong.setVisibility(View.GONE);
        }

        // Nếu là chế độ redo, đổi text button Home
        if (isRedoMode) {
            btnHome.setText("Back to Quiz Setup");
        }
    }

    private void displayResult() {
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);

        // Hiển thị điểm số bình thường
        tvScore.setText(score + "/" + total);

        // Tính điểm chữ cái theo bảng và hiển thị ở giữa vòng tròn
        String grade = calculateGrade(score, total);
        tvGrade.setText(grade);

        // Hiển thị thông điệp dựa trên kết quả
        if (grade.equals("A+")) {
            tvResult.setText("Congratulations!");
            tvMessage.setText("Perfect! You have done excellent!");
        } else if (grade.equals("A") || grade.equals("B")) {
            tvResult.setText("Congratulations!");
            tvMessage.setText("Great job! You have done well!");
        } else if (grade.equals("C")) {
            tvResult.setText("Good Job!");
            tvMessage.setText("Nice work! Keep it up!");
        } else if (grade.equals("D")) {
            tvResult.setText("Keep Trying!");
            tvMessage.setText("Not bad! Practice makes perfect!");
        } else {
            tvResult.setText("Keep Learning!");
            tvMessage.setText("Don't give up! Keep studying!");
        }
    }

    private String calculateGrade(int score, int total) {
        // Tính điểm chữ cái theo bảng
        if (total == 10) {
            if (score >= 10) return "A+";
            if (score >= 9) return "A";
            if (score >= 8) return "B";
            if (score >= 7) return "C";
            if (score >= 5) return "D";
            return "F";
        } else if (total == 15) {
            if (score >= 15) return "A+";
            if (score >= 13) return "A";
            if (score >= 12) return "B";
            if (score >= 10) return "C";
            if (score >= 8) return "D";
            return "F";
        } else if (total == 20) {
            if (score >= 20) return "A+";
            if (score >= 17) return "A";
            if (score >= 15) return "B";
            if (score >= 13) return "C";
            if (score >= 10) return "D";
            return "F";
        } else if (total == 30) {
            if (score >= 29) return "A+";
            if (score >= 26) return "A";
            if (score >= 23) return "B";
            if (score >= 19) return "C";
            if (score >= 15) return "D";
            return "F";
        }
        return "N/A";
    }

    private void retryQuiz() {
        finish();
    }

    private void redoWrongAnswers() {
        Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
        intent.putExtra("is_redo_mode", true);
        intent.putIntegerArrayListExtra("wrong_indices", wrongAnswerIndices);
        intent.putExtra("quiz_words", quizWords);
        startActivity(intent);
        finish();
    }

    private void goHome() {
        if (isRedoMode) {
            // Quay về QuizSetupActivity
            Intent intent = new Intent(ResultActivity.this, QuizSetupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            // Quay về MainActivity
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goHome();
    }
}

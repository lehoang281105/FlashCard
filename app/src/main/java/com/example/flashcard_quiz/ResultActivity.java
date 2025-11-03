package com.example.flashcard_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResult, tvScore, tvMessage;
    private Button btnRetry, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViews();
        displayResult();
    }

    private void initViews() {
        tvResult = findViewById(R.id.tv_result);
        tvScore = findViewById(R.id.tv_score);
        tvMessage = findViewById(R.id.tv_message);
        btnRetry = findViewById(R.id.btn_retry);
        btnHome = findViewById(R.id.btn_home);

        btnRetry.setOnClickListener(v -> retryQuiz());
        btnHome.setOnClickListener(v -> goHome());
    }

    private void displayResult() {
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);

        tvScore.setText(score + "/" + total);

        // Tính phần trăm
        double percentage = (score * 100.0) / total;

        // Hiển thị thông điệp dựa trên kết quả
        if (percentage == 100) {
            tvResult.setText("🎉 XUẤT SẮC!");
            tvMessage.setText("Hoàn hảo! Bạn đã trả lời đúng tất cả!");
        } else if (percentage >= 80) {
            tvResult.setText("😊 RẤT TỐT!");
            tvMessage.setText("Tuyệt vời! Bạn làm rất tốt!");
        } else if (percentage >= 60) {
            tvResult.setText("👍 KHÁ!");
            tvMessage.setText("Khá ổn! Hãy cố gắng hơn nữa!");
        } else if (percentage >= 40) {
            tvResult.setText("💪 CÓ TIẾN BỘ!");
            tvMessage.setText("Không tệ! Tiếp tục luyện tập nhé!");
        } else {
            tvResult.setText("📚 CẦN CỐ GẮNG!");
            tvMessage.setText("Đừng nản lòng! Hãy học thêm và thử lại!");
        }
    }

    private void retryQuiz() {
        finish();
    }

    private void goHome() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goHome();
    }
}

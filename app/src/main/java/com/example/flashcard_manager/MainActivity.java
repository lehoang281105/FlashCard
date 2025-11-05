package com.example.flashcard_manager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnFlashcard, btnQuiz, btnManageVocabulary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnFlashcard = findViewById(R.id.btnFlashcard);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnManageVocabulary = findViewById(R.id.btnManageVocabulary);
    }

    private void setupListeners() {
        btnFlashcard.setOnClickListener(v -> {
            // TODO: Người 1 sẽ implement
            // Intent intent = new Intent(this, FlashcardActivity.class);
            // startActivity(intent);
        });

        btnQuiz.setOnClickListener(v -> {
            // TODO: Người 2 sẽ implement
            // Intent intent = new Intent(this, QuizActivity.class);
            // startActivity(intent);
        });

        btnManageVocabulary.setOnClickListener(v -> {
            // Mở màn hình quản lý theo Topics
            Intent intent = new Intent(this, VocabularyManagementActivity.class);
            startActivity(intent);
        });
    }
}
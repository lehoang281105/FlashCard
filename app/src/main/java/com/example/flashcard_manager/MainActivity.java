package com.example.flashcard_manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.flashcardnnn.R;

public class MainActivity extends AppCompatActivity {

    private LinearLayout navFlashcard, navQuiz, navVocabulary;

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
        navFlashcard = findViewById(R.id.navFlashcard);
        navQuiz = findViewById(R.id.navQuiz);
        navVocabulary = findViewById(R.id.navVocabulary);
    }

    private void setupListeners() {
        navFlashcard.setOnClickListener(v -> {
            // TODO: Người 1 sẽ implement
            // Intent intent = new Intent(this, FlashcardActivity.class);
            // startActivity(intent);
        });

        navQuiz.setOnClickListener(v -> {
            // TODO: Người 2 sẽ implement
            // Intent intent = new Intent(this, QuizActivity.class);
            // startActivity(intent);
        });

        navVocabulary.setOnClickListener(v -> {
            // Mở màn hình quản lý theo Topics
            Intent intent = new Intent(this, VocabularyManagementActivity.class);
            startActivity(intent);
        });
    }
}
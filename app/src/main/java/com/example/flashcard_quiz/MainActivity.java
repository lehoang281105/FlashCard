package com.example.flashcard_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        setupClickListeners();
    }

    private void initViews() {
        navFlashcard = findViewById(R.id.navFlashcard);
        navQuiz = findViewById(R.id.navQuiz);
        navVocabulary = findViewById(R.id.navVocabulary);
    }

    private void setupClickListeners() {
        navFlashcard.setOnClickListener(v -> {
            // TODO: Người 1 sẽ implement
            Toast.makeText(this, "Chức năng Flashcard - Người 1 sẽ làm", Toast.LENGTH_SHORT).show();
        });

        navQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizSetupActivity.class);
            startActivity(intent);
        });

        navVocabulary.setOnClickListener(v -> {
            // TODO: Người 3 sẽ implement
            Toast.makeText(this, "Chức năng Quản lý - Người 3 sẽ làm", Toast.LENGTH_SHORT).show();
        });
    }
}
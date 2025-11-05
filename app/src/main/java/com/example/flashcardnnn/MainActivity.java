package com.example.flashcardnnn;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.flashcardnnn.fragment.FlashcardFragment;
import com.example.flashcardnnn.fragment.QuizFragment;
import com.example.flashcardnnn.fragment.VocabularyFragment;

public class MainActivity extends AppCompatActivity {

    private LinearLayout navFlashcard, navQuiz, navVocabulary;
    private ImageView iconFlashcard, iconQuiz, iconVocabulary;
    private TextView labelFlashcard, labelQuiz, labelVocabulary;
    
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupListeners();
        
        // Load Flashcard fragment by default
        if (savedInstanceState == null) {
            selectTab(0);
        }
    }

    private void initViews() {
        navFlashcard = findViewById(R.id.navFlashcard);
        navQuiz = findViewById(R.id.navQuiz);
        navVocabulary = findViewById(R.id.navVocabulary);
        
        iconFlashcard = findViewById(R.id.iconFlashcard);
        iconQuiz = findViewById(R.id.iconQuiz);
        iconVocabulary = findViewById(R.id.iconVocabulary);
        
        labelFlashcard = findViewById(R.id.labelFlashcard);
        labelQuiz = findViewById(R.id.labelQuiz);
        labelVocabulary = findViewById(R.id.labelVocabulary);
    }

    private void setupListeners() {
        navFlashcard.setOnClickListener(v -> selectTab(0));
        navQuiz.setOnClickListener(v -> selectTab(1));
        navVocabulary.setOnClickListener(v -> selectTab(2));
    }

    private void selectTab(int tabIndex) {
        // Reset all tabs
        resetAllTabs();
        
        Fragment fragment = null;
        
        switch (tabIndex) {
            case 0:
                // Flashcard tab
                navFlashcard.setSelected(true);
                iconFlashcard.setSelected(true);
                labelFlashcard.setSelected(true);
                fragment = new FlashcardFragment();
                break;
                
            case 1:
                // Quiz tab
                navQuiz.setSelected(true);
                iconQuiz.setSelected(true);
                labelQuiz.setSelected(true);
                fragment = new QuizFragment();
                break;
                
            case 2:
                // Vocabulary tab
                navVocabulary.setSelected(true);
                iconVocabulary.setSelected(true);
                labelVocabulary.setSelected(true);
                fragment = new VocabularyFragment();
                break;
        }
        
        // Load fragment
        if (fragment != null) {
            loadFragment(fragment);
        }
    }

    private void resetAllTabs() {
        navFlashcard.setSelected(false);
        navQuiz.setSelected(false);
        navVocabulary.setSelected(false);
        
        iconFlashcard.setSelected(false);
        iconQuiz.setSelected(false);
        iconVocabulary.setSelected(false);
        
        labelFlashcard.setSelected(false);
        labelQuiz.setSelected(false);
        labelVocabulary.setSelected(false);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        if (currentFragment != null) {
            transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
        }
        
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
        
        currentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        // If not on Flashcard tab, go back to Flashcard
        if (currentFragment instanceof FlashcardFragment) {
            super.onBackPressed();
        } else {
            selectTab(0);
        }
    }
}

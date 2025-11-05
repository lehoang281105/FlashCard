package com.example.flashcardnnn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flashcardnnn.R;
import com.example.flashcard_manager.VocabularyManagementActivity;

public class VocabularyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tự động mở VocabularyManagementActivity khi fragment được tạo
        Intent intent = new Intent(getActivity(), VocabularyManagementActivity.class);
        startActivity(intent);
    }
}

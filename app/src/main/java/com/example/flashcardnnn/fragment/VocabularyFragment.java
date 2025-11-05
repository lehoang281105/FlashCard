package com.example.flashcardnnn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flashcardnnn.R;

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

        // TODO: Người 3 sẽ code phần Quản lý từ vựng ở đây
        // Có thể sử dụng RetrofitClient.getApiService() để:
        // - Thêm từ mới: addWord(word)
        // - Xóa từ: deleteWord(id)
        // - Cập nhật từ: updateWord(id, word)
        // - Lấy danh sách từ: getWords()
    }
}

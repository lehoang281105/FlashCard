package com.example.flashcard_manager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardnnn.R;
import com.example.flashcard_manager.models.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<Word> words = new ArrayList<>();
    private OnWordClickListener listener;

    public interface OnWordClickListener {
        void onEditClick(Word word);
        void onDeleteClick(Word word);
    }

    public WordAdapter(OnWordClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = words.get(position);
        holder.bind(word);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWords(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    public void removeWord(int position) {
        words.remove(position);
        notifyItemRemoved(position);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWord;
        private TextView tvMeaning;
        private TextView tvPronunciation;
        private TextView tvExample;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvPronunciation = itemView.findViewById(R.id.tvPronunciation);
            tvExample = itemView.findViewById(R.id.tvExample);
            btnEdit = itemView.findViewById(R.id.btnEditWord);
            btnDelete = itemView.findViewById(R.id.btnDeleteWord);

            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(words.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(words.get(position));
                }
            });
        }

        public void bind(Word word) {
            // Xử lý null values
            tvWord.setText(word.getWord() != null && !word.getWord().isEmpty()
                    ? word.getWord()
                    : "(Chưa có từ)");

            tvMeaning.setText(word.getMeaning() != null && !word.getMeaning().isEmpty()
                    ? word.getMeaning()
                    : "(Chưa có nghĩa)");

            if (word.getPronunciation() != null && !word.getPronunciation().isEmpty()) {
                tvPronunciation.setText("/" + word.getPronunciation() + "/");
                tvPronunciation.setVisibility(View.VISIBLE);
            } else {
                tvPronunciation.setVisibility(View.GONE);
            }

            if (word.getExample() != null && !word.getExample().isEmpty()) {
                tvExample.setText("VD: " + word.getExample());
                tvExample.setVisibility(View.VISIBLE);
            } else {
                tvExample.setVisibility(View.GONE);
            }
        }
    }
}

package com.example.flashcardnnn.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardnnn.R;
import com.example.flashcardnnn.model.Word;

import java.util.List;
import java.util.Locale;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Word> wordList;
    private TextToSpeech textToSpeech;
    private boolean isTTSInitialized = false;

    public FlashcardAdapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);

        // Initialize TextToSpeech
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(parent.getContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    isTTSInitialized = (result != TextToSpeech.LANG_MISSING_DATA
                            && result != TextToSpeech.LANG_NOT_SUPPORTED);
                }
            });
        }

        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.bind(word);
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void updateData(List<Word> newWordList) {
        this.wordList = newWordList;
        notifyDataSetChanged();
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    class FlashcardViewHolder extends RecyclerView.ViewHolder {
        private CardView cardFront, cardBack;
        private TextView tvEnglish, tvVietnamese, tvWordType, tvWordTypeBack, tvExample;
        private ImageView btnSpeak;
        private boolean isShowingFront = true;
        private Word currentWord;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);

            cardFront = itemView.findViewById(R.id.cardFront);
            cardBack = itemView.findViewById(R.id.cardBack);
            tvEnglish = itemView.findViewById(R.id.tvEnglish);
            tvVietnamese = itemView.findViewById(R.id.tvVietnamese);
            tvWordType = itemView.findViewById(R.id.tvWordType);
            tvWordTypeBack = itemView.findViewById(R.id.tvWordTypeBack);
            tvExample = itemView.findViewById(R.id.tvExample);
            btnSpeak = itemView.findViewById(R.id.btnSpeak);

            // Set up flip animation on card click
            View.OnClickListener flipListener = v -> flipCard();
            cardFront.setOnClickListener(flipListener);
            cardBack.setOnClickListener(flipListener);

            // Set up speak button
            btnSpeak.setOnClickListener(v -> speakWord());
        }

        public void bind(Word word) {
            this.currentWord = word;

            // Reset to front side
            isShowingFront = true;
            cardFront.setVisibility(View.VISIBLE);
            cardBack.setVisibility(View.GONE);
            cardFront.setRotationY(0);
            cardBack.setRotationY(0);

            // Set data
            tvEnglish.setText(word.getEnglish());
            tvVietnamese.setText(word.getVietnamese());
            tvExample.setText(word.getExample() != null ? word.getExample() : "");

            String type = word.getType() != null ? word.getType() : "word";
            tvWordType.setText(type);
            tvWordTypeBack.setText(translateType(type));
        }

        private void flipCard() {
            AnimatorSet setOut;
            AnimatorSet setIn;

            if (isShowingFront) {
                // Flip from front to back
                setOut = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(),
                        R.animator.card_flip_out);
                setIn = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(),
                        R.animator.card_flip_in);

                setOut.setTarget(cardFront);
                setIn.setTarget(cardBack);

                setOut.start();
                setIn.start();

                cardFront.setVisibility(View.GONE);
                cardBack.setVisibility(View.VISIBLE);
            } else {
                // Flip from back to front
                setOut = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(),
                        R.animator.card_flip_out);
                setIn = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(),
                        R.animator.card_flip_in);

                setOut.setTarget(cardBack);
                setIn.setTarget(cardFront);

                setOut.start();
                setIn.start();

                cardBack.setVisibility(View.GONE);
                cardFront.setVisibility(View.VISIBLE);
            }

            isShowingFront = !isShowingFront;
        }

        private void speakWord() {
            if (currentWord != null && isTTSInitialized) {
                textToSpeech.speak(currentWord.getEnglish(), TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(itemView.getContext(),
                        "Text-to-Speech chưa sẵn sàng", Toast.LENGTH_SHORT).show();
            }
        }

        private String translateType(String englishType) {
            switch (englishType.toLowerCase()) {
                case "noun":
                    return "danh từ";
                case "verb":
                    return "động từ";
                case "adjective":
                    return "tính từ";
                case "adverb":
                    return "trạng từ";
                case "pronoun":
                    return "đại từ";
                case "preposition":
                    return "giới từ";
                case "conjunction":
                    return "liên từ";
                case "interjection":
                    return "thán từ";
                default:
                    return "từ";
            }
        }
    }
}

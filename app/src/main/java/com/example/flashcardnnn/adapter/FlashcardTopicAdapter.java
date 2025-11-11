package com.example.flashcardnnn.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardnnn.R;
import com.example.flashcard_manager.models.Topic;

import java.util.List;
import java.util.Random;

public class FlashcardTopicAdapter extends RecyclerView.Adapter<FlashcardTopicAdapter.ViewHolder> {

    private List<Topic> topicList;
    private OnTopicClickListener listener;
    private static final String[] GRADIENT_COLORS = {
            "#2C5F2D", // Dark Green
            "#1A535C", // Dark Teal
            "#3F51B5", // Indigo
            "#5E35B1", // Deep Purple
            "#D84315", // Deep Orange
            "#6A1B9A", // Purple
            "#00695C", // Teal
            "#424242"  // Dark Gray
    };

    public interface OnTopicClickListener {
        void onTopicClick(Topic topic);
    }

    public FlashcardTopicAdapter(List<Topic> topicList, OnTopicClickListener listener) {
        this.topicList = topicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        holder.bind(topic);
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvTopicName;
        private TextView tvTopicDescription;
        private TextView tvWordCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvTopicName = itemView.findViewById(R.id.tvTopicName);
            tvTopicDescription = itemView.findViewById(R.id.tvTopicDescription);
            tvWordCount = itemView.findViewById(R.id.tvWordCount);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTopicClick(topicList.get(position));
                }
            });
        }

        public void bind(Topic topic) {
            tvTopicName.setText(topic.getName());

            // Set description
            String description = topic.getDescription();
            if (description != null && !description.isEmpty()) {
                tvTopicDescription.setText(description);
                tvTopicDescription.setVisibility(View.VISIBLE);
            } else {
                tvTopicDescription.setVisibility(View.GONE);
            }

            // Format word count
            int count = topic.getWordCount();
            String phraseText = count == 1 ? "phrase" : "phrases";
            tvWordCount.setText(count + " " + phraseText);

            // Set darker background color
            String colorHex = GRADIENT_COLORS[getAdapterPosition() % GRADIENT_COLORS.length];

            // Use the color as card background
            try {
                cardView.setCardBackgroundColor(Color.parseColor(colorHex));
            } catch (Exception e) {
                cardView.setCardBackgroundColor(Color.parseColor("#3F51B5"));
            }
        }
    }

    public void updateTopics(List<Topic> newTopicList) {
        this.topicList = newTopicList;
        notifyDataSetChanged();
    }
}

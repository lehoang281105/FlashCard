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
            "#667eea,#764ba2", // Purple gradient
            "#f093fb,#f5576c", // Pink gradient
            "#4facfe,#00f2fe", // Blue gradient
            "#43e97b,#38f9d7", // Green gradient
            "#fa709a,#fee140", // Orange-Pink gradient
            "#30cfd0,#330867", // Blue-Purple gradient
            "#a8edea,#fed6e3", // Light gradient
            "#ff9a9e,#fecfef"  // Pink gradient
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
        private TextView tvWordCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvTopicName = itemView.findViewById(R.id.tvTopicName);
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

            // Format word count
            int count = topic.getWordCount();
            String phraseText = count == 1 ? "phrase" : "phrases";
            tvWordCount.setText(count + " " + phraseText);

            // Set gradient background color
            String gradient = GRADIENT_COLORS[getAdapterPosition() % GRADIENT_COLORS.length];
            String[] colors = gradient.split(",");

            // Use the first color as card background
            try {
                cardView.setCardBackgroundColor(Color.parseColor(colors[0]));
            } catch (Exception e) {
                cardView.setCardBackgroundColor(Color.parseColor("#667eea"));
            }
        }
    }

    public void updateTopics(List<Topic> newTopicList) {
        this.topicList = newTopicList;
        notifyDataSetChanged();
    }
}

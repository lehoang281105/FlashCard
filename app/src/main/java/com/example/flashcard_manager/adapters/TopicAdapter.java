package com.example.flashcard_manager.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard_manager.R;
import com.example.flashcard_manager.models.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topics = new ArrayList<>();
    private final OnTopicClickListener listener;

    // Map emoji icons for each topic
    private static final Map<String, String> TOPIC_ICONS = new HashMap<>();
    static {
        TOPIC_ICONS.put("animal", "🐾");
        TOPIC_ICONS.put("food", "🍔");
        TOPIC_ICONS.put("job", "💼");
        TOPIC_ICONS.put("emotion", "😊");
        TOPIC_ICONS.put("school", "🎓");
        TOPIC_ICONS.put("technology", "💻");
        TOPIC_ICONS.put("transport", "🚗");
        TOPIC_ICONS.put("home", "🏠");
        TOPIC_ICONS.put("nature", "🌳");
        TOPIC_ICONS.put("sport", "⚽");
        TOPIC_ICONS.put("hobby", "🎨");
        TOPIC_ICONS.put("weather", "🌤️");
        TOPIC_ICONS.put("color", "🎨");
        TOPIC_ICONS.put("people", "👥");
    }

    // Map colors for each topic
    private static final Map<String, String> TOPIC_COLORS = new HashMap<>();
    static {
        TOPIC_COLORS.put("animal", "#FF6B9D");
        TOPIC_COLORS.put("food", "#FEC163");
        TOPIC_COLORS.put("job", "#96E6B3");
        TOPIC_COLORS.put("emotion", "#8EC5FC");
        TOPIC_COLORS.put("school", "#C5A3FF");
        TOPIC_COLORS.put("technology", "#FFB6C1");
        TOPIC_COLORS.put("transport", "#FFDAB9");
        TOPIC_COLORS.put("home", "#B0E57C");
        TOPIC_COLORS.put("nature", "#87CEEB");
        TOPIC_COLORS.put("sport", "#FFA07A");
        TOPIC_COLORS.put("hobby", "#DDA0DD");
        TOPIC_COLORS.put("weather", "#ADD8E6");
        TOPIC_COLORS.put("color", "#FFB347");
        TOPIC_COLORS.put("people", "#F0E68C");
    }

    public interface OnTopicClickListener {
        void onTopicClick(Topic topic);
        void onEditClick(Topic topic);
        void onDeleteClick(Topic topic);
    }

    public TopicAdapter(OnTopicClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.bind(topic);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTopicName;
        private final TextView tvTopicDescription;
        private final TextView tvWordCount;
        private final TextView tvTopicIcon;
        private final CardView cvIconBackground;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicName = itemView.findViewById(R.id.tvTopicName);
            tvTopicDescription = itemView.findViewById(R.id.tvTopicDescription);
            tvWordCount = itemView.findViewById(R.id.tvWordCount);
            tvTopicIcon = itemView.findViewById(R.id.tvTopicIcon);
            cvIconBackground = itemView.findViewById(R.id.cvIconBackground);
            ImageButton btnEdit = itemView.findViewById(R.id.btnEditTopic);
            ImageButton btnDelete = itemView.findViewById(R.id.btnDeleteTopic);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTopicClick(topics.get(position));
                }
            });

            btnEdit.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(topics.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(topics.get(position));
                }
            });
        }

        public void bind(Topic topic) {
            if (topic != null) {
                tvTopicName.setText(topic.getName() != null ? topic.getName() : "Không có tên");
                tvTopicDescription.setText(topic.getDescription() != null ? topic.getDescription() : "");
                String wordCountText = topic.getWordCount() + " từ";
                tvWordCount.setText(wordCountText);

                // Set icon based on topic ID
                String topicId = topic.getId();
                String icon = TOPIC_ICONS.getOrDefault(topicId, "📚");
                tvTopicIcon.setText(icon);

                // Set background color for icon
                String color = TOPIC_COLORS.getOrDefault(topicId, "#6366F1");
                try {
                    cvIconBackground.setCardBackgroundColor(Color.parseColor(color));
                } catch (Exception e) {
                    cvIconBackground.setCardBackgroundColor(Color.parseColor("#6366F1"));
                }
            }
        }
    }
}


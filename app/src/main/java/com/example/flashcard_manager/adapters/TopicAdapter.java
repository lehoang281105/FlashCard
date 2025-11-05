package com.example.flashcard_manager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcard_manager.R;
import com.example.flashcard_manager.models.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topics = new ArrayList<>();
    private OnTopicClickListener listener;

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

    public void removeTopic(int position) {
        topics.remove(position);
        notifyItemRemoved(position);
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTopicName;
        private TextView tvTopicDescription;
        private TextView tvWordCount;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicName = itemView.findViewById(R.id.tvTopicName);
            tvTopicDescription = itemView.findViewById(R.id.tvTopicDescription);
            tvWordCount = itemView.findViewById(R.id.tvWordCount);
            btnEdit = itemView.findViewById(R.id.btnEditTopic);
            btnDelete = itemView.findViewById(R.id.btnDeleteTopic);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTopicClick(topics.get(position));
                }
            });

            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(topics.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(topics.get(position));
                }
            });
        }

        public void bind(Topic topic) {
            if (topic != null) {
                tvTopicName.setText(topic.getName() != null ? topic.getName() : "Không có tên");
                tvTopicDescription.setText(topic.getDescription() != null ? topic.getDescription() : "");
                tvWordCount.setText(topic.getWordCount() + " từ");
            }
        }
    }
}

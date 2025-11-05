package com.example.flashcard_manager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flashcard_manager.models.Topic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddEditTopicActivity extends AppCompatActivity {

    private EditText etTopicName, etTopicDescription;
    private Button btnSave, btnCancel;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TopicPrefs";
    private static final String TOPICS_KEY = "topics";
    private String topicId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_topic);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initViews();
        loadTopicData();
        setupListeners();
    }

    private void initViews() {
        etTopicName = findViewById(R.id.etTopicName);
        etTopicDescription = findViewById(R.id.etTopicDescription);
        btnSave = findViewById(R.id.btnSaveTopic);
        btnCancel = findViewById(R.id.btnCancelTopic);

        topicId = getIntent().getStringExtra("topicId");
        if (topicId != null) {
            isEditMode = true;
            setTitle("Sửa Topic");
        } else {
            setTitle("Thêm Topic Mới");
        }
    }

    private void loadTopicData() {
        if (isEditMode) {
            String name = getIntent().getStringExtra("topicName");
            String description = getIntent().getStringExtra("topicDescription");
            etTopicName.setText(name);
            etTopicDescription.setText(description);
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveTopic());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveTopic() {
        String name = etTopicName.getText().toString().trim();
        String description = etTopicDescription.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên topic", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Topic> topics = loadTopicsFromPrefs();

        if (isEditMode) {
            // Cập nhật topic
            for (Topic topic : topics) {
                if (topic.getId().equals(topicId)) {
                    topic.setName(name);
                    topic.setDescription(description);
                    break;
                }
            }
            Toast.makeText(this, "Đã cập nhật topic", Toast.LENGTH_SHORT).show();
        } else {
            // Thêm topic mới
            Topic newTopic = new Topic();
            newTopic.setId(UUID.randomUUID().toString());
            newTopic.setName(name);
            newTopic.setDescription(description);
            topics.add(newTopic);
            Toast.makeText(this, "Đã thêm topic mới", Toast.LENGTH_SHORT).show();
        }

        saveTopicsToPrefs(topics);
        finish();
    }

    private List<Topic> loadTopicsFromPrefs() {
        String json = sharedPreferences.getString(TOPICS_KEY, null);
        if (json != null) {
            Type type = new TypeToken<List<Topic>>() {}.getType();
            List<Topic> topics = new Gson().fromJson(json, type);
            return topics != null ? topics : new ArrayList<>();
        }
        return new ArrayList<>();
    }

    private void saveTopicsToPrefs(List<Topic> topics) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(topics);
        editor.putString(TOPICS_KEY, json);
        editor.apply();
    }
}

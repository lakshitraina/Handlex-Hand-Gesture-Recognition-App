package com.example.handlex;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoTutorialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private String[] videoIds = {
            "0FcwzMq4iWg", // Example YouTube video IDs (replace with actual links)
            "CGqXy3JOZRs",
            "CGqXy3JOZRs",
            "4Ll3OtqAzyw",
            "DBQINq0SsAw"
    };
    private String[] lessonTitles = {
            "Lesson 1: Introduction to Hand Signs",
            "Lesson 2: Basic Gestures",
            "Lesson 3: Intermediate Gestures",
            "Lesson 4: Advanced Gestures",
            "Lesson 5: Mastering Hand Signs"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_tutorial);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoAdapter = new VideoAdapter(videoIds, lessonTitles, this::onLessonCompleted);
        recyclerView.setAdapter(videoAdapter);
    }

    private void onLessonCompleted(int lessonNumber) {
        // Reward logic based on lesson completion
        String rewardTitle = getRewardTitle(lessonNumber);
        Toast.makeText(this, "Congratulations! You earned: " + rewardTitle, Toast.LENGTH_LONG).show();
    }

    private String getRewardTitle(int lessonNumber) {
        switch (lessonNumber) {
            case 1: return "Beginner Learner";
            case 2: return "Learner";
            case 3: return "Intermediate Learner";
            case 4: return "Master Learner";
            case 5: return "Expert Learner";
            default: return "Unknown Reward";
        }
    }
}
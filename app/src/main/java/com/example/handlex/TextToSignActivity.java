package com.example.handlex;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class TextToSignActivity extends AppCompatActivity {
    private EditText inputText;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_sign);

        inputText = findViewById(R.id.inputText);
        videoView = findViewById(R.id.videoView);

        findViewById(R.id.translateBtn).setOnClickListener(v -> {
            String text = inputText.getText().toString().trim();
            playSignVideo(text);
        });
    }

    private void playSignVideo(String word) {
        String videoPath = "android.resource://" + getPackageName() + "/raw/" + word.toLowerCase();
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }
}

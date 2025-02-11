package com.example.handlex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnSignRecognition, btnTextToSign, btnSpeechToText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignRecognition = findViewById(R.id.btnSignRecognition);
        btnTextToSign = findViewById(R.id.btnTextToSign);
        btnSpeechToText = findViewById(R.id.btnSpeechToText);

        // Navigate to Sign Recognition Activity
        btnSignRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignRecognitionActivity.class));
            }
        });

        // Navigate to Text to Sign Activity
        btnTextToSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TextToSignActivity.class));
            }
        });

        // Navigate to Speech to Text Activity
        btnSpeechToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpeechToTextActivity.class));
            }
        });

        btnvd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpeechToTextActivity.class));
            }
        });
    }
}

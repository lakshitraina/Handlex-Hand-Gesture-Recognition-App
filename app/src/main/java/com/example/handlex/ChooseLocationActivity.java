package com.example.handlex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handlex.databinding.ActivityChooseLocationBinding;

public class ChooseLocationActivity extends AppCompatActivity {

    private ActivityChooseLocationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityChooseLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button button= (Button) findViewById(R.id.doneButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ChooseLocationActivity.this,LoginActivity.class));
            }
        });


        // List of locations
        String[] locationList = new String[]{
                "Mute", "Deaf"
        };

        // Create adapter and set to AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationList);
        AutoCompleteTextView autoCompleteTextView = binding.listOfLocation;
        autoCompleteTextView.setAdapter(adapter);
    }
}
package com.example.handlex;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForumActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText forumMessage;
    private Button postButton;
    private ListView forumListView;
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        db = FirebaseFirestore.getInstance();
        forumMessage = findViewById(R.id.forumMessage);
        postButton = findViewById(R.id.postButton);
        forumListView = findViewById(R.id.forumListView);

        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        forumListView.setAdapter(adapter);

        loadMessages();

        postButton.setOnClickListener(v -> {
            String message = forumMessage.getText().toString();
            if (!message.isEmpty()) {
                Map<String, Object> post = new HashMap<>();
                post.put("message", message);
                db.collection("forums").add(post);
                forumMessage.setText("");
                loadMessages();
            }
        });
    }

    private void loadMessages() {
        db.collection("forums").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messages.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    messages.add(doc.getString("message"));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}

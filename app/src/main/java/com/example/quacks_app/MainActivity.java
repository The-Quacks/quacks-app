package com.example.quacks_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CRUD<Event> crud = new CRUD<>(Event.class);
        Map<String, String> mapping = new HashMap<>();
        mapping.put("organizerId", "vjGUWpp8dsHqoTCklNQB");
        crud.readQueryLive(mapping, new ReadMultipleCallback<Event>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<Event> data) {
                TextView textView = findViewById(R.id.text_sample);
                if (!data.isEmpty()) {
                    textView.setText(data.get(0).getDescription());
                }
            }

            @Override
            public void onReadMultipleFailure(Exception e) {

            }
        });
    }
}
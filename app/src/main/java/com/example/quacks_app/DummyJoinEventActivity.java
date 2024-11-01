package com.example.quacks_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DummyJoinEventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_join_event_activity);
        String id = getIntent().getStringExtra("id");
        CRUD<Event> crud = new CRUD<>(Event.class);
        crud.readStatic(id, (Event data) -> {
            TextView textView = findViewById(R.id.textView);
            TextView textView2 = findViewById(R.id.textView2);
            TextView textView3 = findViewById(R.id.textView3);
            TextView textView4 = findViewById(R.id.textView4);
            if (data != null) {
                textView.setText(data.getId());
                textView2.setText(data.getOrganizerId());
                textView3.setText(data.getDateTime().toString());
                textView4.setText(data.getDescription());
            } else {
                textView.setText("Failed");
            }
        });
    }
}

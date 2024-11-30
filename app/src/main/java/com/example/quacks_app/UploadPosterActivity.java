package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UploadPosterActivity extends AppCompatActivity {
    private Event event;
    private User user;
    private Facility facility;
    private EventList eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.upload_poster);

        event = (Event) getIntent().getSerializableExtra("Event");
        user = (User) getIntent().getSerializableExtra("User");
        facility = (Facility) getIntent().getSerializableExtra("Facility");

        ImageView imageView = findViewById(R.id.poster);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button next = findViewById(R.id.next_button_poster);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadPosterActivity.this, ViewEvents.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                startActivity(intent);
                finish();

            }
        });
    }
}

package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EventDescription extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

//        If the device id doesn't have a profile
        Button joinWaitlist = findViewById(R.id.joinWaitlistButton);
        joinWaitlist.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateEntrantProfile.class);
            startActivity(intent);
        });
    }

}

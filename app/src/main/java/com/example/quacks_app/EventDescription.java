package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EventDescription extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        ImageButton back = findViewById(R.id.backButton);
        Button joinWaitlist = findViewById(R.id.joinWaitlistButton);
        back.setOnClickListener(v -> {
            startActivity(new Intent(this, EntrantHome.class));
        });


//        joinWaitlist.setOnClickListener(v -> {
//            join the waitlist
//        });
    }

}

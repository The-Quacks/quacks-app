package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeEntrant extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_entrant);
        EdgeToEdge.enable(this);

        Button welcomeHome = findViewById(R.id.welcome_home);
        Button createFacility = findViewById(R.id.CREATE_FACILITY);
        ImageButton homeIcon = findViewById(R.id.homeIcon);

        welcomeHome.setOnClickListener(view -> {
            startActivity(new Intent(WelcomeEntrant.this, EntrantHome.class));
        });

        homeIcon.setOnClickListener(view -> {
            startActivity(new Intent(WelcomeEntrant.this, EntrantHome.class));
        });

//        createFacility.setOnClickListener(view -> {
//            Go to create facility page
//        }
    }
}

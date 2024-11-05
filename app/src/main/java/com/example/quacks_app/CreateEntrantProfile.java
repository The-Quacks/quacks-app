package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEntrantProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_entrant_profile);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, EntrantHome.class);
            startActivity(intent);
        });
    }
}

package com.example.quacks_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_home);

        ImageButton Profile = findViewById(R.id.profilesButton);

        Profile.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "PROFILE!", Toast.LENGTH_SHORT).show();
        });


    }
}
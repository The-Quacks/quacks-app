package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AdminHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        EdgeToEdge.enable(this);

        ImageButton Profile = findViewById(R.id.profilesButton);

        Profile.setOnClickListener(view -> {
            Intent myIntent = new Intent(AdminHome.this, AdminListView.class);
            startActivity(myIntent);
        });


    }
}

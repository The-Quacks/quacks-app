package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EntrantHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_home);
        EdgeToEdge.enable(this);

        ImageButton profile = findViewById(R.id.profileButton);
        ImageButton waitlist = findViewById(R.id.waitlistButton);
        ImageButton notifications = findViewById(R.id.notificationsButton);
        ImageButton scanQRCode = findViewById(R.id.scanQRCodeButton);

        profile.setOnClickListener(view -> {
            if (true) { // If user doesn't have a profile
                startActivity(new Intent(EntrantHome.this, CreateEntrantProfile.class));
            }
        });

//        waitlist.setOnClickListener(view -> {
//            startActivity(new Intent(EntrantHome.this, Waitlist.class));
//        });

//        notifications.setOnClickListener(view -> {
//            startActivity(new Intent(EntrantHome.this, Notifications.class));
//        });

        scanQRCode.setOnClickListener(view -> {
            startActivity(new Intent(EntrantHome.this, ScanQRCode.class));
        });
    }
}

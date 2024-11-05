package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        ReadMultipleCallback<User> readMultipleCallback = new ReadMultipleCallback<User>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<User> data) {
                ImageButton profile = findViewById(R.id.profileButton);
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                for (User user : data) {
                    if (user.getDeviceId().equals(deviceId)) {
                        profile.setTag("true");
                        break;
                    }
                }
            }
            @Override
            public void onReadMultipleFailure(Exception e) {
                Toast.makeText(EntrantHome.this, "Could not connect to database", Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, String> deviceId = new HashMap<>();
        deviceId.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        CRUD<User> crud = new CRUD<>(User.class);
        crud.readQueryStatic(deviceId, readMultipleCallback);

        profile.setOnClickListener(view -> {
            if (profile.getTag().equals("false")) {
                startActivity(new Intent(EntrantHome.this, CreateEntrantProfile.class));
            }
            else {
                Toast.makeText(EntrantHome.this, "Go to profile activity", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(EntrantHome.this, EntrantProfile.class));
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

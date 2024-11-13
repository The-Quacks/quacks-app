package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code WelcomeEntrant} class is the first page users should see when they open the app.
 * It has a button to go to the entrant home page, in which case the user is assumed to have the
 * entrant role, and a button to create a facility, which means that the user has the organizer
 * role.
 */

public class WelcomeEntrant extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_entrant);
        EdgeToEdge.enable(this);

        Button welcomeHome = findViewById(R.id.welcome_home);
        Button createFacility = findViewById(R.id.CREATE_FACILITY);
        ImageButton homeIcon = findViewById(R.id.homeIcon);

        welcomeHome.setOnClickListener(view -> {
            if (user != null) {
                Intent intent = new Intent(WelcomeEntrant.this, EntrantHome.class);
                intent.putExtra("User", user);
                startActivity(intent);
                finish();
            }
        });

        homeIcon.setOnClickListener(view -> {
            if (user != null) {
                Intent intent = new Intent(WelcomeEntrant.this, EntrantHome.class);
                intent.putExtra("User", user);
                startActivity(intent);
                finish();
            }
        });
        ReadMultipleCallback<User> readMultipleCallback = new ReadMultipleCallback<User>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<User> data) {
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                for (User u : data) {
                    if (u.getDeviceId().equals(deviceId)) {
                        user = u;
                        break;
                    }
                }
                // Create user if they don't exist yet
                if (data.isEmpty()) {
                    User u = new User();
                    u.setDeviceId(deviceId);
                    ArrayList<Role> roles = new ArrayList<>();
                    roles.add(Role.ENTRANT);
                    u.setRoles(roles);
                    CRUD.create(u, new CreateCallback() {
                        @Override
                        public void onCreateSuccess() {
                            user = u;
                        }

                        @Override
                        public void onCreateFailure(Exception e) {
                            Toast.makeText(WelcomeEntrant.this, "Failed to create user", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onReadMultipleFailure(Exception e) {
                Toast.makeText(WelcomeEntrant.this, "Could not connect to database", Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, Object> deviceId = new HashMap<>();
        deviceId.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        CRUD.readQueryStatic(deviceId, User.class, readMultipleCallback);

//        createFacility.setOnClickListener(view -> {
//            Go to create facility page
//        }
    }
}

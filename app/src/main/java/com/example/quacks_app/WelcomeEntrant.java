package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private Facility facility;
    private ActivityResultLauncher<Intent> resultLauncher;
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

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            user = (User) data.getSerializableExtra("User");
                            facility = (Facility) data.getSerializableExtra("Facility");
                        }
                    }
                });

        createFacility.setOnClickListener(v -> {
            if (user != null) {
                if (facility == null) {
                    Intent intent = new Intent(WelcomeEntrant.this, CreateFacility.class);
                    intent.putExtra("User", user);
                    resultLauncher.launch(intent);

                }
                else {
                    Toast.makeText(WelcomeEntrant.this, "Facility already exists", Toast.LENGTH_SHORT).show();
                }

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
                //fetch their facility if they have one
                else {
                    ReadMultipleCallback<Facility> readMultipleCallback = new ReadMultipleCallback<Facility>() {
                        @Override
                        public void onReadMultipleSuccess(ArrayList<Facility> data) {
                            for (Facility fac : data) {
                                if (fac.getOrganizerId().equals(user.getDocumentId())) {
                                    facility = fac;
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onReadMultipleFailure(Exception e) {
                            Toast.makeText(WelcomeEntrant.this, "Could not connect to database", Toast.LENGTH_SHORT).show();
                        }
                    };

                    Map<String, Object> query = new HashMap<>();
                    query.put("organizerId", user.getDocumentId());
                    CRUD.readQueryStatic(query, Facility.class, readMultipleCallback);
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

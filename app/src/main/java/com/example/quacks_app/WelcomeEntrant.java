package com.example.quacks_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

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
    private static final int REQUEST_CODE = 1;
    private NotificationHandler nHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_entrant);
        EdgeToEdge.enable(this);

        Button welcomeHome = findViewById(R.id.welcome_home);
        Button createFacility = findViewById(R.id.CREATE_FACILITY);
        ImageButton homeIcon = findViewById(R.id.homeIcon);
        nHandler = new NotificationHandler();
        NotificationHandler.askForPermission(this);
        NotificationHandler.createChannel(this);

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

    //Separate overidden function outside onCreate
    //This function responds with a Snackbar if notifications are enabled/disabled
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //call super
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Define Snackbar
        Snackbar resultSnackbar;
        //Build Alert to prompt permission change via Settings.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Change Permissions in Settings");
        alertDialogBuilder
                .setTitle("Change Notification Permissions")
                .setMessage("\nClick SETTINGS to manually set permissions.")
                .setCancelable(false)
                //If user wants to change, send to Settings App, in notification settings for this app.
                .setPositiveButton("SETTINGS", (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri URI = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(URI);
                    startActivity(intent);
                })
                //If not, just cancel the alert dialog box.
                .setNegativeButton("CANCEL", (dialog, id) -> {
                    dialog.cancel();
                });
        // If initally asking for notification permissions (aka request code matches):
        if (requestCode == REQUEST_CODE) {
            // If granted, from now on show Snackbar that says "Notifications Enabled", with option to change permissions.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                resultSnackbar = Snackbar.make(this, this.findViewById(R.id.welcome_home).getRootView(), "Notifications Enabled", Snackbar.LENGTH_LONG);
                resultSnackbar.setAction("EDIT", view -> {
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                });
                resultSnackbar.show();
                // If denied, from now on show Snackbar that says "Notifications Disabled", with option to change permissions.
            } else {
                resultSnackbar = Snackbar.make(this, this.findViewById(R.id.welcome_home).getRootView(), "Notifications Disabled", Snackbar.LENGTH_LONG);
                resultSnackbar.setAction("EDIT", view -> {
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                });
                resultSnackbar.show();
            }
        }
    }
}

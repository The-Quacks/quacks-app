package com.example.quacks_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code EntrantHome} class is used on the home page for entrants. It includes buttons to
 * go to different parts of the app. Currently, the scan QR code button is implemented, which allows
 * the user to scan a QR code, see the event information, and join the waitlist.
 */

public class EntrantHome extends AppCompatActivity {
    static boolean hasProfile = false;
    private User user;

    private static final int REQUEST_CODE = 1;
    private NotificationHandler nHandler;

    private EventList eventIds;
    private ArrayList<Event> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_home);
        EdgeToEdge.enable(this);

        nHandler = new NotificationHandler();
        NotificationHandler.askForPermission(this);
        NotificationHandler.createChannel(this);

        user = (User) getIntent().getSerializableExtra("User");
        if (user != null && user.getUserProfile() != null) {
            hasProfile = true;
            eventIds = user.getUserProfile().getEventList();
        }

        ImageButton profile = findViewById(R.id.profileButton);
        ImageButton waitlist = findViewById(R.id.waitlistButton);
        ImageButton notifications = findViewById(R.id.notificationsButton);
        ImageButton scanQRCode = findViewById(R.id.scanQRCodeButton);
        ImageButton switch_activity = findViewById(R.id.switch_activity_entrant);

        // Set up the ActivityResultLauncher
        ActivityResultLauncher<Intent> swapActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Get updated user and facility from the result
                        user = (User) result.getData().getSerializableExtra("User");

                        // Update the UI based on the new data
                        updateUI(switch_activity);
                    }
                }
        );

        // Dynamically update the UI
        updateUI(switch_activity);
        setupRoleListener(switch_activity);

        switch_activity.setOnClickListener(v -> {
            if (user.getRoles().contains(Role.ORGANIZER)) {
                Intent intent = new Intent(EntrantHome.this, OrganizerHomepage.class);
                intent.putExtra("User", user);
                swapActivityLauncher.launch(intent);
            }
            else if (user.getRoles().contains(Role.ADMIN)) {
                Intent intent = new Intent(EntrantHome.this, AdminHome.class);
                intent.putExtra("User", user);
                swapActivityLauncher.launch(intent);
            }
        });


        profile.setOnClickListener(view -> {
            if (!hasProfile) {
                Intent intent = new Intent(EntrantHome.this, CreateEntrantProfile.class);
                intent.putExtra("User", user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(EntrantHome.this, ProfileActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        waitlist.setOnClickListener(view -> {
            if (!hasProfile) {
                Toast.makeText(EntrantHome.this, "Please create a profile first!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (eventIds == null) {
                Toast.makeText(EntrantHome.this, "No events found in waitlist", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> query = new HashMap<>();
            for (String eventId : eventIds.getEventIds()) {
                query.put("eventId", eventId);
            }

            if (query.isEmpty()) {
                Toast.makeText(EntrantHome.this, "No events found in waitlist", Toast.LENGTH_SHORT).show();
                return;
            }

            CRUD.readQueryStatic(query, Event.class, new ReadMultipleCallback<Event>() {
                @Override
                public void onReadMultipleSuccess(ArrayList<Event> data) {
                    events = data;
                    if (!events.isEmpty()) {
                        Intent entrantWaitlistIntent = new Intent(EntrantHome.this, ViewEventsEntrant.class);
                        entrantWaitlistIntent.putExtra("User", user);
                        entrantWaitlistIntent.putExtra("EventList", events);
                        startActivity(entrantWaitlistIntent);
                    } else {
                        Toast.makeText(EntrantHome.this, "No events found in waitlist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onReadMultipleFailure(Exception e) {
                    Toast.makeText(EntrantHome.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                }
            });
        });

//        notifications.setOnClickListener(view -> {
//            startActivity(new Intent(EntrantHome.this, Notifications.class));
//        });

        scanQRCode.setOnClickListener(view -> {
            GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                    .build();

            GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(this, options);
            scanner.startScan()
                    .addOnSuccessListener(barcode -> {
                        String id = barcode.getRawValue();
                        Intent switchActivityIntent = new Intent(getApplicationContext(),
                                EventDescription.class);
                        switchActivityIntent.putExtra("id", id);
                        switchActivityIntent.putExtra("User", user);
                        switchActivityIntent.putExtra("isRemoving", false);
                        startActivity(switchActivityIntent);
                    })
                    .addOnCanceledListener(this::finish)
                    .addOnFailureListener(e -> {
                    });
        });
    }

    /**
     * Updates the UI dynamically based on the user's facility and role status.
     */
    private void updateUI(ImageButton switch_activity) {
        // Hide Switch Activity button if role organizer or admin exists
        if (user.getRoles().contains(Role.ORGANIZER) || user.getRoles().contains(Role.ADMIN)) {
            switch_activity.setVisibility(View.VISIBLE);
        } else {
            switch_activity.setVisibility(View.GONE);
        }
    }

    private void setupRoleListener(ImageButton switch_activity) {
        if (user != null && user.getDocumentId() != null) {
            CRUD.readLive(user.getDocumentId(), User.class, new ReadCallback<User>() {
                @Override
                public void onReadSuccess(User updatedUser) {
                    user = updatedUser; // Update the user object with new data
                    updateUI(switch_activity); // Dynamically update the UI
                }

                @Override
                public void onReadFailure(Exception e) {
                    Toast.makeText(EntrantHome.this, "Failed to monitor user roles.", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                resultSnackbar = Snackbar.make(this, this.findViewById(R.id.notificationsButton).getRootView(), "Notifications Enabled", Snackbar.LENGTH_LONG);
                resultSnackbar.setAction("EDIT", view -> {
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                });
                resultSnackbar.show();
                // If denied, from now on show Snackbar that says "Notifications Disabled", with option to change permissions.
            } else {
                resultSnackbar = Snackbar.make(this, this.findViewById(R.id.notificationsButton).getRootView(), "Notifications Disabled", Snackbar.LENGTH_LONG);
                resultSnackbar.setAction("EDIT", view -> {
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                });
                resultSnackbar.show();
            }
        }
    }
}

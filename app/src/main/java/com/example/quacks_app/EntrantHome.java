package com.example.quacks_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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
 * go to different parts of the app. If the user is an organizer or entrant, the button in the bottom
 * corner will switch to the proper home page. Pressing profile displays the entrant's profile,
 * and allows them to edit their details. The waitlist button shows all of the events that the
 * user has joined the waitlist for. The scan QR code button lets the user scan an event's
 * code, and then displays details about the event.
 */

public class EntrantHome extends AppCompatActivity {
    static boolean hasProfile = false;
    private User user;

    private static final int REQUEST_CODE = 1;
    private NotificationHandler nHandler;

    private EventList eventIds;
    private ArrayList<Event> events;

    boolean isFirstSelection = true;

    /**
     * Initializes the Entrant Home page, sets up UI components, and handles user interactions.
     *
     * @param savedInstanceState If the activity is reinitialized after previously being shut down,
     *                           this bundle contains the most recent data. Otherwise, it is {@code null}.
     */
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
        Spinner spinner = findViewById(R.id.profile_spinner_entrant);
        ImageButton goBack = findViewById(R.id.homeIcon);

        // Set up the ActivityResultLauncher
        ActivityResultLauncher<Intent> swapActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Get updated user and facility from the result
                        user = (User) result.getData().getSerializableExtra("User");
                    }
                }
        );

        ArrayAdapter<String> adapter = getStringArrayAdapter();
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return; // Ignore the initial trigger
                }

                String selectedItem = parent.getItemAtPosition(position).toString();

                if ("Administrator Profile".equals(selectedItem)) {
                    Intent intent = new Intent(EntrantHome.this, AdminHome.class);
                    intent.putExtra("User", user);
                    swapActivityLauncher.launch(intent);

                }

                else if ("Organizer Profile".equals(selectedItem)) {
                    Intent intent = new Intent(EntrantHome.this, OrganizerHomepage.class);
                    intent.putExtra("User", user);
                    swapActivityLauncher.launch(intent);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        notifications.setOnClickListener(v -> {
            Intent intent = new Intent(EntrantHome.this, NotificationCenter.class);
            intent.putExtra("User", user);
            intent.putExtra("Notif_Handler", nHandler);
            startActivity(intent);
        });
    }

    /**
     * Initializes the spinner adapter with selectable profiles.
     *
     * @return An {@link ArrayAdapter} with profile options.
     */
    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter() {
        ArrayList<String> items = new ArrayList<>();
        items.add("Entrant Profile");
        if (user.getRoles().contains(Role.ORGANIZER)) {
            items.add("Organizer Profile");
        }
        if (user.getRoles().contains(Role.ADMIN)) {
            items.add("Administrator Profile");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * Handles the result of permission requests for notifications.
     *
     * @param requestCode  The request code passed in {@code requestPermissions}.
     * @param permissions  The requested permissions.
     * @param grantResults The results for the corresponding permissions.
     */
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
                nHandler.appEnabled = true;
                nHandler.phoneEnabled = true;
                nHandler.getNotificationForUser(user, new ReadMultipleCallback<Notification>() {
                    @Override
                    public void onReadMultipleSuccess(ArrayList<Notification> data) {
                        if (data != null) {
                            ArrayList<Notification> notifications = new ArrayList<>();
                            for (Notification notification : data) {
                                if (notification.getUser().getDeviceId().equals(user.getDeviceId())) {
                                    if (notification.getSentStatus().equals("Not Sent")) {

                                        notifications.add(notification);
                                    }

                                }
                            }
                            nHandler.sendUnreadNotifications(EntrantHome.this, notifications);
                        }
                    }

                    @Override
                    public void onReadMultipleFailure(Exception e) {

                    }
                });


                // If denied, from now on show Snackbar that says "Notifications Disabled", with option to change permissions.
            } else {
                resultSnackbar = Snackbar.make(this, this.findViewById(R.id.notificationsButton).getRootView(), "Notifications Disabled", Snackbar.LENGTH_LONG);
                resultSnackbar.setAction("EDIT", view -> {
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                });
                resultSnackbar.show();
                nHandler.appEnabled = false;
                nHandler.phoneEnabled = false;
            }
        }
    }
}

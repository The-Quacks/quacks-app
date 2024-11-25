package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
    private EventList eventIds;
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_home);
        EdgeToEdge.enable(this);

        user = (User) getIntent().getSerializableExtra("User");
        if (user.getUserProfile() != null) {
            hasProfile = true;
            eventIds = user.getUserProfile().getEventList();
        }

        ImageButton profile = findViewById(R.id.profileButton);
        ImageButton waitlist = findViewById(R.id.waitlistButton);
        ImageButton notifications = findViewById(R.id.notificationsButton);
        ImageButton scanQRCode = findViewById(R.id.scanQRCodeButton);
        ImageButton switch_activity = findViewById(R.id.switch_activity_entrant);

        switch_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getRoles().contains(Role.ORGANIZER)) {
                    Intent intent = new Intent(EntrantHome.this, OrganizerHomepage.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                    finish();
                }
                else if (user.getRoles().contains(Role.ADMIN)) {
                    Intent intent = new Intent(EntrantHome.this, AdminHome.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                    finish();
                }
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
            Map<String, Object> query = new HashMap<>();
            for (String eventId : eventIds.getEventIds()) {
                query.put("eventId", eventId);
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
                        switchActivityIntent.putExtra("isRemoving", false);
                        startActivity(switchActivityIntent);
                    })
                    .addOnCanceledListener(this::finish)
                    .addOnFailureListener(e -> {
                    });
        });


        // Add list of eventIds to entrant attributes (done)
        // For this, get the list of eventIds that match the array
        // Then display the list on the next page
    }
}

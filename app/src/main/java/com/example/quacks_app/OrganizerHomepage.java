package com.example.quacks_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrganizerHomepage extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button profile;
    private Button view_events;
    private Button create_events;
    private Button entrant_map;
    private Facility facility;
    private User current;
    private EventList eventList;

    /**
     * This is the Organizer Homepage: they can choose out of the four options
     * Edit Profile, View Events, Create Events, Entrant Map
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_homepage);// or the correct XML layout file

        //event = new ArrayList<Event>();
        eventList = new EventList();

        profile = findViewById(R.id.organizer_profile_button);
        view_events = findViewById(R.id.view_events);
        create_events = findViewById(R.id.create_event);
        entrant_map = findViewById(R.id.map);

        profile.setTag("false");


        ReadMultipleCallback<User> readMultipleCallback = new ReadMultipleCallback<User>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<User> data) {
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                for (User user : data) {
                    if (user.getDeviceId().equals(deviceId)) {
                        current = user;
                        if (user.getUserProfile() != null) {
                            UserProfile temp = user.getUserProfile();
                            Map<String, Object> query = new HashMap<>();
                            query.put("organizerId", user.getDocumentId());
                            CRUD.readQueryStatic(query, Facility.class, new ReadMultipleCallback<Facility>() {
                                @Override
                                public void onReadMultipleSuccess(ArrayList<Facility> data) {
                                    if (data.isEmpty()) {
                                        Toast.makeText(OrganizerHomepage.this, "No facility found", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (data.size() > 1) {
                                        Toast.makeText(OrganizerHomepage.this, "Multiple facilities found", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        facility = data.get(0);
                                    }
                                }

                                @Override
                                public void onReadMultipleFailure(Exception e) {
                                    Toast.makeText(OrganizerHomepage.this, "Could not retrieve facilities", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        profile.setTag("true");
                        break;
                    }
                }
            }

            @Override
            public void onReadMultipleFailure(Exception e) {
                Toast.makeText(OrganizerHomepage.this, "Could not connect to database", Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, Object> deviceId = new HashMap<>();
        deviceId.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        CRUD.readQueryStatic(deviceId, User.class, readMultipleCallback);

        profile.setOnClickListener(view -> {
            if (profile.getTag().equals("false")) {
                startActivityForResult(new Intent(OrganizerHomepage.this, CreateFacility.class), 1);
            } else {
                Intent intent = new Intent(OrganizerHomepage.this, ViewOrganizer.class);
                intent.putExtra("User", current);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        view_events.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerHomepage.this, ViewEvents.class);
            intent.putExtra("User", current);
            intent.putExtra("EventList", eventList);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });
        create_events.setOnClickListener(view -> {
            Intent intent =new Intent(OrganizerHomepage.this, CreateEvent.class);
            intent.putExtra("User", current);
            intent.putExtra("EventList", eventList);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });

        entrant_map.setOnClickListener(view -> Toast.makeText(OrganizerHomepage.this, "Entrant Map Coming Soon!", Toast.LENGTH_SHORT).show());

    }

    /**
     * This gets the organizer profile back from the create event page.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {  // check for the specific request code
            if (resultCode == RESULT_OK) {
                current =(User) data.getSerializableExtra("User");
                facility = (Facility) data.getSerializableExtra("Facility");
                if (current == null || facility == null) {
                    Toast.makeText(OrganizerHomepage.this, "Error: Facility and Current are Null", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
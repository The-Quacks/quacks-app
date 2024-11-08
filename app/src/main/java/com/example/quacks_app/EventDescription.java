package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code EventDescription} class is used to show important information about a selected event.
 * It also includes the option to join the waitlist for that event. The information displayed will
 * be changed in future implementations, when more functionality is completed. Additionally, the
 * code to join the waitlist will be improved in the future.
 */

public class EventDescription extends AppCompatActivity {
    private String userId;
    private String applicantListId;
    private ApplicantList applicantList = new ApplicantList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        String id = getIntent().getStringExtra("id");
        CRUD<Event> eventCrud = new CRUD<>(Event.class);
        ReadCallback<Event> readEventCallback = new ReadCallback<Event>() {
            @Override
            public void onReadSuccess(Event data) {
                TextView eventTitle = findViewById(R.id.eventTitle);
                eventTitle.setText(data.getDescription());
                // This will need to be updated after merging with the full Event implementation
//                eventDescription.setText(String.format("Capacity: %s\nRegistration open until: %s\nClass Duration: %s\nInstructor: %s\nGeolocation Requirement: %s", data.getClass_capacity(), data.getStartDateTime().toString(), data.getEndDateTime().toString(), data.getInstructor(), data.getGeolocation()));
                TextView eventDescription = findViewById(R.id.eventDescription);
                eventDescription.setText(String.format("Applicant List: %s\nDateTime: %s\nFacility: %s\nOrganizer: %s", data.getApplicantList(), data.getDateTime(), data.getFacility(), data.getOrganizerId()));
                applicantListId = data.getApplicantList();
            }
            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(EventDescription.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
            }
        };

        eventCrud.readStatic(id, readEventCallback);

        ImageButton back = findViewById(R.id.backButton);
        ImageButton home = findViewById(R.id.homeIcon);
        Button joinWaitlist = findViewById(R.id.joinWaitlistButton);

        back.setOnClickListener(v -> {
            finish();
        });

        home.setOnClickListener(v -> {
            startActivity(new Intent(this, EntrantHome.class));
        });


        joinWaitlist.setOnClickListener(v -> {
            // **This code needs a LOT of refactoring**


            if (!EntrantHome.hasProfile) {
                Toast.makeText(this, "Please create a profile", Toast.LENGTH_SHORT).show();
                Intent createProfile = new Intent(this, CreateEntrantProfile.class);
                startActivity(createProfile);
            } else {
                Map<String, String> user = new HashMap<>();
                user.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

                ReadMultipleCallback<User> readMultipleCallback = new ReadMultipleCallback<User>() {
                    @Override
                    public void onReadMultipleSuccess(ArrayList<User> data) {
                        for (User user : data) {
                            if (user.getDeviceId().equals(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))) {
                                userId = user.getId();
                                Log.d("User ID", userId);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onReadMultipleFailure(Exception e) {
                        Toast.makeText(EventDescription.this, "Could not connect to database", Toast.LENGTH_SHORT).show();
                    }
                };

                CRUD<User> getUserId = new CRUD<>(User.class);
                getUserId.readQueryStatic(user, readMultipleCallback);


                UpdateCallback updateCallback = new UpdateCallback() {
                    @Override
                    public void onUpdateSuccess() {
                        Toast.makeText(EventDescription.this, "Successfully joined waitlist", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EventDescription.this, EntrantHome.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onUpdateFailure(Exception e) {
                        Toast.makeText(EventDescription.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
                    }
                };

                CRUD<ApplicantList> applicantListCrud = new CRUD<>(ApplicantList.class);

                ReadCallback<ApplicantList> readAppListCallback = new ReadCallback<ApplicantList>() {
                    @Override
                    public void onReadSuccess(ApplicantList data) {
                        ArrayList<String> applicantIds = data.getApplicantIds();
                        applicantList.setApplicantIds(applicantIds);
                        applicantList.addUser(userId);
                        applicantListCrud.update(applicantListId, applicantList, updateCallback);
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(EventDescription.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
                    }
                };
                applicantListCrud.readStatic(applicantListId, readAppListCallback);
            }
        });
    }

}

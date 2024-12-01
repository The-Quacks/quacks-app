package com.example.quacks_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.GeoPoint;

/**
 * The {@code EventDescription} class is used to show important information about a selected event.
 * It also includes the option to join the waitlist for that event. The information displayed will
 * be changed in future implementations, when more functionality is completed. Additionally, the
 * code to join the waitlist will be improved in the future.
 */

public class EventDescription extends AppCompatActivity {
    private String userId;
    private String eventId;
    private String applicantListId;
    private User currentUser;
    private boolean isRemoving;
    private boolean geolocationRequired;
    private Geolocation geolocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        ImageButton back = findViewById(R.id.backButton);
        ImageButton home = findViewById(R.id.homeIcon);
        Button joinWaitlist = findViewById(R.id.joinWaitlistButton);

        eventId = getIntent().getStringExtra("id");
        currentUser = (User) getIntent().getSerializableExtra("User");
        userId = currentUser.getDocumentId();
        isRemoving = getIntent().getBooleanExtra("isRemoving", false);

        if (isRemoving) {
            joinWaitlist.setText("Leave Waitlist");
        }

        ReadCallback<Event> readEventCallback = new ReadCallback<Event>() {
            @Override
            public void onReadSuccess(Event data) {
                TextView eventTitle = findViewById(R.id.eventTitle);
                eventTitle.setText(data.getEventName());
                // This will need to be updated after merging with the full Event implementation
//                eventDescription.setText(String.format("Capacity: %s\nRegistration open until: %s\nClass Duration: %s\nInstructor: %s\nGeolocation Requirement: %s", data.getClass_capacity(), data.getStartDateTime().toString(), data.getEndDateTime().toString(), data.getInstructor(), data.getGeolocation()));
                TextView eventDescription = findViewById(R.id.eventDescription);
                eventDescription.setText(String.format("Applicant List: %s\nStart Time: %s\nDescription: %s\nFacility: %s\nOrganizer: %s\nGeolocation Required: %s", data.getApplicantList(), data.getDateTime(), data.getDescription(), data.getFacility(), data.getOrganizerId(), data.getGeo()));
                geolocationRequired = data.getGeo();
                applicantListId = data.getApplicantList();
            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(EventDescription.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
            }
        };

        CRUD.readLive(eventId, Event.class, readEventCallback);

        back.setOnClickListener(v -> {
            finish();
        });

        home.setOnClickListener(v -> {
            startActivity(new Intent(this, EntrantHome.class));
        });


        joinWaitlist.setOnClickListener(v -> {
            if (userId != null) {
                ReadCallback<ApplicantList> readAppListCallback = new ReadCallback<ApplicantList>() {
                    @Override
                    public void onReadSuccess(ApplicantList applicantList) {
                        if (applicantList == null) {
                            Toast.makeText(EventDescription.this, "Registration has not yet opened for this event", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        if (isRemoving) {
                            if (applicantList.contains(userId)) {
                                applicantList.removeUser(userId);

                                CRUD.update(applicantList, new UpdateCallback() {
                                    @Override
                                    public void onUpdateSuccess() {
                                        removeEventFromUser(eventId);
                                    }

                                    @Override
                                    public void onUpdateFailure(Exception e) {
                                        Toast.makeText(EventDescription.this, "Error leaving waitlist", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(EventDescription.this, "Error: You are not in the waitlist!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (applicantList.contains(userId)) {
                                Toast.makeText(EventDescription.this, "You are already in the waitlist!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (geolocationRequired) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EventDescription.this);
                                builder.setTitle("Geolocation is required to join the waitlist for this event");
                                builder.setMessage("Would you like to continue?");

                                builder.setPositiveButton("Yes", (dialog, which) -> {
                                    geolocation = new Geolocation(EventDescription.this, EventDescription.this);
                                    geolocation.setLocationCallback(new Geolocation.LocationCallback() {
                                        @Override
                                        public void onLocationReceived(double latitude, double longitude) {
                                            currentUser.setGeoPoint(new GeoPoint(latitude, longitude));
                                            applicantList.addUser(userId);
                                            CRUD.update(applicantList, new UpdateCallback() {
                                                @Override
                                                public void onUpdateSuccess() {
                                                    addEventToUser(eventId);
                                                }

                                                @Override
                                                public void onUpdateFailure(Exception e) {
                                                    Toast.makeText(EventDescription.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onLocationError(String error) {
                                            Toast.makeText(EventDescription.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    geolocation.requestLocationPermissions();
                                });

                                builder.setNegativeButton("Cancel", (dialog, which) -> {
                                    dialog.dismiss();
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                applicantList.addUser(userId);
                                CRUD.update(applicantList, new UpdateCallback() {
                                    @Override
                                    public void onUpdateSuccess() {
                                        addEventToUser(eventId);
                                    }

                                    @Override
                                    public void onUpdateFailure(Exception e) {
                                        Toast.makeText(EventDescription.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(EventDescription.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
                    }
                };

                CRUD.readStatic(applicantListId, ApplicantList.class, readAppListCallback);
            } else {
                Toast.makeText(EventDescription.this, "Could not find user in database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEventToUser(String eventId) {
        if (currentUser != null) {
            EventList userEvents = currentUser.getUserProfile().getEventList();
            if (userEvents == null) {
                userEvents = new EventList();
            }
            if (!userEvents.contains(eventId)) {
                userEvents.addEvent(eventId);
                currentUser.getUserProfile().setEventList(userEvents);

                CRUD.update(currentUser, new UpdateCallback() {
                    @Override
                    public void onUpdateSuccess() {
                        Toast.makeText(EventDescription.this, "Successfully joined waitlist", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(EventDescription.this, EntrantHome.class);
                        intent.putExtra("User", currentUser);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onUpdateFailure(Exception e) {
                        Toast.makeText(EventDescription.this, "Error updating", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(EventDescription.this, "You are already in the waitlist!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void removeEventFromUser(String eventId) {
        if (currentUser != null) {
            EventList userEvents = currentUser.getUserProfile().getEventList();
            if (userEvents.contains(eventId)) {
                userEvents.removeEvent(eventId);
                currentUser.getUserProfile().setEventList(userEvents);

                CRUD.update(currentUser, new UpdateCallback() {
                    @Override
                    public void onUpdateSuccess() {
                        Toast.makeText(EventDescription.this, "Successfully left the waitlist", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EventDescription.this, EntrantHome.class);
                        intent.putExtra("User", currentUser);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onUpdateFailure(Exception e) {
                        Toast.makeText(EventDescription.this, "Error leaving waitlist", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(EventDescription.this, "Event not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        geolocation.handlePermissionResult(requestCode, permissions, grantResults);
    }
}

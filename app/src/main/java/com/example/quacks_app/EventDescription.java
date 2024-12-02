package com.example.quacks_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * The {@code EventDescription} class is used to show important information about a selected event.
 * It also includes the option to join the waitlist for that event.
 */
public class EventDescription extends AppCompatActivity {
    private String userId;
    private String eventId;
    private String applicantListId;
    private User currentUser;
    private boolean isRemoving;
    private boolean geolocationRequired;
    private Geolocation geolocation;
    private boolean registrationOpen;

    /**
     * Initializes the activity, fetches event details, and sets up the UI for event interaction.
     *
     * @param savedInstanceState If the activity is being reinitialized after being shut down,
     *                           this bundle contains the most recent data. Otherwise, it is {@code null}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        // Buttons and navigation
        ImageButton back = findViewById(R.id.backButton);
        Button joinWaitlist = findViewById(R.id.joinWaitlistButton);

        // Get data from Intent
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
                // Find the text views
                TextView eventTitle = findViewById(R.id.eventTitle);
                TextView eventStartTime = findViewById(R.id.eventStartTime);
                TextView eventDescription = findViewById(R.id.eventDescription);
                TextView eventFacility = findViewById(R.id.eventFacility);
                TextView eventOrganizer = findViewById(R.id.eventOrganizer);
                TextView eventGeo = findViewById(R.id.eventGeoLocation);

                // Set the values
                eventTitle.setText(data.getEventName());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String formattedDate = dateFormat.format(data.getDateTime());
                eventStartTime.setText(formattedDate);
                eventDescription.setText(data.getDescription());

                // Fetch Facility Name
                CRUD.readStatic(data.getFacility(), Facility.class, new ReadCallback<Facility>() {
                    @Override
                    public void onReadSuccess(Facility facility) {
                        if (facility != null) {
                            eventFacility.setText(facility.getName());
                        } else {
                            eventFacility.setText("Facility not found");
                        }
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        eventFacility.setText("Error fetching facility name");
                        Toast.makeText(EventDescription.this, "Error fetching facility details", Toast.LENGTH_SHORT).show();
                    }
                });

                // Fetch Organizer Name
                CRUD.readStatic(data.getOrganizerId(), User.class, new ReadCallback<User>() {
                    @Override
                    public void onReadSuccess(User organizer) {
                        if (organizer != null && organizer.getUserProfile() != null) {
                            eventOrganizer.setText(organizer.getUserProfile().getUserName());
                        } else {
                            Log.d("Event Description","org not found");
                            eventOrganizer.setText("Organizer not found");
                        }
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        eventOrganizer.setText("Error fetching organizer name");
                        Toast.makeText(EventDescription.this, "Error fetching organizer details", Toast.LENGTH_SHORT).show();
                    }
                });

                if (data.getGeo() == Boolean.FALSE){
                    eventGeo.setText("No");
                }
                else{
                    eventGeo.setText("Yes");
                }

                geolocationRequired = data.getGeo();
                applicantListId = data.getApplicantList();
                registrationOpen = data.getRegistration();
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

        ImageButton home = findViewById(R.id.homeIcon);
        home.setOnClickListener(v -> {
            Intent intent = new Intent(EventDescription.this, EntrantHome.class);
            intent.putExtra("User", currentUser);
            startActivity(intent);
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

                if (!registrationOpen || Objects.equals(applicantListId, "0") || applicantListId == null) {
                    Toast.makeText(EventDescription.this, "Registration has not yet opened for this event", Toast.LENGTH_SHORT).show();
                    return;
                }
                CRUD.readStatic(applicantListId, ApplicantList.class, readAppListCallback);
            } else {
                Toast.makeText(EventDescription.this, "Could not find user in database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This function adds the event id to the user's event list attribute.
     * @param eventId The current event id
     */
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

    /**
     * This function removes an event id from the user when they leave the waitlist.
     * @param eventId The event to leave
     */
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

    /**
     * Handles the result of geolocation permission requests.
     *
     * @param requestCode The request code for the permission request.
     * @param permissions The requested permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        geolocation.handlePermissionResult(requestCode, permissions, grantResults);
    }
}

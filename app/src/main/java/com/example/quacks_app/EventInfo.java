package com.example.quacks_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
/**
 * The EventInfo class displays detailed information about a specific event and
 * provides options to edit, delete, or manage the event's applicants.
 */
public class EventInfo extends AppCompatActivity implements EditDeleteEventFragment.EditDeleteDialogListener{
    private String applicantList; // ID of the applicant list for the event
    private EventList eventList; // List of all events
    private Facility actual_facility; // The facility where the event is taking place
    private User user; // The user interacting with this activity
    private Event event; // The event being displayed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);

        // Retrieve event, facility, and user data passed from the previous activity
        event = (Event) getIntent().getSerializableExtra("Event");
        if (event == null){
            Toast.makeText(this, "Error: Event data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        actual_facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");

        // Populate UI fields
        TextView eventName = findViewById(R.id.event_name);
        TextView instructor_name = findViewById(R.id.instructor);
        CheckBox geolocation = findViewById(R.id.geolocation_status);
        TextView date = findViewById(R.id.event_date);
        TextView description = findViewById(R.id.event_description);
        TextView event_facility = findViewById(R.id.event_facility);
        TextView organizer = findViewById(R.id.event_organizer);
        TextView id = findViewById(R.id.event_id);
        TextView waitlist_capacity = findViewById(R.id.waitlist_capacity);
        TextView registration_capacity = findViewById(R.id.class_capacity);
        ImageView eventPoster = findViewById(R.id.event_poster);

        // Buttons
        Button open_registration_button = findViewById(R.id.register_button);
        Button close_registration_button = findViewById(R.id.close);
        Button edit_event_button = findViewById(R.id.edit_event_button);
        Button applicant_lists_button = findViewById(R.id.applicant_lists);

        // Populate text fields with event data
        String text = event.getDescription();
        geolocation.setChecked(event.getGeo() != null && event.getGeo());
        geolocation.setClickable(false);
        String waitlist_text = String.valueOf(event.getWaitlistCapacity());
        String capacity_text = String.valueOf(event.getRegistrationCapacity());
        String inst_name = event.getInstructor();
        String event_name = event.getEventName();

        Date dated = event.getDateTime();
        Facility fac = actual_facility;
        String name = "";
        if (event_facility != null) {
            name = fac.getName();
        }
        String organizerId = event.getOrganizerId();
        eventName.setText(event_name);
        instructor_name.setText(inst_name);
        registration_capacity.setText(waitlist_text);
        waitlist_capacity.setText(capacity_text);

        date.setText(dated.toString());
        description.setText(text);
        if (event_facility != null) {
            event_facility.setText(name);
        }
        organizer.setText(organizerId);
        id.setText(event.getEventId());

        // Load profile picture if it exists
        if (event.getEventPosterPath() != null) {
            CRUD.downloadImage(event.getEventPosterPath(), new ReadCallback<Bitmap>() {
                @Override
                public void onReadSuccess(Bitmap data) {
                    eventPoster.setImageBitmap(data);
                }

                @Override
                public void onReadFailure(Exception e) {
                    Log.e("Event Info", "Failed to load event poster: " + e.getMessage());
                }
            });
        }
        if (event.getEventPosterPath() == null) {
            Log.e("Event Info", "event poster doesn't exist: ");
        }

        // Handle button clicks
        open_registration_button.setOnClickListener(view -> {
            //makes an applicant list for that event.
            Intent intent = new Intent(EventInfo.this, OpenRegistration.class);
            intent.putExtra("Event", event);
            intent.putExtra("User", user);
            intent.putExtra("Facility", actual_facility);
            startActivity(intent);
        });

        applicant_lists_button.setOnClickListener(view -> {
            Intent intent = new Intent(EventInfo.this, ApplicantOptions.class);
            intent.putExtra("Event", event);
            intent.putExtra("User", user);
            intent.putExtra("Facility", actual_facility);
            startActivity(intent);

        });

        edit_event_button.setOnClickListener(view -> {
            EditDeleteEventFragment dialog = new EditDeleteEventFragment();
            dialog.show(getSupportFragmentManager(), "EditDeleteDialog");
        });

        close_registration_button.setOnClickListener(view -> {

            if (!event.getRegistration()){
                Toast.makeText(EventInfo.this, "Event is already closed!", Toast.LENGTH_SHORT).show();
            } else{
                event.setRegistration(false);
                String app_id = event.getApplicantList();
                CRUD.readStatic(app_id, ApplicantList.class, new ReadCallback<ApplicantList>() {
                    @Override
                    public void onReadSuccess(ApplicantList data) {
                        if (data != null){
                            event.setFinal_list(data);

                            CRUD.update(event, new UpdateCallback() {
                                @Override
                                public void onUpdateSuccess() {
                                    Toast.makeText(EventInfo.this, "Registration is Closed!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onUpdateFailure(Exception e) {
                                    Toast.makeText(EventInfo.this, "Error Closing Registration.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(EventInfo.this, "Error Closing Registration.", Toast.LENGTH_SHORT).show();
                    }
                });

            }


                    //            CRUD.delete(event.getDocumentId(), Event.class, new DeleteCallback() {
                    //@Override
                    //public void onDeleteSuccess() {
                    //    Toast.makeText(EventInfo.this, "Event has been deleted", Toast.LENGTH_SHORT).show();
                    //    finish();
                    //}

                    //@Override
                    //public void onDeleteFailure(Exception e) {
                    //    Toast.makeText(EventInfo.this, "Error deleting event", Toast.LENGTH_SHORT).show();
                    //}
                    //})
                }
        );

        // This is the bottom of the page directory
        ImageButton homepage = findViewById(R.id.house);
        ImageButton profile = findViewById(R.id.person);
        ImageButton search = findViewById(R.id.search);

        homepage.setOnClickListener(view -> {
            //Already here
            Intent intent = new Intent(EventInfo.this, OrganizerHomepage.class);
            intent.putExtra("Facility", actual_facility);
            intent.putExtra("User", user);
            startActivity(intent);

        });

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(EventInfo.this, ViewOrganizer.class);
            intent.putExtra("Facility", actual_facility);
            intent.putExtra("User", user);
            startActivity(intent);
        });

        search.setOnClickListener(view -> finish());

    }

    /**
     * Prompts the user to confirm event deletion and performs the deletion if confirmed.
     */
    private void confirmDeleteEvent() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this event? This action cannot be undone.")
                .setPositiveButton("Yes, Delete", (dialog, which) -> {

                    NotificationList notificationList = event.getNotificationList();
                    ArrayList<Notification> notifications = notificationList.getNotificationList();
                    //deletes notifications
                    for (int i = 0; i < notifications.size(); i++){
                        Notification current = notifications.get(i);
                        CRUD.delete(current.getDocumentId(), Notification.class, new DeleteCallback() {
                            @Override
                            public void onDeleteSuccess() {
                            }

                            @Override
                            public void onDeleteFailure(Exception e) {

                            }
                        });
                    }


                    //deletes applicant List
                    String applicant_list = event.getApplicantList();
                    if (!applicant_list.isEmpty()) {
                        CRUD.readStatic(applicant_list, ApplicantList.class, new ReadCallback<ApplicantList>() {
                            @Override
                            public void onReadSuccess(ApplicantList data) {

                                CRUD.delete(data.getDocumentId(), ApplicantList.class, new DeleteCallback() {
                                    @Override
                                    public void onDeleteSuccess() {

                                    }

                                    @Override
                                    public void onDeleteFailure(Exception e) {

                                    }
                                });
                            }

                            @Override
                            public void onReadFailure(Exception e) {

                            }
                        });
                    }



                    //deletes notification list
                    if (notificationList.getDocumentId() != null) {
                        CRUD.delete(notificationList.getDocumentId(), NotificationList.class, new DeleteCallback() {
                            @Override
                            public void onDeleteSuccess() {

                            }

                            @Override
                            public void onDeleteFailure(Exception e) {

                            }
                        });
                    }



                    CRUD.delete(event.getDocumentId(), Event.class, new DeleteCallback() {
                        @Override
                        public void onDeleteSuccess() {
                            Toast.makeText(EventInfo.this, "Event has been deleted", Toast.LENGTH_SHORT).show();
                            finish(); // Close EventInfo after deletion
                        }

                        @Override
                        public void onDeleteFailure(Exception e) {
                            Toast.makeText(EventInfo.this, "Error deleting event", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onEditSelected() {
        // Navigate to EditEvent activity
        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("Event", event);
        intent.putExtra("User", user);
        intent.putExtra("Facility", actual_facility);
        startActivity(intent);
    }

    @Override
    public void onDeleteSelected() {
        // Confirm deletion
        confirmDeleteEvent();
    }

}



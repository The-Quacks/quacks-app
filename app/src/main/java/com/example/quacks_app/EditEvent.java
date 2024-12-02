package com.example.quacks_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class EditEvent extends AppCompatActivity {
    private LocalDate start_date;
    private EditText event_name;
    private EditText class_capacity;
    private EditText waitlist_capacity;
    private EditText beginning;
    private EditText instructor;
    private CheckBox geolocation;
    private EditText description;
    private Facility facility;
    private User user;
    private EventList eventList;
    private Button upload_poster;
    private boolean validDate = false;
    private boolean validInstructorName = false;
    private boolean validEventName = false;
    private boolean wrong = false;
    private FirebaseFirestore db;
    private EditText eventtime;
    private Date final_date_time;
    private Event old_event;
    private int classes = 0;
    private int classes_two = 0;
    private Uri imageUri;

    // ActivityResultLauncher for handling the photo picker
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        // Update the title of the Activity based on the mode - Create Event / Edit Event
        TextView title = findViewById(R.id.title);
        title.setText(R.string.editing_event_details);

        //back and confirm buttons
        Button back = findViewById(R.id.back_button);
        Button confirm = findViewById(R.id.confirm_button);

        if (getIntent().getSerializableExtra("Event") == null) {
            finish();
        }
        old_event = (Event) getIntent().getSerializableExtra("Event");

        if (getIntent().getSerializableExtra("Facility") == null) {
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");

        if (getIntent().getSerializableExtra("User") == null) {
            finish();
        }
        user = (User) getIntent().getSerializableExtra("User");


        //Finding the right text box
        event_name = findViewById(R.id.event_name);
        class_capacity = findViewById(R.id.event_class_capacity);
        waitlist_capacity = findViewById(R.id.event_waitlist_capacity);
        beginning = findViewById(R.id.event_date);
        instructor = findViewById(R.id.event_instructor);
        geolocation = findViewById(R.id.event_geolocation);
        description = findViewById(R.id.event_description);
        eventtime = findViewById(R.id.event_time);

        //Setting the textboxes with old data
        event_name.setText(old_event.getEventName());
        class_capacity.setText(String.valueOf(old_event.getRegistrationCapacity()));
        waitlist_capacity.setText(String.valueOf(old_event.getWaitlistCapacity()));
        instructor.setText(old_event.getInstructor());
        description.setText(old_event.getDescription());
        geolocation.setChecked(old_event.getGeo());

        //getting the hour
        Date date = old_event.getDateTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String day_display = String.format("%02d-%02d-%04d", day, month, year);
        beginning.setText(day_display);

        int hours = calendar.get(Calendar.HOUR_OF_DAY); //24 hr format
        int minutes = calendar.get(Calendar.MINUTE);
        String display = "";

        if (hours > 12) {
            hours -= 12;
            display = String.format("%02d:%02dpm", hours, minutes);

        } else {
            display = String.format("%02d:%02dpm", hours, minutes);
        }
        eventtime.setText(display);

        Button updatePosterButton = findViewById(R.id.UpdatePoster);
        Button deleteEventButton = findViewById(R.id.event_delete_event);

        updatePosterButton.setOnClickListener(view -> {
            Intent intent = new Intent(EditEvent.this, UploadPosterActivity.class);
            intent.putExtra("EventList", eventList);
            intent.putExtra("User", user);
            intent.putExtra("Facility", facility);
            intent.putExtra("Event", old_event);
            startActivity(intent);
            finish();
        });

        deleteEventButton.setOnClickListener(view ->
                CRUD.delete(old_event.getDocumentId(), Event.class, new DeleteCallback() {
                    @Override
                    public void onDeleteSuccess() {
                        Toast.makeText(EditEvent.this, "Event has been deleted", Toast.LENGTH_SHORT).show();

                        // Navigate back to EventList
                        Intent intent = new Intent(EditEvent.this, EventList.class);
                        intent.putExtra("User", user); // Pass any necessary data to EventList
                        intent.putExtra("Facility", facility);
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void onDeleteFailure(Exception e) {
                        Toast.makeText(EditEvent.this, "Error deleting event", Toast.LENGTH_SHORT).show();
                    }
        }));

        back.setOnClickListener(view -> finish());

        confirm.setOnClickListener(view -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String startdate = beginning.getText().toString().trim();
                LocalDate current_date = LocalDate.now();

                start_date = LocalDate.parse(startdate, formatter);


                if (current_date.isBefore(start_date)) {
                    validDate = true;
                    wrong = false;
                }
            } catch (Exception e) {
                Toast.makeText(EditEvent.this, "Format Dates dd-mm-yyyy", Toast.LENGTH_SHORT).show();
            }


            // this checks the format of the **time** entered
            try {
                String hour = eventtime.getText().toString().trim();
                DateTimeFormatter formatted = DateTimeFormatter.ofPattern("h:mma");

                LocalTime eventTime = null;

                try {
                    eventTime = LocalTime.parse(hour.toUpperCase(), formatted);
                } catch (Exception E) {
                    wrong = true;
                    Toast.makeText(EditEvent.this, "Format Event Time 4:00pm", Toast.LENGTH_SHORT).show();
                }

                if (wrong) {
                    validDate = false;
                }

                if (start_date != null && eventTime != null) {
                    final_date_time = Date.from(
                            start_date.atTime(eventTime)
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                    );
                } else {
                    Toast.makeText(EditEvent.this, "Invalid date or time input.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception E) {
                validDate = false;
            }

            // this checks the format of the **class capacity**

            int classes = 0;
            try {
                classes = Integer.parseInt(class_capacity.getText().toString());
            } catch (Exception e) {
                wrong = true;
                Toast.makeText(EditEvent.this, "Error in Format for Class Capacity", Toast.LENGTH_SHORT).show();
            }

            //this checks the format of the **waitlist capacity**
            try {
                classes_two = Integer.parseInt(waitlist_capacity.getText().toString());
            } catch (Exception e) {
                wrong = true;
                Toast.makeText(EditEvent.this, "Error in format for Waitlist Capacity", Toast.LENGTH_SHORT).show();
            }


            // this checks the format of the **instructor name**
            String name = instructor.getText().toString();
            if (name.length() >= 1 && name.length() <= 40) {
                validInstructorName = true;
                wrong = false;
                //Toast.makeText(CreateEvent.this, "6 passed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditEvent.this, "Error: Instructor name needs to be between 1-40 characters", Toast.LENGTH_SHORT).show();
            }

            // this checks the format of the **Geolocation**

            Boolean geo = geolocation.isChecked();

            // this checks the format of the **event name**

            String eventname = event_name.getText().toString();
            if (eventname.length() <= 40) {
                validEventName = true;
                wrong = false;
                //Toast.makeText(CreateEvent.this, "8 passed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditEvent.this, "Event Name needs to be less than 40 characters", Toast.LENGTH_SHORT).show();
            }
            String text = description.getText().toString();

            // this is the case where all the tests pass
            if (validDate && validInstructorName && validEventName && !wrong) {
                old_event.setEventName(eventname);
                old_event.setDateTime(final_date_time);
                old_event.setDescription(text);
                old_event.setInstructor(name);
                old_event.setGeo(geo);
                old_event.setOrganizerId(user.getDocumentId());
                old_event.setFacility(facility.getDocumentId());
                old_event.setRegistrationCapacity(classes);
                old_event.setWaitlistCapacity(classes_two);

                // Upload the new event poster and save the event
                ImageUpload.uploadEventPosterToFirebase(this, imageUri, old_event, () -> {
                    // Save the event with the updated poster path
                    CRUD.createOrUpdate(old_event, new UpdateCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            Toast.makeText(EditEvent.this, "Event updated with new poster successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditEvent.this, EventInfo.class);
                            intent.putExtra("Facility", facility);
                            intent.putExtra("Event", old_event);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        }

                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(EditEvent.this, "Failed to update event with new poster.", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } else {
                Toast.makeText(EditEvent.this, "Validation Failed. Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

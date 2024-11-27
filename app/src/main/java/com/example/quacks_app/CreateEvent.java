package com.example.quacks_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
/**
 * Create Event allows the organizer to create an event and store it in the DB
 */
public class CreateEvent extends AppCompatActivity {
    private LocalDate startDate;

    private EditText event_name;
    private EditText class_capacity;
    private EditText waitlist_capacity;
    private EditText beginning;
    private EditText instructor;
    private EditText description;
    private EditText eventTime;

    private Date final_date_time;
    private CheckBox geolocation;
    private EventList eventList;
    private ImageView eventPoster;

    private Facility facility;
    private User user;
    private Event event;

    private boolean validDate = false;
    private boolean validInstructorName = false;
    private boolean validEventName = false;
    private boolean wrong = false;

    // ActivityResultLauncher for handling the photo picker
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        // Buttons
        Button upload_button = findViewById(R.id.upload_button);
        Button back = findViewById(R.id.back_button);
        Button confirm = findViewById(R.id.confirm_button);

        // Finding the right text box
        event_name = findViewById(R.id.event_name);
        class_capacity = findViewById(R.id.class_capacity);
        waitlist_capacity = findViewById(R.id.waitlist_capacity);
        beginning = findViewById(R.id.event_date);
        instructor = findViewById(R.id.instructor);
        geolocation = findViewById(R.id.geolocation);
        description = findViewById(R.id.description);
        eventTime = findViewById(R.id.event_time);
        eventPoster = findViewById(R.id.eventPoster);

        // Initialize other necessary objects
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        eventList = (EventList) getIntent().getSerializableExtra("EventList");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");
        initializePickImageLauncher();

        // On Click Listeners for Buttons
        upload_button.setOnClickListener(v ->
                requestStoragePermissionAndOpenGallery()
        );

        back.setOnClickListener(view -> finish());

        confirm.setOnClickListener(view -> {
            // Check the format of the day entered
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String startDateString = beginning.getText().toString().trim();
                LocalDate current_date = LocalDate.now();

                startDate = LocalDate.parse(startDateString, formatter);

                if (current_date.isBefore(startDate)) {
                    validDate = true;
                    wrong = false;
                }
            } catch (Exception e) {
                Toast.makeText(CreateEvent.this, "Format Dates dd-mm-yyyy", Toast.LENGTH_SHORT).show();
            }


            // Check the format of the time entered
            try {
                String hour = eventTime.getText().toString().trim();
                DateTimeFormatter formatted = DateTimeFormatter.ofPattern("h:mma");

                LocalTime eventTime = null;

                try {
                    eventTime = LocalTime.parse(hour.toUpperCase(), formatted);
                } catch (Exception E) {
                    wrong = true;
                    Toast.makeText(CreateEvent.this, "Format Event Time 4:00PM", Toast.LENGTH_SHORT).show();
                }

                if (wrong) {
                    validDate = false;
                }

                if (startDate != null && eventTime != null) {
                    final_date_time = Date.from(
                            startDate.atTime(eventTime)
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                    );
                } else {
                    Toast.makeText(CreateEvent.this, "Invalid date or time input.", Toast.LENGTH_SHORT).show();
                }
            } catch(Exception E) {
                validDate = false;
            }

            // Check the format of the **class capacity**
            int classes = 0;
            try {
                classes = Integer.parseInt(class_capacity.getText().toString());
            } catch(Exception e) {
                wrong = true;
                Toast.makeText(CreateEvent.this, "Error in Format for Class Capacity", Toast.LENGTH_SHORT).show();
            }

            // Check the format of the **waitlist capacity**
            int classes_two = 0;
            try {
                classes_two = Integer.parseInt(waitlist_capacity.getText().toString());

            }catch(Exception e){
                wrong = true;
                Toast.makeText(CreateEvent.this, "Error in Format for Waitlist Capacity", Toast.LENGTH_SHORT).show();
            }

            // Check the format of the **instructor name**
            String name = instructor.getText().toString();
            if (!name.isEmpty() && name.length() <= 40 ) {
                validInstructorName = true;
                wrong = false;
            } else {
                Toast.makeText(CreateEvent.this, "Error: Instructor name needs to be between 1-40 characters", Toast.LENGTH_SHORT).show();
            }

            // Check the format of the **Geolocation**
            boolean geo = geolocation.isChecked();

            // Check the format of the **event name**
            String eventname = event_name.getText().toString();
            if (eventname.length() <= 40) {
                validEventName = true;
                wrong = false;
            } else {
                Toast.makeText(CreateEvent.this, "Event name needs to be less than 40 characters", Toast.LENGTH_SHORT).show();
            }
            String text = description.getText().toString();

            if (!wrong && validDate && validInstructorName && validEventName) {
                event = new Event();
                event.setEventName(eventname);
                event.setDateTime(final_date_time);
                event.setDescription(text);
                event.setInstructor(name);
                event.setGeo(geo);
                event.setOrganizerId(user.getDocumentId());
                event.setFacility(facility.getDocumentId());
                event.setRegistrationCapacity(classes);
                event.setWaitlistCapacity(classes_two);

                if (eventList != null) {
                    eventList.addEvent(event);
                }

                CRUD.create(event, new CreateCallback() {
                    @Override
                    public void onCreateSuccess() {
                        Bitmap qrcode = QRCodeUtil.encode(event.getDocumentId(), 100, 100);
                        String hash = QRCodeUtil.hash(qrcode);
                        event.setQRCodeHash(hash);
                        CRUD.update(event, new UpdateCallback() {
                            @Override
                            public void onUpdateSuccess() {
                                Toast.makeText(CreateEvent.this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateEvent.this, QRCodeGeneratorActivity.class);
                                intent.putExtra("EventList", eventList);
                                intent.putExtra("User", user);
                                intent.putExtra("Facility", facility);
                                intent.putExtra("Event", event);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onUpdateFailure(Exception e) {
                                Toast.makeText(CreateEvent.this, "Failed to store qr code hash event.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCreateFailure(Exception e) {
                        Toast.makeText(CreateEvent.this, "Failed to create event.", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(CreateEvent.this, "Validation Failed. Please Try Again", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void initializePickImageLauncher() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Display the selected image on the ImageView
                        eventPoster.setImageURI(imageUri);

                        // Upload the image to Firebase

                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * Requests storage permission and opens the gallery if granted.
     */
    private void requestStoragePermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ImageUpload.openGallery(pickImageLauncher);
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                ImageUpload.openGallery(pickImageLauncher);
            }
        }
    }

//    private void saveUserProfileToFirestore() {
//        if (user != null && user.getDeviceId() != null && userProfile != null) {
//            user.setUserProfile(userProfile);
//            CRUD.update(user, new UpdateCallback() {
//                @Override
//                public void onUpdateSuccess() {
//                    Toast.makeText(ProfileActivity.this, "Profile picture updated in Firestore", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onUpdateFailure(Exception e) {
//                    Toast.makeText(ProfileActivity.this, "Failed to update profile picture in Firestore", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

}

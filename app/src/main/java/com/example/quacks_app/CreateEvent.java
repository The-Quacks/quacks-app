package com.example.quacks_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Create Event allows the organizer to create an event and store it in the DB
 */
public class CreateEvent extends AppCompatActivity {
    private Date start_date;
    private Date end_date;
    private EditText event_name;
    private EditText class_capacity;
    private EditText waitlist_capacity;
    private EditText beginning;
    private EditText end;
    private EditText instructor;
    private CheckBox geolocation;
    private EditText description;
    private Facility facility;
    private User user;
    private Button back;
    private Button confirm;
    private Event event;
    private ImageButton search;
    private ImageButton profile;
    private ImageButton homepage;
    private boolean validDate = false;
    private boolean validInstructorName = false;
    private boolean validEventName = false;
    private boolean wrong = false;
    private FirebaseFirestore db;
    private EventList eventList;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        back = findViewById(R.id.back_button);
        confirm = findViewById(R.id.confirm_button);
        if (getIntent().getSerializableExtra("Facility") == null) {
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (getIntent().getSerializableExtra("EventList") == null){
            finish();
        }
        eventList = (EventList) getIntent().getSerializableExtra("EventList");
        user = (User) getIntent().getSerializableExtra("User");



        //Then we set them like in create profile

        //Finding the right text box
        event_name = findViewById(R.id.event_name);
        class_capacity = findViewById(R.id.class_capacity);
        waitlist_capacity = findViewById(R.id.waitlist_capacity);
        beginning = findViewById(R.id.registration_start);
        end = findViewById(R.id.registration_end);
        instructor = findViewById(R.id.instructor);
        geolocation = findViewById(R.id.geolocation);
        description = findViewById(R.id.description);


        //buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String startDateString = beginning.getText().toString().trim();
                    String endDateString = end.getText().toString().trim();
                    LocalDate current_date = LocalDate.now();

                    LocalDate startDate = LocalDate.parse(startDateString, formatter);
                    LocalDate endDate = LocalDate.parse(endDateString, formatter);
                    start_date = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    end_date = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    if (current_date.isBefore(startDate) && startDate.isBefore(endDate)) {
                        validDate = true;

                    }
                } catch (Exception e) {
                    Toast.makeText(CreateEvent.this, "Format Dates dd-mm-yyyy", Toast.LENGTH_SHORT).show();
                }

                int classes = 0;
                try {
                    classes = Integer.parseInt(class_capacity.getText().toString());
                } catch(Exception e){
                    wrong = true;
                    Toast.makeText(CreateEvent.this, "Error in Format for Class Capacity", Toast.LENGTH_SHORT).show();
                }

                if (wrong) {
                    wrong = false;
                }

                int classes_two = 0;
                try {
                    classes_two = Integer.parseInt(waitlist_capacity.getText().toString());
                } catch(Exception e) {
                    wrong = true;
                    Toast.makeText(CreateEvent.this, "Error in Format for Waitlist Capacity", Toast.LENGTH_SHORT).show();
                }

                String name = instructor.getText().toString();
                if (name.length() >= 1 && name.length() <= 40 ) {
                    validInstructorName = true;
                    //Toast.makeText(CreateEvent.this, "6 passed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateEvent.this, "Error: Instructor name needs to be between 1-40 characters", Toast.LENGTH_SHORT).show();
                }

                boolean geo = geolocation.isChecked();


                String eventname = event_name.getText().toString();
                if (eventname.length() <= 40) {
                    validEventName = true;
                    //Toast.makeText(CreateEvent.this, "8 passed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateEvent.this, "Event name needs to be less than 40 characters", Toast.LENGTH_SHORT).show();
                }
                String text = description.getText().toString();

                if (!wrong && validDate && validInstructorName && validEventName) {
                    event = new Event();
                    event.setDateTime(start_date);
                    event.setEventName(eventname);
                    //event.setStartDateTime(start_date);
                    //event.setInstructor(name);
                    event.setGeolocationRequired(geo);
                    event.setDescription(text);
                    event.setOrganizerId(user.getDocumentId());
                    event.setFacility(facility.getDocumentId());

                    if (eventList != null) {
                        eventList.addEvent(event);
                    }


                    //Toast.makeText(CreateEvent.this, "It reaches the bottom", Toast.LENGTH_SHORT).show();
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

            }
        });

        //This is the bottom of the page directory
        homepage = findViewById(R.id.house);
        profile = findViewById(R.id.person);
        search = findViewById(R.id.search);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(CreateEvent.this, OrganizerHomepage.class);
                if (facility != null) {
                    intent.putExtra("Facility", facility);
                }
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateEvent.this, ViewOrganizer.class);
                if (facility != null) {
                    intent.putExtra("Facility", facility);
                }
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateEvent.this, ViewOrganizer.class);
                if (facility != null) {
                    intent.putExtra("Facility", facility);
                }
                startActivity(intent);
            }
        });
    }
}

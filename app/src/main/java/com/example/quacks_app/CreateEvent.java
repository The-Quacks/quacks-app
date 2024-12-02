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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

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
    private EditText eventtime;
    private Date final_date_time;
    private ApplicantList appList;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        back = findViewById(R.id.event_back_button);
        confirm = findViewById(R.id.event_confirm_button);
        if (getIntent().getSerializableExtra("Facility") == null) {
            finish();
        }

        if (getIntent().getSerializableExtra("User") == null){
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        //Then we set them like in create profile

        //Finding the right text box
        event_name = findViewById(R.id.event_name);
        class_capacity = findViewById(R.id.event_class_capacity);
        waitlist_capacity = findViewById(R.id.event_waitlist_capacity);
        beginning = findViewById(R.id.event_date);
        instructor = findViewById(R.id.event_instructor);
        geolocation = findViewById(R.id.event_geolocation);
        description = findViewById(R.id.event_description);
        eventtime = findViewById(R.id.event_time);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //this checks the format of the **day** entered
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


                // this checks the format of the **time** entered
                try {
                    String hour = eventtime.getText().toString().trim();
                    DateTimeFormatter formatted = DateTimeFormatter.ofPattern("h:mma");

                    LocalTime eventTime = null;

                    try {
                        eventTime = LocalTime.parse(hour.toUpperCase(), formatted);
                    } catch (Exception E) {
                        wrong = true;
                        Toast.makeText(CreateEvent.this, "Format Event Time 4:00pm", Toast.LENGTH_SHORT).show();
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


                // this checks the format of the **class capacity**

                int classes = 0;
                try {
                    classes = Integer.parseInt(class_capacity.getText().toString());
                } catch(Exception e) {
                    wrong = true;
                    Toast.makeText(CreateEvent.this, "Error in Format for Class Capacity", Toast.LENGTH_SHORT).show();
                }

                //this checks the format of the **waitlist capacity**
                int classes_two = 0;
                try {
                    classes_two = Integer.parseInt(waitlist_capacity.getText().toString());

                }catch(Exception e){
                    wrong = true;
                    Toast.makeText(CreateEvent.this, "Error in Format for Waitlist Capacity", Toast.LENGTH_SHORT).show();
                }

                // this checks the format of the **instructor name**
                String name = instructor.getText().toString();
                if (name.length() >= 1 && name.length() <= 40 ) {
                    validInstructorName = true;
                    wrong = false;
                    //Toast.makeText(CreateEvent.this, "6 passed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateEvent.this, "Error: Instructor name needs to be between 1-40 characters", Toast.LENGTH_SHORT).show();
                }

                // this checks the format of the **Geolocation**

                boolean geo = geolocation.isChecked();

                // this checks the format of the **event name**

                String eventname = event_name.getText().toString();
                if (eventname.length() <= 40) {
                    validEventName = true;
                    wrong = false;
                    //Toast.makeText(CreateEvent.this, "8 passed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateEvent.this, "Event name needs to be less than 40 characters", Toast.LENGTH_SHORT).show();
                }
                String text = description.getText().toString();

                if (!wrong && validDate && validInstructorName && validEventName) {
                    event = new Event();
                    appList = new ApplicantList();
                    appList.setLimit(classes);
                    event.setEventName(eventname);
                    event.setDateTime(final_date_time);
                    event.setDescription(text);
                    event.setInstructor(name);
                    event.setGeo(geo);
                    event.setOrganizerId(user.getDocumentId());
                    event.setFacility(facility.getDocumentId());
                    event.setRegistrationCapacity(classes);
                    event.setWaitlistCapacity(classes_two);
                    event.setFinal_list(null);

                    //Setting the notification list
                    String notificationListId = UUID.randomUUID().toString();
                    NotificationList notificationList = new NotificationList();
                    CRUD.create(notificationList, new CreateCallback() {
                        @Override
                        public void onCreateSuccess() {
                            notificationList.setNotificationListId(notificationListId);
                            CRUD.update(notificationList, new UpdateCallback() {
                                @Override
                                public void onUpdateSuccess() {
                                    event.setNotificationList(notificationList);
                                    //Toast.makeText(CreateEvent.this, "Notification List created successfully", Toast.LENGTH_SHORT).show();

                                    CRUD.update(event, new UpdateCallback() {
                                        @Override
                                        public void onUpdateSuccess() {
                                            //Toast.makeText(CreateEvent.this, "Notification List created successfully", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onUpdateFailure(Exception e) {

                                        }
                                    });
                                }

                                @Override
                                public void onUpdateFailure(Exception e) {
                                    Toast.makeText(CreateEvent.this, "Notification List was not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCreateFailure(Exception e) {
                            Toast.makeText(CreateEvent.this, "Notification list was not created", Toast.LENGTH_SHORT).show();
                        }
                    });


                    String uuid = UUID.randomUUID().toString();
                    event.setDocumentId(uuid);
                    event.setRegistration(false);

                    notificationList.setNotificationEventId(event.getEventId());

                    //Toast.makeText(CreateEvent.this, "It reaches the bottom", Toast.LENGTH_SHORT).show();
                    CRUD.create(appList, new CreateCallback() {
                        @Override
                        public void onCreateSuccess() {
                            event.setApplicantList(appList.getDocumentId());
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
                        }

                        @Override
                        public void onCreateFailure(Exception e) {

                        }
                    });


                } else {
                    Toast.makeText(CreateEvent.this, "Validation Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
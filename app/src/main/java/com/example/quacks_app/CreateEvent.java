package com.example.quacks_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Create Event allows the organizer to create an event and store it in the DB
 */
public class CreateEvent extends AppCompatActivity {
    private LocalDate start_date;
    private EditText event_name;
    private EditText class_capacity;
    private EditText waitlist_capacity;
    private EditText beginning;
    private EditText instructor;
    private EditText geolocation;
    private EditText description;
    private Facility facility;
    private User user;
    private Button back;
    private Button confirm;
    private Event event;
    private ImageButton search;
    private ImageButton profile;
    private ImageButton homepage;
    private int test_one = 0;
    private int test_two = 0;
    private int test_three = 0;
    private int test_five = 0;
    private int test_six = 0;
    private int test_seven = 0;
    private int test_eight = 0;
    private int wrong = 0;
    private FirebaseFirestore db;
    private EventList eventList;
    private EditText eventtime;
    private Date final_date_time;
    private Button upload_button;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        // buttons
        upload_button = findViewById(R.id.upload_button);
        back = findViewById(R.id.back_button);
        confirm = findViewById(R.id.confirm_button);

        if (getIntent().getSerializableExtra("Facility") == null) {
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (getIntent().getSerializableExtra("EventList")==null){
            finish();
        }
        eventList = (EventList) getIntent().getSerializableExtra("EventList");
        user = (User) getIntent().getSerializableExtra("User");

        //Then we set them like in create profile

        //Finding the right text box
        event_name = findViewById(R.id.event_name);
        class_capacity = findViewById(R.id.class_capacity);
        waitlist_capacity = findViewById(R.id.waitlist_capacity);
        beginning = findViewById(R.id.event_date);
        instructor = findViewById(R.id.instructor);
        geolocation = findViewById(R.id.geolocation);
        description = findViewById(R.id.description);
        eventtime = findViewById(R.id.event_time);





        //buttons
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateEvent.this, "Feature Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
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
                try{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String startdate = beginning.getText().toString().trim();
                    LocalDate current_date = LocalDate.now();

                    start_date = LocalDate.parse(startdate, formatter);


                    if (current_date.isBefore(start_date)){
                        test_one = 1;

                    }
                } catch (Exception e){
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
                        wrong = 1;
                        Toast.makeText(CreateEvent.this, "Format Event Time 4:00pm", Toast.LENGTH_SHORT).show();
                    }

                    if (wrong == 1) {
                        wrong = 0;
                    } else {
                        test_two = 1;
                    }

                    if (start_date != null && eventTime != null) {
                        Date date = Date.from(
                                start_date.atTime(eventTime)
                                        .atZone(ZoneId.systemDefault())
                                        .toInstant()
                        );
                        final_date_time = date;
                    } else {
                        Toast.makeText(CreateEvent.this, "Invalid date or time input.", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception E){
                    test_two = 0;
                }


                // this checks the format of the **class capacity**

                int classes = 0;
                try {
                    classes = Integer.parseInt(class_capacity.getText().toString());


                } catch(Exception e){
                    wrong = 1;
                    Toast.makeText(CreateEvent.this, "Error in Format for Class Capacity", Toast.LENGTH_SHORT).show();
                }

                if (wrong == 1){
                    wrong = 0;
                }else{
                    test_three = 1;
                    //Toast.makeText(CreateEvent.this, "3 passed", Toast.LENGTH_SHORT).show();
                }

                //this checks the format of the **waitlist capacity**
                int classes_two = 0;
                try {
                    classes_two = Integer.parseInt(waitlist_capacity.getText().toString());

                }catch(Exception e){
                    wrong = 1;
                    Toast.makeText(CreateEvent.this, "Error in Format for Waitlist Capacity", Toast.LENGTH_SHORT).show();
                }

                if (wrong != 1){
                    test_five = 1;
                    //Toast.makeText(CreateEvent.this, "5 passed", Toast.LENGTH_SHORT).show();
                }

                // this checks the format of the **instructor name**

                String name = instructor.getText().toString();
                if (name.length() >=1 && name.length() <= 40 ){
                    test_six = 1;
                    //Toast.makeText(CreateEvent.this, "6 passed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateEvent.this, "Error Instructor length needs to be between 1-40 characters", Toast.LENGTH_SHORT).show();
                }

                // this checks the format of the **Geolocation**

                String geo = geolocation.getText().toString();
                if (geo.contains("Disabled")){
                    test_seven = 1;
                    //Toast.makeText(CreateEvent.this, "7 passed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateEvent.this, "Geolocation has two options: Enabled/Disabled", Toast.LENGTH_SHORT).show();
                }
                if (geo.contains("Enabled")){
                    Toast.makeText(CreateEvent.this, "Enabled geolocation is not currently available.", Toast.LENGTH_SHORT).show();
                }

                // this checks the format of the **event name**

                String eventname = event_name.getText().toString();
                if (eventname.length() <= 40){
                    test_eight = 1;
                    //Toast.makeText(CreateEvent.this, "8 passed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateEvent.this, "Event Name needs to be less than 40 characters", Toast.LENGTH_SHORT).show();
                }
                String text = description.getText().toString();


                //this is the case where all the tests pass

                if (test_one == 1 && test_two == 1 && test_three == 1 && test_five == 1 && test_six == 1 && test_seven == 1&& test_eight == 1 ) {

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

                    if (eventList != null){
                        eventList.addEvent(event);
                    }


                    //Toast.makeText(CreateEvent.this, "It reaches the bottom", Toast.LENGTH_SHORT).show();
                    CRUD.create(event, new CreateCallback() {
                        @Override
                        public void onCreateSuccess() {
                            Toast.makeText(CreateEvent.this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                            // needs to save all the attributes.

                            Intent intent = new Intent(CreateEvent.this, ViewEvents.class);
                            intent.putExtra("EventList", eventList);
                            intent.putExtra("User", user);
                            intent.putExtra("Facility", facility);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCreateFailure(Exception e) {
                            Toast.makeText(CreateEvent.this, "Failed to create event.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
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

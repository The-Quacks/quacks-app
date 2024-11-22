package com.example.quacks_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.Date;

public class EditEvent extends AppCompatActivity {
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
    private Button upload_poster;
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
    private Event old_event;
    private int classes = 0;
    private int classes_two = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        //back and confirm buttons
        back = findViewById(R.id.edit_back_button);
        confirm = findViewById(R.id.edit_confirm_button);
        upload_poster = findViewById(R.id.edit_upload_button);

        //gets intents
        if (getIntent().getSerializableExtra("Facility") == null) {
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (getIntent().getSerializableExtra("EventList")==null){
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null){
            finish();
        }
        old_event = (Event) getIntent().getSerializableExtra("Event");


        eventList = (EventList) getIntent().getSerializableExtra("EventList");
        user = (User) getIntent().getSerializableExtra("User");

        //Finding the right text box
        event_name = findViewById(R.id.editted_event_name);
        class_capacity = findViewById(R.id.editted_class_capacity);
        waitlist_capacity = findViewById(R.id.editted_waitlist_capacity);
        beginning = findViewById(R.id.editted_event_date);
        instructor = findViewById(R.id.editted_instructor);
        geolocation = findViewById(R.id.editted_geolocation);
        description = findViewById(R.id.editted_description);
        eventtime = findViewById(R.id.editted_event_time);

        //Setting the textboxes with old data
        event_name.setText(old_event.getEventName());
        class_capacity.setText(old_event.getRegistrationCapacity());
        waitlist_capacity.setText(old_event.getWaitlistCapacity());
        beginning.setText(old_event.getDateTime().toString());
        instructor.setText(old_event.getInstructor());
        geolocation.setText(old_event.getGeo());
        description.setText(old_event.getDescription());

        //getting the event time
        Date date = old_event.getDateTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hours = calendar.get(Calendar.HOUR_OF_DAY); //24 hr format
        int minutes = calendar.get(Calendar.MINUTE);
        String display = "";

        if (hours > 12){
            hours -= 12;
            display = String.format("%02d:%02dpm", hours, minutes);

        }else{
            display = String.format("%02d:%02dpm", hours, minutes);
        }
        eventtime.setText(display);

        //buttons
        upload_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditEvent.this, "Event Poster Coming Soon", Toast.LENGTH_SHORT).show();

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
                        wrong = 1;
                        Toast.makeText(EditEvent.this, "Format Event Time 4:00pm", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditEvent.this, "Invalid date or time input.", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception E){
                    test_two = 0;
                }


                // this checks the format of the **class capacity**

                try {
                    classes = Integer.parseInt(class_capacity.getText().toString());


                } catch(Exception e){
                    wrong = 1;
                    Toast.makeText(EditEvent.this, "Error in Format for Class Capacity", Toast.LENGTH_SHORT).show();
                }

                if (wrong == 1){
                    wrong = 0;
                }else{
                    test_three = 1;
                    //Toast.makeText(CreateEvent.this, "3 passed", Toast.LENGTH_SHORT).show();
                }

                //this checks the format of the **waitlist capacity**
                try {
                    classes_two = Integer.parseInt(waitlist_capacity.getText().toString());

                }catch(Exception e){
                    wrong = 1;
                    Toast.makeText(EditEvent.this, "Error in Format for Waitlist Capacity", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditEvent.this, "Error Instructor length needs to be between 1-40 characters", Toast.LENGTH_SHORT).show();
                }

                // this checks the format of the **Geolocation**

                String geo = geolocation.getText().toString();
                if (geo.contains("Disabled")){
                    test_seven = 1;
                    //Toast.makeText(CreateEvent.this, "7 passed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditEvent.this, "Geolocation has two options: Enabled/Disabled", Toast.LENGTH_SHORT).show();
                }
                if (geo.contains("Enabled")){
                    Toast.makeText(EditEvent.this, "Enabled geolocation is not currently available.", Toast.LENGTH_SHORT).show();
                }

                // this checks the format of the **event name**

                String eventname = event_name.getText().toString();
                if (eventname.length() <= 40){
                    test_eight = 1;
                    //Toast.makeText(CreateEvent.this, "8 passed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditEvent.this, "Event Name needs to be less than 40 characters", Toast.LENGTH_SHORT).show();
                }
                String text = description.getText().toString();


                //this is the case where all the tests pass

                if (test_one == 1 && test_two == 1 && test_three == 1 && test_five == 1 && test_six == 1 && test_seven == 1&& test_eight == 1 ) {

                    CRUD.update(old_event, new UpdateCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            old_event.setEventName(eventname);
                            old_event.setDateTime(final_date_time);
                            old_event.setDescription(text);
                            old_event.setInstructor(name);
                            old_event.setGeo(geo);
                            old_event.setOrganizerId(user.getDocumentId());
                            old_event.setFacility(facility.getDocumentId());
                            old_event.setRegistrationCapacity(classes);
                            old_event.setWaitlistCapacity(classes_two);
                            CRUD.create(old_event, new CreateCallback() {
                                @Override
                                public void onCreateSuccess() {
                                    Toast.makeText(EditEvent.this, "Event Created!", Toast.LENGTH_SHORT).show();
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("Event", old_event);
                                    resultIntent.putExtra("Facility", facility);
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }
                                @Override
                                public void onCreateFailure(Exception e) {
                                }
                            });
                        }
                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(EditEvent.this, "Error creating event, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(EditEvent.this, "Validation Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });




        //This is the bottom of the page directory
        homepage = findViewById(R.id.edit_house);
        profile = findViewById(R.id.edit_person);
        search = findViewById(R.id.edit_search);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(EditEvent.this, OrganizerHomepage.class);
                if (facility != null) {
                    intent.putExtra("Facility", facility);
                }
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditEvent.this, ViewOrganizer.class);
                if (facility != null) {
                    intent.putExtra("Facility", facility);
                }
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditEvent.this, ViewOrganizer.class);
                if (facility != null) {
                    intent.putExtra("Facility", facility);
                }
                startActivity(intent);
            }
        });


    }

}

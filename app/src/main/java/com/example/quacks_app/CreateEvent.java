package com.example.quacks_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Create Event allows the organizer to create an event and store it in the DB
 */
public class CreateEvent extends AppCompatActivity {
    private EventList eventList;
    private EditText event_name;
    private EditText class_capacity;
    private EditText waitlist_capacity;
    private EditText beginning;
    private EditText end;
    private EditText instructor;
    private EditText geolocation;
    private Facility facility;
    private Button back;
    private Button confirm;
    private Event event;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        event = new Event();
        eventList = new EventList();
        back = findViewById(R.id.back_button);
        confirm = findViewById(R.id.confirm_button);
        facility = (Facility) getIntent().getSerializableExtra("Facility");

        //Then we set them like in create profile

        //Finding the right text box
        event_name = findViewById(R.id.event_name);
        class_capacity = findViewById(R.id.class_capacity);
        waitlist_capacity = findViewById(R.id.waitlist_capacity);
        beginning = findViewById(R.id.registration_start);
        end = findViewById(R.id.registration_end);
        instructor = findViewById(R.id.instructor);
        geolocation = findViewById(R.id.geolocation);




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

                Date start_date = stringToDate(beginning.getText().toString());
                event.setStartDateTime(start_date);

                Date end_date = stringToDate(end.getText().toString());
                //testing date follows right format
                event.setEndDateTime(end_date);

                try {
                    int classes = Integer.parseInt(class_capacity.getText().toString());
                    event.setClass_capacity(classes);
                }catch(Exception e){
                    Toast.makeText(CreateEvent.this, "Error in Format for Class Capacity", Toast.LENGTH_SHORT).show();
                }

                try {
                    int classes = Integer.parseInt(waitlist_capacity.getText().toString());
                    event.setWaitlist_capacity(classes);
                }catch(Exception e){
                    Toast.makeText(CreateEvent.this, "Error in Format for Waitlist Capacity", Toast.LENGTH_SHORT).show();
                }

                event.setInstructor(instructor.getText().toString());
                event.setGeolocation(geolocation.getText().toString());
                event.setEventName(event_name.getText().toString());
                event.setFacility(facility);

                //EventList.
                eventList.addEvent(event);

                Toast.makeText(CreateEvent.this, "Event Created!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * This was to convert to set the event correctly according to database
     * @param text
     * @return
     */
    private Date stringToDate(String text) {
        Date d = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            d = dateFormat.parse(text);
        } catch (ParseException e) {
            // Handle the exception if necessary
            d = null;
        }
        return d;
    }


}

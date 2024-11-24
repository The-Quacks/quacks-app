package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventInfo extends AppCompatActivity {
    private TextView organizer;
    private TextView date;
    private TextView description;
    private TextView facility;
    private TextView id;
    private String applicantList;
    private EventList eventList;
    private Button open_registration;
    private Button close_registration;
    private Button delete_event;
    private Button entrant_map;
    private ImageButton profile;
    private ImageButton search;
    private ImageButton homepage;
    private Facility actual_facility;
    private TextView eventname;
    private TextView instructor_name;
    private TextView waitlist_capacity;
    private TextView registration_capacity;
    private CheckBox geolocation;
    private User user;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);


        if (getIntent().getSerializableExtra("Event") == null) {
            finish();
        } else {
            event = (Event) getIntent().getSerializableExtra("Event");
        }


        if (getIntent().getSerializableExtra("Facility") != null) {
            actual_facility = (Facility) getIntent().getSerializableExtra("Facility");
        }

        user = (User) getIntent().getSerializableExtra("User");

        //these are info fields
        eventname = findViewById(R.id.event_name);
        instructor_name = findViewById(R.id.instructor);
        geolocation = findViewById(R.id.geolocation_status);
        date = findViewById(R.id.event_date);
        description = findViewById(R.id.event_description);
        facility = findViewById(R.id.event_facility);
        organizer = findViewById(R.id.event_organizer);
        id = findViewById(R.id.event_id);
        waitlist_capacity = findViewById(R.id.waitlist_capacity);
        registration_capacity = findViewById(R.id.class_capacity);

        //these are buttons
        open_registration = findViewById(R.id.register);
        close_registration = findViewById(R.id.close);
        delete_event = findViewById(R.id.delete_button);
        entrant_map = findViewById(R.id.map);


        String text = event.getDescription();
        geolocation.setChecked(event.getGeo());
        geolocation.setClickable(false);
        String waitlist_text = String.valueOf(event.getWaitlistCapacity());
        String capacity_text = String.valueOf(event.getRegistrationCapacity());
        String inst_name = event.getInstructor();
        String event_name = event.getEventName();

        Date dated = event.getDateTime();
        Facility fac = actual_facility;
        String name = "";
        if (facility != null) {
            name = fac.getName();
        }
        String organizerid = event.getOrganizerId();
        eventname.setText(event_name);
        instructor_name.setText(inst_name);
        registration_capacity.setText(waitlist_text);
        waitlist_capacity.setText(capacity_text);

        date.setText(dated.toString());
        description.setText(text);
        facility.setText(name);
        organizer.setText(organizerid);
        id.setText(event.getEventId());

        open_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //makes an applicant list for that event.
                Intent intent = new Intent(EventInfo.this, OpenRegistration.class);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        close_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfo.this, ApplicantOptions.class);
                intent.putExtra("Event", event);
                startActivity(intent);

            }
        });

        entrant_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(EventInfo.this, EditEvent.class);
                intent.putExtra("Event", event);
                intent.putExtra("User", user);
                intent.putExtra("Facility", actual_facility);
                startActivity(intent);
            }
        });

        delete_event.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  CRUD.delete(event.getDocumentId(), Event.class, new DeleteCallback() {
                      @Override
                      public void onDeleteSuccess() {
                          Toast.makeText(EventInfo.this, "Event has been deleted", Toast.LENGTH_SHORT).show();
                          finish();
                      }

                      @Override
                      public void onDeleteFailure(Exception e) {
                          Toast.makeText(EventInfo.this, "Error deleting event", Toast.LENGTH_SHORT).show();
                      }
                  });
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
                Intent intent = new Intent(EventInfo.this, OrganizerHomepage.class);
                if (facility != null) {
                    intent.putExtra("Facility", actual_facility);
                }
                intent.putExtra("User", user);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfo.this, ViewOrganizer.class);
                if (facility != null) {
                    intent.putExtra("Facility", actual_facility);
                }
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }

}



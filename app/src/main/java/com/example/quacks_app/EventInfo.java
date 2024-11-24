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

import java.util.Date;

public class EventInfo extends AppCompatActivity {
    private String applicantList;
    private EventList eventList;
    private Facility actual_facility;
    private User user;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);

        event = (Event) getIntent().getSerializableExtra("Event");
        if (event == null){
            Toast.makeText(this, "Error: Event data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (getIntent().getSerializableExtra("User") == null){
            finish();
        }
        if (getIntent().getSerializableExtra("Facility") != null) {
            actual_facility = (Facility) getIntent().getSerializableExtra("Facility");
        }

        eventList = (EventList) getIntent().getSerializableExtra("EventList");

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

        // Buttons
        Button open_registration = findViewById(R.id.register);
        Button close_registration = findViewById(R.id.close);
        Button delete_event = findViewById(R.id.delete_button);
        Button applicant_lists = findViewById(R.id.map);

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

        open_registration.setOnClickListener(view -> {
            //makes an applicant list for that event.
            Intent intent = new Intent(EventInfo.this, OpenRegistration.class);
            intent.putExtra("Event", event);
            intent.putExtra("User", user);
            intent.putExtra("Facility", actual_facility);
            startActivity(intent);
        });

        close_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfo.this, ApplicantOptions.class);
                intent.putExtra("Event", event);
                intent.putExtra("User", user);
                intent.putExtra("Facility", actual_facility);
                startActivity(intent);
        }});

        applicant_lists.setOnClickListener(new View.OnClickListener() {
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

}



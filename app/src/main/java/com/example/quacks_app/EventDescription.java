package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDescription extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        String id = getIntent().getStringExtra("id");
        CRUD<Event> crud = new CRUD<>(Event.class);
        ReadCallback<Event> readCallback = new ReadCallback<Event>() {
            @Override
            public void onReadSuccess(Event data) {
                TextView eventTitle = findViewById(R.id.eventTitle);
                eventTitle.setText(data.getDescription());
                // This will need to be updated after merging with the full Event implementation
//                eventDescription.setText(String.format("Capacity: %s\nRegistration open until: %s\nClass Duration: %s\nInstructor: %s\nGeolocation Requirement: %s", data.getClass_capacity(), data.getStartDateTime().toString(), data.getEndDateTime().toString(), data.getInstructor(), data.getGeolocation()));
                TextView eventDescription = findViewById(R.id.eventDescription);
                eventDescription.setText(String.format("Applicant List: %s\nDateTime: %s\nFacility: %s\nOrganizer: %s", data.getApplicantList(), data.getDateTime(), data.getFacility(), data.getOrganizerId()));
            }
            @Override
            public void onReadFailure(Exception e) {
                TextView eventDescription = findViewById(R.id.eventDescription);
                eventDescription.setText("This ain't workin");
                Log.e("EventDescription", "Read failed", e);
            }
        };

        crud.readStatic(id, readCallback);

        ImageButton back = findViewById(R.id.backButton);
        ImageButton home = findViewById(R.id.homeIcon);
        Button joinWaitlist = findViewById(R.id.joinWaitlistButton);

        back.setOnClickListener(v -> {
            finish();
        });

        home.setOnClickListener(v -> {
            startActivity(new Intent(this, EntrantHome.class));
        });


//        joinWaitlist.setOnClickListener(v -> {
//            join the waitlist
//        });
    }

}

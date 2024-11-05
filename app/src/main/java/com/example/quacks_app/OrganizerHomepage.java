package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the Organizer Homepage: they can choose out of the four options
 * Edit Profile, View Events, Create Events, Entrant Map
 */
public class OrganizerHomepage extends AppCompatActivity {
    private Button organizer_profile;
    private Button view_events;
    private Button create_events;
    private Button entrant_map;
    private Facility facility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_homepage);// or the correct XML layout file

        //Getting Current Facility
        facility = (Facility) getIntent().getSerializableExtra("Facility");

        organizer_profile = findViewById(R.id.organizer_profile_button);
        view_events = findViewById(R.id.view_events);
        create_events = findViewById(R.id.create_event);
        entrant_map = findViewById(R.id.map);

        organizer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goes to the VIEW ORGANIZER PAGE
                Intent intent = new Intent(OrganizerHomepage.this, ViewOrganizer.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        view_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GOES TO THE EVENT LIST PAGE
            }
        });

        create_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goes to the CREATE EVENT page
                Intent intent = new Intent(OrganizerHomepage.this, CreateEvent.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        entrant_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goes to ENTRANT map page
                Toast.makeText(OrganizerHomepage.this, "Entrant Map Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

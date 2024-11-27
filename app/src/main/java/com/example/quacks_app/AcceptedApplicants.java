package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AcceptedApplicants extends AppCompatActivity {
    private ImageButton search;
    private ImageButton homepage;
    private ImageButton profile;
    private Button back;
    private Facility facility;
    private User user;
    private Event event;
    private Button select;
    private Button notify_all;
    /**
     * This is the list that holds all accepted applicants
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accepted_applicants);

        if (getIntent().getSerializableExtra("Facility") == null){
            Toast.makeText(AcceptedApplicants.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null){
            Toast.makeText(AcceptedApplicants.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(AcceptedApplicants.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }
        //if (getIntent().getSerializableExtra("EventList") == null){
        //    Toast.makeText(AcceptedApplicants.this, "No EventList", Toast.LENGTH_SHORT).show();
        //    finish();
        //}
        //eventList = (EventList) getIntent().getSerializableExtra("EventList");
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");

        select = findViewById(R.id.accepted_select_button);
        notify_all = findViewById(R.id.accepted_notify_button);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedApplicants.this, NotifyOptions.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        notify_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AcceptedApplicants.this, "This feature is coming soon!", Toast.LENGTH_SHORT).show();
            }
        });


        back = findViewById(R.id.accepted_back_button);
        search = findViewById(R.id.accepted_app_search);
        homepage = findViewById(R.id.accepted_app_house);
        profile = findViewById(R.id.accepted_app_person);

        //create buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedApplicants.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedApplicants.this, ViewOrganizer.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(AcceptedApplicants.this, ViewEvents.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                //intent.putExtra("EventList", eventList);
                startActivity(intent);

            }
        });




    }
}

package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeclinedApplicants extends AppCompatActivity {
    private Button back;
    private ImageButton search;
    private ImageButton profile;
    private ImageButton homepage;
    private Facility facility;
    private User user;
    private Button notify_all;
    private Button notify_options;
    private Event event;

    /**
     * This is the list that holds all declined applicants
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.declined_applicants);

        if (getIntent().getSerializableExtra("Facility") == null){
            Toast.makeText(DeclinedApplicants.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null){
            Toast.makeText(DeclinedApplicants.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(DeclinedApplicants.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");

        notify_all = findViewById(R.id.declined_notify_button);
        notify_options = findViewById(R.id.declined_select_button);

        notify_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeclinedApplicants.this, "This feature is coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        notify_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclinedApplicants.this, NotifyOptions.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        search = findViewById(R.id.declined_app_search);
        homepage = findViewById(R.id.declined_app_house);
        profile = findViewById(R.id.declined_app_person);

        back = findViewById(R.id.declined_back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclinedApplicants.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclinedApplicants.this, ViewOrganizer.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(DeclinedApplicants.this, ViewEvents.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

    }
}
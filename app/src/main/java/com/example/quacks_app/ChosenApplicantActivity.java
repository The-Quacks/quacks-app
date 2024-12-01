package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChosenApplicantActivity extends AppCompatActivity {
    private ListView applicantListView;
    private Cartable userdisplay;
    private ArrayList<Cartable> userList;
    private ArrayList<User> real_user;
    private ApplicantArrayAdapter applicantArrayAdapter;
    private Button back;
    private Facility facility;
    private User user;
    private Button notify_all;
    private Event event;

    /**
     * This is the list that holds all declined applicants
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_applicant);

        if (getIntent().getSerializableExtra("Facility") == null){
            Toast.makeText(ChosenApplicantActivity.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null){
            Toast.makeText(ChosenApplicantActivity.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(ChosenApplicantActivity.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");

        assert event != null;
        if (event.getApplicantList().equals("0")){
            Toast.makeText(ChosenApplicantActivity.this, "Please Open Registration!", Toast.LENGTH_SHORT).show();
            finish();

        }

        notify_all = findViewById(R.id.chosen_notify_button);

        notify_all.setOnClickListener(view ->
                Toast.makeText(ChosenApplicantActivity.this, "This feature is coming soon!", Toast.LENGTH_SHORT).show()
        );

        // Setting up list

        applicantListView = findViewById(R.id.chosen_app_list);
        real_user = new ArrayList<User>();
        userList = new ArrayList<Cartable>();
        applicantArrayAdapter = new ApplicantArrayAdapter(this, userList);
        applicantListView.setAdapter(applicantArrayAdapter);

        if (event == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // To do for josie

        back = findViewById(R.id.chosen_back_button);
        back.setOnClickListener(view -> finish());

        // Bottom App Navigation
        ImageButton search = findViewById(R.id.app_search);
        ImageButton homepage = findViewById(R.id.app_house);
        ImageButton profile = findViewById(R.id.app_person);

        homepage.setOnClickListener(view -> {
            Intent intent = new Intent(ChosenApplicantActivity.this, OrganizerHomepage.class);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(ChosenApplicantActivity.this, ViewOrganizer.class);
            intent.putExtra("User", user);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });

        search.setOnClickListener(view -> {
            //Already here
            Intent intent = new Intent(ChosenApplicantActivity.this, ViewEvents.class);
            intent.putExtra("User", user);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });
    }
}
package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ApplicantOptions extends AppCompatActivity {
    private Button all_applicants;
    private Button declined_applicants;
    private Button accepted_applicants;
    private Button back;
    private Facility facility;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicant_options);


        if (getIntent().getSerializableExtra("Facility") == null){
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");

        if (getIntent().getSerializableExtra("User")==null){
            finish();
        }
        user = (User) getIntent().getSerializableExtra("User");

        if (getIntent().getSerializableExtra("Event") == null){
            finish();
        }
        Event event = (Event) getIntent().getSerializableExtra("Event");

        //otherwise
        all_applicants = findViewById(R.id.all_button);
        declined_applicants = findViewById(R.id.declined_button);
        accepted_applicants = findViewById(R.id.accepted_button);
        back = findViewById(R.id.applicant_back_button);

        //create buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        all_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApplicantOptions.this, AllApplicants.class);
                intent.putExtra("Event", event);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        declined_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApplicantOptions.this, DeclinedApplicants.class);
                intent.putExtra("Event", event);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        accepted_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApplicantOptions.this, AcceptedApplicants.class);
                intent.putExtra("Event", event);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicant_options);

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
                startActivity(intent);
            }
        });

        declined_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ApplicantOptions.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        accepted_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ApplicantOptions.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



    }
}

package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The {@code ApplicantOptions} class represents a screen in the Quacks app
 * where users can choose different categories of applicants to view, such as
 * all applicants, accepted applicants, declined applicants, cancelled applicants,
 * or finalized applicants for a specific event.
 *
 * <p>This class handles navigation to the respective applicant list screens based
 * on user selection.</p>
 */
public class ApplicantOptions extends AppCompatActivity {
    private Button all_applicants;
    private Button declined_applicants;
    private Button accepted_applicants;
    private Button back;
    private Button cancelled_applicants;
    private Button finalized_applicants;
    private Facility facility;
    private User user;

    /**
     * Initializes the activity, sets up the UI components, and handles navigation
     * to different applicant categories.
     *
     * @param savedInstanceState {@code null} if the activity is being created for the first time.
     */
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
        cancelled_applicants = findViewById(R.id.cancelled_button);
        finalized_applicants = findViewById(R.id.finalized_button);
        back = findViewById(R.id.applicant_back_button);

        //create buttons
        back.setOnClickListener(view -> finish());

        all_applicants.setOnClickListener(view -> {
            Intent intent = new Intent(ApplicantOptions.this, AllApplicants.class);
            intent.putExtra("Event", event);
            intent.putExtra("Facility", facility);
            intent.putExtra("User", user);
            startActivity(intent);
        });

        declined_applicants.setOnClickListener(view -> {
            Intent intent = new Intent(ApplicantOptions.this, DeclinedApplicants.class);
            intent.putExtra("Event", event);
            intent.putExtra("Facility", facility);
            intent.putExtra("User", user);
            startActivity(intent);
        });

        accepted_applicants.setOnClickListener(view -> {
            Intent intent = new Intent(ApplicantOptions.this, AcceptedApplicants.class);
            intent.putExtra("Event", event);
            intent.putExtra("Facility", facility);
            intent.putExtra("User", user);
            startActivity(intent);
        });

        cancelled_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApplicantOptions.this, CancelledApplicants.class);
                intent.putExtra("Event", event);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        finalized_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApplicantOptions.this, FinalizedApplicants.class);
                intent.putExtra("Event", event);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}

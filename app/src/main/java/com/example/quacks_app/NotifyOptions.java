package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The {@code NotifyOptions} class provides a user interface for event organizers
 * to choose how to notify applicants about an event. Organizers can either randomly select
 * applicants or manually pick them from the list.
 */
public class NotifyOptions extends AppCompatActivity {
    private Button pool_applicants;
    private Button select_applicants;
    private Button back;
    private Event event;
    private User user;
    private Facility facility;

    /**
     * Called when the activity is first created. Initializes UI components
     * and sets up navigation between different notification options.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the most recent data. Otherwise, it is {@code null}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_options);

        pool_applicants = findViewById(R.id.random_button);
        select_applicants = findViewById(R.id.pick_button);
        back =findViewById(R.id.back_button);

        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(NotifyOptions.this, "No Event Passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        event = (Event) getIntent().getSerializableExtra("Event");

        if (getIntent().getSerializableExtra("Facility") == null){
            Toast.makeText(NotifyOptions.this, "No Facility Passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (getIntent().getSerializableExtra("User") == null){
            Toast.makeText(NotifyOptions.this, "No User Passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        user = (User) getIntent().getSerializableExtra("User");



        pool_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotifyOptions.this, SelectAtRandom.class);
                intent.putExtra("Event", event);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        select_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotifyOptions.this, PickApplicant.class);
                intent.putExtra("Event", event);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}

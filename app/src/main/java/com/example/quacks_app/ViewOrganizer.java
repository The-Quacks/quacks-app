package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewOrganizer extends AppCompatActivity {
    private Button back;
    private Button edit;
    private TextView name;
    private TextView location;
    private TextView contact_info;
    private TextView details;
    private TextView accessibility;
    private ImageButton homepage;
    private ImageButton search;
    private ImageButton profile;

    private Facility facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_organizer_profile);// or the correct XML layout file

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (facility != null) {
            //setting textviews
            name = findViewById(R.id.Name);
            location = findViewById(R.id.location);
            contact_info = findViewById(R.id.contact_info);
            details = findViewById(R.id.facility_details);
            accessibility = findViewById(R.id.accessibility);

            name.setText(facility.getName());
            location.setText(facility.getLocation());
            contact_info.setText(facility.getContactInfo());
            details.setText(facility.getDetails());
            accessibility.setText(facility.getAccessible());
        }

        edit = findViewById(R.id.edit_button);
        back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to homepage
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this is the edit function
                Intent intent = new Intent(ViewOrganizer.this, MakeOrganizerProfile.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });


        //This is the bottom of the page directory
        homepage = findViewById(R.id.house);
        profile = findViewById(R.id.person);
        search = findViewById(R.id.search);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOrganizer.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewOrganizer.this, "Already On Profile ViewPage!", Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(ViewOrganizer.this, ViewEvents.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

    }


}

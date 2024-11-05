package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewOrganizer extends AppCompatActivity {
    private Button back;
    private Button edit;
    private TextView name;
    private TextView location;
    private TextView contact_info;
    private TextView details;
    private TextView accessibility;

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
            accessibility.setText(facility.accessibilityStat());
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

    }


}

package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MakeOrganizerProfile extends AppCompatActivity {// or the correct XML layout file
    private Button back;
    private Button confirm;
    private Facility facility;
    private EditText bussiness_name;
    private EditText location;
    private EditText contact_info;
    private EditText phone_number;
    private EditText facility_deets;
    private EditText accessibility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_organizer_profile);// or the correct XML layout file

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (facility != null) {
            //setting textviews
            bussiness_name = findViewById(R.id.Name);
            location = findViewById(R.id.location);
            contact_info = findViewById(R.id.contact_info);
            facility_deets = findViewById(R.id.facility_details);
            accessibility = findViewById(R.id.accessibility);

            bussiness_name.setText(facility.getName());
            location.setText(facility.getLocation());
            contact_info.setText(facility.getContactInfo());
            facility_deets.setText(facility.getDetails());
            accessibility.setText(facility.accessibilityStat());
        }


        //on back click nothing is saved
        back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //on confirm click

                bussiness_name = findViewById(R.id.Name);
                location = findViewById(R.id.location);
                phone_number = findViewById(R.id.contact_info);
                facility_deets = findViewById(R.id.facility_details);
                accessibility = findViewById(R.id.accessibility);

                facility.setName(bussiness_name.getText().toString());
                facility.setLocation(location.getText().toString());
                facility.setContactInfo(phone_number.getText().toString());
                facility.setDetails(facility_deets.getText().toString());
                facility.setaccessibilityStat(accessibility.getText().toString());
                Toast.makeText(MakeOrganizerProfile.this, "Facility Updated!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MakeOrganizerProfile.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });
    }
}


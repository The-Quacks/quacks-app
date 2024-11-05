package com.example.quacks_app;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import java.security.AccessControlContext;

public class CreateFacility extends AppCompatActivity {
    private EditText bussiness_name;
    private EditText location;
    private EditText phone_number;
    private EditText facility_deets;
    private EditText accessibility;
    private Facility new_facility;
    private Button confirm;
    private Button back;

    /**
     * This is where they can create a Facility Page
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_facility);// or the correct XML layout file


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
                new_facility = new Facility();

                bussiness_name = findViewById(R.id.Name);
                location = findViewById(R.id.location);
                phone_number = findViewById(R.id.contact_info);
                facility_deets = findViewById(R.id.facility_details);
                accessibility = findViewById(R.id.accessibility);

                new_facility.setName(bussiness_name.getText().toString());
                new_facility.setLocation(location.getText().toString());
                new_facility.setContactInfo(phone_number.getText().toString());
                new_facility.setDetails(facility_deets.getText().toString());
                new_facility.setaccessibilityStat(accessibility.getText().toString());

                // we want to send the result of completed facility back to main activity
                // sort of like a finish().putExtra but more legal

                Intent result = new Intent();
                result.putExtra("Facility", new_facility);
                setResult(RESULT_OK, result);
                finish();


            }
        });

    }
}

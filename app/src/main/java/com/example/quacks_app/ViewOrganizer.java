package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewOrganizer extends AppCompatActivity {
    private Button back;
    private Button edit;
    private TextView name;
    private TextView location;
    private TextView contact_info;
    private TextView details;
    private TextView accessibility;
    private TextView emailed;
    private TextView usernamed;
    private User current;
    private Facility facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_organizer_profile);// or the correct XML layout file


        current = (User) getIntent().getSerializableExtra("User");
        facility = (Facility) getIntent().getSerializableExtra("Facility");


        name = findViewById(R.id.Name);
        location = findViewById(R.id.location);
        contact_info = findViewById(R.id.contact_info);
        details = findViewById(R.id.facility_details);
        accessibility = findViewById(R.id.accessibility);
        emailed = findViewById(R.id.email);
        usernamed = findViewById(R.id.username);


        edit = findViewById(R.id.edit_button);
        back = findViewById(R.id.back_button);



        name.setText(facility.getName());
        location.setText(facility.getLocation());
        contact_info.setText(facility.getPhone());
        details.setText(facility.getDetails());
        accessibility.setText(facility.getAccessible());
        UserProfile userProfile = current.getUserProfile();
        emailed.setText(userProfile.getEmail());
        usernamed.setText(userProfile.getUserName());


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
                intent.putExtra("User", current);
                startActivity(intent);
            }
        });


    }


}

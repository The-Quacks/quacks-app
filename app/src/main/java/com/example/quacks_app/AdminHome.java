package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/*
This is the home screen for Admin, containing the buttons that allow you to navigate
to different lists. Profiles, Events, Images, Facilities, and QR codes
 */

public class AdminHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        EdgeToEdge.enable(this);

        ImageButton Profile = findViewById(R.id.profilesButton);
        ImageButton Events = findViewById(R.id.eventsButton);
        ImageButton Images = findViewById(R.id.imagesButton);
        ImageButton Facilities = findViewById(R.id.facilitiesButton);

        Bundle bundle = new Bundle();

        Profile.setOnClickListener(view -> {
            Intent myIntent = new Intent(AdminHome.this, ProfileActivity.class);
            bundle.putString("viewType", "Profile");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        });

        Events.setOnClickListener(view -> {
            Intent myIntent = new Intent(AdminHome.this, AdminListView.class);
            bundle.putString("viewType", "Events");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        });

        Images.setOnClickListener(view -> {
            Intent myIntent = new Intent(AdminHome.this, AdminListView.class);
            bundle.putString("viewType", "Images");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        });

        Facilities.setOnClickListener(view -> {
            Intent myIntent = new Intent(AdminHome.this, AdminListView.class);
            bundle.putString("viewType", "Facilities");
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        });



    }
}

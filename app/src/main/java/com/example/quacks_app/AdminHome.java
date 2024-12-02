package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*
This is the home screen for Admin, containing the buttons that allow you to navigate
to different lists. Profiles, Events, Images, Facilities, and QR codes
 */

public class AdminHome extends AppCompatActivity {
    private User user;
    boolean isFirstSelection = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        EdgeToEdge.enable(this);

        user = (User) getIntent().getSerializableExtra("User");

        ImageButton Profile = findViewById(R.id.profilesButton);
        ImageButton Events = findViewById(R.id.eventsButton);
        ImageButton Images = findViewById(R.id.imagesButton);
        ImageButton Facilities = findViewById(R.id.facilitiesButton);
        Spinner spinner = findViewById(R.id.profile_spinner_admin);

        Bundle bundle = new Bundle();

        ArrayAdapter<String> adapter = getStringArrayAdapter();
        spinner.setAdapter(adapter);
        spinner.setSelection(2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return; // Ignore the initial trigger
                }

                String selectedItem = parent.getItemAtPosition(position).toString();

                if ("Organizer Profile".equals(selectedItem)) {
                    Intent intent = new Intent(AdminHome.this, OrganizerHomepage.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                    finish();
                }
                else if ("Entrant Profile".equals(selectedItem)) {
                    Intent intent = new Intent(AdminHome.this, EntrantHome.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Profile.setOnClickListener(view -> {
            Intent myIntent = new Intent(AdminHome.this, AdminListView.class);
            bundle.putString("viewType", "Profile");
            myIntent.putExtra("User", user);
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
            Intent myIntent = new Intent(AdminHome.this, AdminImageListView.class);
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

    // Initialize the spinner adapter with selectable profiles
    private ArrayAdapter<String> getStringArrayAdapter() {
        ArrayList<String> items = new ArrayList<>();
        items.add("Entrant Profile");
        if (user.getRoles().contains(Role.ORGANIZER)) {
            items.add("Organizer Profile");
        }
        items.add("Administrator Profile");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}

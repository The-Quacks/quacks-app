package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The {@code AdminHome} class represents the home screen for admin users in the Quacks app.
 * It provides navigation buttons for different features such as Profiles, Events, Images,
 * Facilities, and the ability to switch to the entrant home screen.
 */
public class AdminHome extends AppCompatActivity {
    private User user;

    /**
     * Initializes the activity. Sets up the UI components and navigation actions for the admin home screen.
     * @param savedInstanceState {@code null} if the activity is being created for the first time.
     */
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
        ImageButton switch_activity = findViewById(R.id.switch_profile_admin);

        Bundle bundle = new Bundle();

        switch_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, EntrantHome.class);
                intent.putExtra("User", user);
                startActivity(intent);
                finish();
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
}

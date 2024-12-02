package com.example.quacks_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrganizerHomepage extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button profile;
    private Facility facility;
    private User current;
    boolean isFirstSelection = true;

    /**
     * This is the Organizer Homepage: they can choose out of the four options
     * Edit Profile, View Events, Create Events, Entrant Map
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_homepage);// or the correct XML layout file

        //event = new ArrayList<Event>();
        profile = findViewById(R.id.organizer_profile_button);
        Button view_events = findViewById(R.id.view_events);
        Button create_events = findViewById(R.id.create_event);
        Button entrant_map = findViewById(R.id.mapped);
        Spinner spinner = findViewById(R.id.profile_spinner_organizer);

        current = (User) getIntent().getSerializableExtra("User");

        ArrayAdapter<String> adapter = getStringArrayAdapter();
        spinner.setAdapter(adapter);
        spinner.setSelection(1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return; // Ignore the initial trigger
                }

                String selectedItem = parent.getItemAtPosition(position).toString();

                if ("Administrator Profile".equals(selectedItem)) {
                    Intent intent = new Intent(OrganizerHomepage.this, AdminHome.class);
                    intent.putExtra("User", current);
                    startActivity(intent);
                    finish();
                }
                else if ("Entrant Profile".equals(selectedItem)) {
                    Intent intent = new Intent(OrganizerHomepage.this, EntrantHome.class);
                    intent.putExtra("User", current);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ReadMultipleCallback<Facility> readMultipleCallback = new ReadMultipleCallback<Facility>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<Facility> data) {
                for (Facility fac : data) {
                    if (fac.getOrganizerId().equals(current.getDocumentId())) {
                        facility = fac;
                        break;
                    }
                }
                if (facility == null) {
                    profile.setTag("false");
                }
                else {
                    profile.setTag("true");
                }
            }

            @Override
            public void onReadMultipleFailure(Exception e) {
                Toast.makeText(OrganizerHomepage.this, "Could not connect to database", Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, Object> query = new HashMap<>();
        query.put("organizerId", current.getDocumentId());
        CRUD.readQueryStatic(query, Facility.class, readMultipleCallback);

        profile.setOnClickListener(view -> {
            if (profile.getTag().equals("false")) {
                startActivityForResult(new Intent(OrganizerHomepage.this, CreateFacility.class), 1);
            } else {
                Intent intent = new Intent(OrganizerHomepage.this, ViewOrganizer.class);
                intent.putExtra("User", current);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        view_events.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerHomepage.this, ViewEvents.class);
            intent.putExtra("User", current);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });

        create_events.setOnClickListener(view -> {
            Intent intent =new Intent(OrganizerHomepage.this, CreateEvent.class);
            // mode = create, as we're creating the event
            intent.putExtra("mode", "create");

            intent.putExtra("User", current);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });


        entrant_map.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerHomepage.this, SelectMapEvent.class);
            intent.putExtra("User", current);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });
    }

    // Initialize the spinner adapter with selectable profiles
    private ArrayAdapter<String> getStringArrayAdapter() {
        ArrayList<String> items = new ArrayList<>();
        items.add("Entrant Profile");
        items.add("Organizer Profile");
        if (current.getRoles().contains(Role.ADMIN)) {
            items.add("Administrator Profile");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * This gets the organizer profile back from the create event page.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {  // check for the specific request code
            if (resultCode == RESULT_OK) {
                current =(User) data.getSerializableExtra("User");
                facility = (Facility) data.getSerializableExtra("Facility");
                if (current == null || facility == null) {
                    Toast.makeText(OrganizerHomepage.this, "Error: Facility and Current are Null", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
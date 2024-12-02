package com.example.quacks_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the activity when an entrant presses the Waitlists button.
 * It displays all events where the user has joined the waitlist.
 * Selecting an event displays information about the event, and allows the user to
 * leave that waitlist.
 */
public class ViewEventsEntrant extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_event_list);

        ListView eventList = findViewById(R.id.event_list);
        ImageButton home = findViewById(R.id.homeIcon);

        user = (User) getIntent().getSerializableExtra("User");

        ArrayList<Event> events = (ArrayList<Event>) getIntent().getSerializableExtra("EventList");
        EventArrayAdapter eventArrayAdapter = new EventArrayAdapter(this, events);
        eventList.setAdapter(eventArrayAdapter);

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {

                                Intent data = result.getData();
                                if (data != null) {
                                    user = (User) data.getSerializableExtra("User");
                                }
                            }
                        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event clickedEvent = (Event) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(ViewEventsEntrant.this, EventDescription.class);
                intent.putExtra("User", user);
                intent.putExtra("id", clickedEvent.getEventId());
                intent.putExtra("isRemoving", true);
                activityResultLauncher.launch(intent);
            }
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(ViewEventsEntrant.this, EntrantHome.class);
            intent.putExtra("User", user);
            startActivity(intent);
        });
    }
}

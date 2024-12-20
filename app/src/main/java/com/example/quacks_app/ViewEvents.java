package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code ViewEvents} class handles the display and management of events associated with a specific facility.
 * Users can view a list of events, navigate to event details, and access related features through bottom navigation buttons.
 */
public class ViewEvents extends AppCompatActivity {
    private Facility facility;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;
    private ArrayList<Listable> dataList;
    private EventList evented;
    private User user;

    /**
     * Initializes the activity and sets up the event list, bottom navigation buttons, and event click listeners.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        ArrayList<Listable> dataList = new ArrayList<>();

        if (getIntent().getSerializableExtra("Facility")==null){
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");

        if (facility == null || user == null) {
            Toast.makeText(this, "Missing required data. Exiting.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ListView eventList = findViewById(R.id.list_of_events);

        eventDataList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList, facility);
        eventList.setAdapter(eventArrayAdapter);

        Map<String, Object> query = new HashMap<>();
        query.put("organizerId", user.getDocumentId());
        ListenerRegistration listenerRegistration = CRUD.readQueryLive(query, Event.class, new ReadMultipleCallback<Event>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<Event> data) {
                eventDataList.clear();
                eventDataList.addAll(data);
                eventArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onReadMultipleFailure(Exception e) {
                Toast.makeText(ViewEvents.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle list item clicks
        eventList.setOnItemClickListener((adapterView, view, i, l) -> {
            Event clickedEvent = (Event) eventArrayAdapter.getItem(i); // Use adapter's `getItem` method to retrieve data
            if (clickedEvent == null) {
                Toast.makeText(ViewEvents.this, "Error: Event not found.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ViewEvents.this, EventInfo.class);
            intent.putExtra("Event", clickedEvent);
            intent.putExtra("EventList", evented);
            if (facility != null){
                intent.putExtra("Facility", facility);
            }
            if (user != null) {
                intent.putExtra("User", user);
            }
            startActivity(intent);
        });

        //This is the bottom of the page directory
        ImageButton homepage = findViewById(R.id.house);
        ImageButton profile = findViewById(R.id.person);
        ImageButton search = findViewById(R.id.search);

        homepage.setOnClickListener(view -> {
            Intent intent = new Intent(ViewEvents.this, OrganizerHomepage.class);
            intent.putExtra("User", user);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(ViewEvents.this, ViewOrganizer.class);
            intent.putExtra("User", user);
            intent.putExtra("Facility", facility);
            startActivity(intent);
        });

        search.setOnClickListener(view -> {
            //Already here
            Toast.makeText(ViewEvents.this, "Already On Search Page!", Toast.LENGTH_SHORT).show();
        });
    }
}
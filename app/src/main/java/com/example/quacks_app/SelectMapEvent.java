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

public class SelectMapEvent extends AppCompatActivity {
    private ImageButton homepage;
    private ImageButton profile;
    private ImageButton search;
    private Facility facility;
    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;
    private User user;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        if (facility == null){
            finish();
        }

        eventList = findViewById(R.id.list_of_events);

        eventDataList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList, facility);
        eventList.setAdapter(eventArrayAdapter);

        Map<String, Object> query = new HashMap<>();
        query.put("organizerId", user.getDocumentId());
        listenerRegistration = CRUD.readQueryLive(query, Event.class, new ReadMultipleCallback<Event>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<Event> data) {
                eventDataList.clear();
                eventDataList.addAll(data);
                eventArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onReadMultipleFailure(Exception e) {
                Toast.makeText(SelectMapEvent.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event clickedEvent = (Event) adapterView.getItemAtPosition(i);

                CRUD.readQueryLive(new HashMap<>(), User.class, new ReadMultipleCallback<User>() {
                    @Override
                    public void onReadMultipleSuccess(ArrayList<User> allUsers) {
                        ArrayList<User> entrants = new ArrayList<>();
                        for (User user : allUsers) {
                            if (user.getUserProfile() == null) {
                                continue;
                            }
                            if (user.getUserProfile() != null && user.getUserProfile().getEventList() != null && user.getUserProfile().getEventList().contains(clickedEvent.getEventId())) {
                                entrants.add(user);
                            }
                        }

                        Intent intent = new Intent(SelectMapEvent.this, EntrantsMapActivity.class);
                        if (clickedEvent != null && !entrants.isEmpty() && facility != null && user != null) {
                            intent.putExtra("Event", clickedEvent);
                            intent.putExtra("Entrants", entrants);
                            intent.putExtra("Facility", facility);
                            intent.putExtra("User", user);
                        } else {
                            Toast.makeText(SelectMapEvent.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onReadMultipleFailure(Exception e) {
                        Toast.makeText(SelectMapEvent.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //This is the bottom of the page directory
        homepage = findViewById(R.id.house);
        profile = findViewById(R.id.person);
        search = findViewById(R.id.search);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectMapEvent.this, OrganizerHomepage.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectMapEvent.this, ViewOrganizer.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Toast.makeText(SelectMapEvent.this, "Already On Searchpage!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

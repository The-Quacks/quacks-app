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

public class ViewEvents extends AppCompatActivity {
    private Facility facility;
    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;
    private ArrayList<Listable> dataList;
    private EventList evented;
    private User user;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);


        dataList = new ArrayList<>();

        if (getIntent().getSerializableExtra("Facility")==null){
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");

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
                Toast.makeText(ViewEvents.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event clickedEvent = (Event) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(ViewEvents.this, EventInfo.class);
                intent.putExtra("Event", clickedEvent);
                if (facility != null){
                    intent.putExtra("Facility", facility);
                }
                if (user != null) {
                    intent.putExtra("User", user);
                }
                startActivity(intent);
            }
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
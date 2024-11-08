package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class ViewEvents extends AppCompatActivity {
    private ImageButton homepage;
    private ImageButton profile;
    private ImageButton search;
    private Facility facility;

    private FirebaseFirestore db;
    private CollectionReference eventsRef;


    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventArrayAdapter;
    private ArrayList<Listable> dataList;
    private EventList evented;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);


        dataList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Event");

        if (getIntent().getSerializableExtra("EventList") == null){
           finish();
        }
        evented = (EventList) getIntent().getSerializableExtra("EventList");
        if (getIntent().getSerializableExtra("Facility")==null){
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");

        eventList = findViewById(R.id.event_list);

        eventDataList = new ArrayList<>();
        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        eventsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Toast.makeText(ViewEvents.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                return;
            }

            eventDataList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                Event event = document.toObject(Event.class);
                eventDataList.add(event);
            }
            eventArrayAdapter.notifyDataSetChanged();
        });



        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event clickedEvent = (Event) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(ViewEvents.this, EventInfo.class);
                intent.putExtra("Event", clickedEvent);
                if (evented != null) {
                    intent.putExtra("EventList", evented);
                }
                if (facility != null){
                    intent.putExtra("Facility", facility);
                }
                startActivity(intent);
            }
        });


        //This is the bottom of the page directory
        homepage = findViewById(R.id.house);
        profile = findViewById(R.id.person);
        search = findViewById(R.id.search);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEvents.this, OrganizerHomepage.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEvents.this, ViewOrganizer.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Toast.makeText(ViewEvents.this, "Already On Searchpage!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
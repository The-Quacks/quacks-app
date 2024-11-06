package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class ViewEvents extends AppCompatActivity {
    private ImageButton homepage;
    private ImageButton profile;
    private ImageButton search;
    private Facility facility;

    private FirebaseFirestore db;
    private CollectionReference eventsRef;


    ListView eventList;
    ArrayList<Event> eventDataList;
    EventArrayAdapter eventArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);// or the correct XML layout file
        facility = (Facility) getIntent().getSerializableExtra("Facility");

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        eventList = findViewById(R.id.event_list);
        eventDataList = new ArrayList<>();

        eventArrayAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventArrayAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //when someone clicks on an event it leads them to the event info
                //page
                Event clicked = (Event) eventList.getItemAtPosition(position);
                Intent intent = new Intent(ViewEvents.this, EventInfo.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", clicked);
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
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEvents.this, ViewOrganizer.class);
                intent.putExtra("Facility", facility);
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

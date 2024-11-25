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

public class ViewEventsEntrant extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_event_list);

        ListView eventList = findViewById(R.id.event_list);
        ImageButton home = findViewById(R.id.homeIcon);

        ArrayList<Event> events = (ArrayList<Event>) getIntent().getSerializableExtra("EventList");
        EventArrayAdapter eventArrayAdapter = new EventArrayAdapter(this, events);
        eventList.setAdapter(eventArrayAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event clickedEvent = (Event) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(ViewEventsEntrant.this, EventDescription.class);
                intent.putExtra("User", getIntent().getSerializableExtra("User"));
                intent.putExtra("id", clickedEvent.getEventId());
                intent.putExtra("isRemoving", true);
                startActivity(intent);
            }
        });

        home.setOnClickListener(v -> {
            startActivity(new Intent(this, EntrantHome.class));
        });
    }
}

package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventInfo extends AppCompatActivity {
    private TextView organizer;
    private TextView date;
    private TextView description;
    private TextView facility;
    private TextView id;
    private String applicantList;
    private EventList eventList;
    private Button open_registration;
    private Button close_registration;
    private Button edit_event;
    private Button entrant_map;
    private ImageButton profile;
    private ImageButton search;
    private ImageButton homepage;
    private Facility actual_facility;


    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);


        if (getIntent().getSerializableExtra("Event") == null) {
            finish();
        } else {
            event = (Event) getIntent().getSerializableExtra("Event");
        }


        if (getIntent().getSerializableExtra("Facility") != null) {
            actual_facility = (Facility) getIntent().getSerializableExtra("Facility");
        }


        date = findViewById(R.id.event_date);
        description = findViewById(R.id.event_description);
        facility = findViewById(R.id.event_facility);
        organizer = findViewById(R.id.event_organizer);
        id = findViewById(R.id.event_id);
        open_registration = findViewById(R.id.register);
        close_registration = findViewById(R.id.close);
        edit_event = findViewById(R.id.delete_button);
        entrant_map = findViewById(R.id.map);


        String text = event.getDescription();
        Date dated = event.getDateTime();
        Facility fac = event.getFacility();
        String name = "";
        if (facility != null) {
            name = fac.getName();
        }
        String organizerid = event.getOrganizerId();

        date.setText(dated.toString());
        description.setText(text);
        facility.setText(name);
        organizer.setText(organizerid);
        id.setText(event.getEventId());

        open_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //makes an applicant list for that event.
                Intent intent = new Intent(EventInfo.this, OpenRegistration.class);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        close_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfo.this, NotifyOptions.class);
                intent.putExtra("Event", event);
                startActivity(intent);

            }
        });

        entrant_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EventInfo.this, "Entrant Map Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        edit_event.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Map<String, Object> query = new HashMap<>();
                  query.put("description", event.getDescription());
                  query.put("organizerId", event.getOrganizerId());
                  query.put("dateTime", event.getDateTime());

                  CRUD.readQueryStatic(query, Event.class, new ReadMultipleCallback<Event>() {
                      @Override
                      public void onReadMultipleSuccess(ArrayList<Event> data) {
                          for (Event event : data) {
                              CRUD.delete(event.getDocumentId(), Event.class, new DeleteCallback() {
                                  @Override
                                  public void onDeleteSuccess() {
                                      Toast.makeText(EventInfo.this, "Event has been deleted", Toast.LENGTH_SHORT).show();
                                  }

                                  @Override
                                  public void onDeleteFailure(Exception e) {
                                      Toast.makeText(EventInfo.this, "Error deleting events", Toast.LENGTH_SHORT).show();

                                  }
                              });
                          }
                          if (data.isEmpty()) {
                              Toast.makeText(EventInfo.this, "No events found with the given description and organizer ID", Toast.LENGTH_SHORT).show();
                          }
                          else {
                              Intent intent = new Intent(EventInfo.this, OrganizerHomepage.class);
                              startActivity(intent);
                              finish();
                          }

                      }

                      @Override
                      public void onReadMultipleFailure(Exception e) {
                          Toast.makeText(EventInfo.this, "Error getting events", Toast.LENGTH_SHORT).show();
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
                //Already here
                Intent intent = new Intent(EventInfo.this, OrganizerHomepage.class);
                if (facility != null) {
                    intent.putExtra("Facility", actual_facility);
                }
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfo.this, ViewOrganizer.class);
                if (facility != null) {
                    intent.putExtra("Facility", actual_facility);
                }
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }
}



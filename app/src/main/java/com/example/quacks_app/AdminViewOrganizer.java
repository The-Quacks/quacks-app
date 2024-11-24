package com.example.quacks_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*
This is the individual edit and delete screen to view events, can be multipurposed for not just admin
 */

public class AdminViewOrganizer extends AppCompatActivity {
    String fieldOne;
    String fieldTwo;
    String fieldThree;
    String fieldFour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_admin);
        EdgeToEdge.enable(this);

        Bundle bundle = getIntent().getExtras();
        Listable editEvent = (Listable) bundle.getSerializable("value");
        String id = bundle.getString("id");

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button delete = findViewById(R.id.delete_button);

        delete.setOnClickListener(view -> {
            DeleteCallback delCall = new DeleteCallback(){
                @Override
                public void onDeleteSuccess() {
                    System.out.println("Document successfully deleted!");
                    Toast.makeText(AdminViewOrganizer.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onDeleteFailure(Exception e) {
                    System.err.println("Error deleting document: " + e.getMessage());
                }
            };
            if (editEvent instanceof Event){
                Event realEvent = (Event) editEvent;
                CRUD.delete(id, Event.class, delCall);
                CRUD.delete(realEvent.getApplicantList(), ApplicantList.class, delCall);
            }
            else if (editEvent instanceof User){
                // todo if user, remove all mentions of user in applicantlists
                // todo if Organzier, remove all events and applicantlists associated with it, alongside facilities
                // todo if admin, remove user profile
            }

            else if (editEvent instanceof Facility) {
                Facility realFacility = (Facility) editEvent;
                CRUD.readLive(realFacility.getEventListId(), EventList.class, new ReadCallback<EventList>() {
                    @Override
                    public void onReadSuccess(EventList eventListData) {
                        ArrayList<String> evIds = eventListData.getEventIds();
                        for (String event : evIds){
                            CRUD.readLive(event, Event.class, new ReadCallback<Event>() {
                                @Override
                                public void onReadSuccess(Event eventData) {
                                    String appList = eventData.getApplicantList();
                                    CRUD.delete(appList, ApplicantList.class, delCall);
                                    CRUD.delete(eventData.getDocumentId(), Event.class, delCall);
                                    CRUD.delete(realFacility.getEventListId(), EventList.class, delCall);
                                    CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);
                                }

                                @Override
                                public void onReadFailure(Exception e) {
                                    CRUD.delete(realFacility.getEventListId(), EventList.class, delCall);
                                    CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);

                                }
                            });
                        }


                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);

                    }
                });

            }
        });
        if (editEvent instanceof Event){
            CRUD.readLive(editEvent.getDocumentId(), Event.class, new ReadCallback<Event>() {
                @Override
                public void onReadSuccess(Event data) {
                    Event specEvent = data;
                    CRUD.readLive(specEvent.getApplicantList(), ApplicantList.class, new ReadCallback<ApplicantList>() {
                        @Override
                        public void onReadSuccess(ApplicantList appData) {
                            CRUD.readLive(specEvent.getFacility(), Facility.class, new ReadCallback<Facility>() {
                                @Override
                                public void onReadSuccess(Facility facData) {
                                    fieldOne = "Name: " + specEvent.getEventName();
                                    fieldTwo = "Capacity: "+ appData.getLimit();
                                    fieldThree = "Availability: " + (appData.getLimit() - appData.getApplicantIds().size());
                                    fieldFour = "Location: " + facData.getLocation();

                                    EditText name = findViewById(R.id.Name);
                                    EditText capacity = findViewById(R.id.capacity);
                                    EditText available = findViewById(R.id.availability);
                                    EditText location = findViewById(R.id.location);

                                    name.setText(fieldOne);
                                    capacity.setText(fieldTwo);
                                    available.setText(fieldThree);
                                    location.setText(fieldFour);

                                }
                                @Override
                                public void onReadFailure(Exception e) {

                                }
                            });

                        }
                        @Override
                        public void onReadFailure(Exception e) {

                        }
                    });
                }
                @Override
                public void onReadFailure(Exception e) {
                    Log.d(TAG, "get failed with ", e);

                }
            });

        }
        else if (editEvent instanceof Facility){
            Facility specFacility = (Facility) editEvent;
            fieldOne = "Name: " + specFacility.getName();
            fieldTwo = "Contact Info: "+  specFacility.getPhone();
            fieldThree = "Location: " + specFacility.getLocation();
            CRUD.readLive(specFacility.getOrganizerId(), User.class, new ReadCallback<User>() {
                @Override
                public void onReadSuccess(User specUser) {
                    fieldFour = "Organizer: " + specUser.getUserProfile().getUserName();
                    EditText name = findViewById(R.id.Name);
                    EditText capacity = findViewById(R.id.capacity);
                    EditText available = findViewById(R.id.availability);
                    EditText location = findViewById(R.id.location);

                    name.setText(fieldOne);
                    capacity.setText(fieldTwo);
                    available.setText(fieldThree);
                    location.setText(fieldFour);
                }

                @Override
                public void onReadFailure(Exception e) {

                }
            });

        }

    }
}

package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
Selecting a specified number of participants per notification round
 */
public class SelectAtRandom extends AppCompatActivity {
    private EditText capacity;
    private Button back;
    private Button confirm;
    private Event event;
    private String applicantListId;
    private NotificationList notificationList;
    private ArrayList<String> Tracker;
    private ImageButton home;
    private ImageButton search;
    private ImageButton profile;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_random_applicants);

        capacity = findViewById(R.id.random_amount);
        back = findViewById(R.id.random_back_button);
        confirm = findViewById(R.id.random_confirm_button);

        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(SelectAtRandom.this, "No event was passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        event = (Event) getIntent().getSerializableExtra("Event");
        applicantListId = "";
        int waitlist_capacity = 0;

        try{
            assert event != null;
            applicantListId = event.getApplicantList();
            waitlist_capacity = event.getRegistrationCapacity();
        } catch(Exception E){
            Toast.makeText(SelectAtRandom.this, "The applicant list or waitlist capacity is not set", Toast.LENGTH_SHORT).show();
            finish();
        }

        capacity.setText(String.valueOf(waitlist_capacity));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (applicantListId == null || applicantListId.isEmpty()) {
                    Toast.makeText(SelectAtRandom.this, "Applicant List ID is invalid.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                CRUD.readStatic(applicantListId, ApplicantList.class, new ReadCallback<ApplicantList>() {
                    @Override
                    public void onReadSuccess(ApplicantList applicantList) {

                        if (applicantList != null) {
                            int limit;
                            try {
                                limit = Integer.parseInt(capacity.getText().toString());
                                if (limit <= 0 || limit > applicantList.getApplicantIds().size()) {
                                    Toast.makeText(SelectAtRandom.this, "Number of applicants chosen exceeds amount in waitlist", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (NumberFormatException e) {
                                Toast.makeText(SelectAtRandom.this, "Invalid capacity value.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            List<String> ids = applicantList.getApplicantIds().subList(0, limit);
                            int counter = 0;
                            for (String applicantId : ids) {
                                CRUD.readStatic(applicantId, User.class, new ReadCallback<User>() {
                                    @Override
                                    public void onReadSuccess(User user) {
                                        if (user != null) {

                                            UserProfile profile = user.getUserProfile();
                                            Notification notify = new Notification(user, applicantListId, event.getEventId(), "Unknown", "Not Sent");

                                            //Looks at the NotificationList Id and decides whether it needs to create a new one
                                            if (event.getNotificationList() == null){//creates one
                                                String notificationListId = UUID.randomUUID().toString();
                                                notificationList = new NotificationList();
                                                notificationList.setDocumentId(notificationListId);
                                                event.setNotificationList(notificationList);
                                                notificationList.setNotificationEventId(event.getEventId());
                                            }
                                            else{
                                                notificationList = event.getNotificationList();
                                            }
                                            //otherwise event.getNotificationList returns the list associated to the event
                                            //we add the new notification to the list
                                            notificationList.addNotification(notify);

                                        }

                                    }

                                    @Override
                                    public void onReadFailure(Exception e) {
                                        Toast.makeText(SelectAtRandom.this, "Error notifying users.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Tracker.add("1");
                            }
                            if (!Tracker.isEmpty()){
                                Toast.makeText(SelectAtRandom.this, "Users Notified.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SelectAtRandom.this, OrganizerHomepage.class);
                                intent.putExtra("Event", event);
                                startActivity(intent);

                            }else{
                                Toast.makeText(SelectAtRandom.this, "Error notifying users.", Toast.LENGTH_SHORT).show();
                                finish();
                            }



                        } else {
                            Toast.makeText(SelectAtRandom.this, "Applicant list is empty.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(SelectAtRandom.this, "Error retrieving applicant list.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}





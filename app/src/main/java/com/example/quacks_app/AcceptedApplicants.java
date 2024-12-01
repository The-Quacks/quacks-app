package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AcceptedApplicants extends AppCompatActivity {
    private ListView applicantListView;
    private Cartable userdisplay;
    private ArrayList<Cartable> userList;
    private ArrayList<User> real_user;
    private ApplicantArrayAdapter applicantArrayAdapter;
    private ImageButton search;
    private ImageButton homepage;
    private ImageButton profile;
    private Button back;
    private Facility facility;
    private User user;
    private Event event;
    private Button select;
    private Button notify_all;
    /**
     * This is the list that holds all accepted applicants
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accepted_applicants);

        if (getIntent().getSerializableExtra("Facility") == null){
            Toast.makeText(AcceptedApplicants.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null){
            Toast.makeText(AcceptedApplicants.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(AcceptedApplicants.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }
        //if (getIntent().getSerializableExtra("EventList") == null){
        //    Toast.makeText(AcceptedApplicants.this, "No EventList", Toast.LENGTH_SHORT).show();
        //    finish();
        //}
        //eventList = (EventList) getIntent().getSerializableExtra("EventList");
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");
        assert event != null;
        if (event.getRegistration() == false){
            Toast.makeText(AcceptedApplicants.this, "Please Open Registration!", Toast.LENGTH_SHORT).show();
            finish();
        }
        select = findViewById(R.id.accepted_select_button);
        notify_all = findViewById(R.id.accepted_notify_button);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedApplicants.this, NotifyOptions.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        notify_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedApplicants.this, Choices.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        //setting up list

        applicantListView = findViewById(R.id.accepted_app_list);
        real_user = new ArrayList<User>();
        userList = new ArrayList<Cartable>();
        applicantArrayAdapter = new ApplicantArrayAdapter(this, userList);
        applicantListView.setAdapter(applicantArrayAdapter);

        if (event == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String applicantListId = event.getApplicantList();

        NotificationList notificationList = event.getNotificationList(); //gets notificationList object
        ArrayList<Notification> notifications = notificationList.getNotificationList();

        ArrayList<String> accepted_applicant_ids = new ArrayList<>();
        if (notifications == null){
            finish();
        }


        // Load the applicants
        CRUD.readStatic(applicantListId, ApplicantList.class, new ReadCallback<ApplicantList>() {
            @Override
            public void onReadSuccess(ApplicantList applicantList) {
                if (applicantList != null) {

                    userList.clear();

                    for (String applicantId : applicantList.getApplicantIds()) {

                        CRUD.readStatic(applicantId, User.class, new ReadCallback<User>() {//this retrieves the applicanId

                            @Override
                            public void onReadSuccess(User user) {
                                if (user != null) {
                                    //check to see if user was notified here
                                    real_user.add(user);
                                    UserProfile profile = user.getUserProfile();

                                    for (int i = 0; i < notifications.size(); i++){
                                        Notification current = notifications.get(i);
                                        if (current.getUser() != null) {
                                            User current_user = current.getUser();
                                            String first = current_user.getDeviceId();
                                            String second = user.getDeviceId();
                                            if (first.equals(second) && current.getWaitlistStatus().equals("Accepted")) {
                                                userdisplay = new Cartable(profile.getUserName(), user.getDeviceId(), false, profile);
                                                userList.add(userdisplay);
                                            }

                                        }
                                    }
                                }
                                applicantArrayAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onReadFailure(Exception e) {
                                Toast.makeText(AcceptedApplicants.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(AcceptedApplicants.this, "Failed to load applicant list", Toast.LENGTH_SHORT).show();
            }
        });

        back = findViewById(R.id.accepted_back_button);
        search = findViewById(R.id.accepted_app_search);
        homepage = findViewById(R.id.accepted_app_house);
        profile = findViewById(R.id.accepted_app_person);

        //create buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedApplicants.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedApplicants.this, ViewOrganizer.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(AcceptedApplicants.this, ViewEvents.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                //intent.putExtra("EventList", eventList);
                startActivity(intent);

            }
        });




    }
}

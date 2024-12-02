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

/**
 * The {@code CancelledApplicants} class represents a screen in the Quacks app
 * that displays a list of applicants who cancelled their acceptance for a specific event.
 * It allows organizers to view the list and notify applicants if needed.
 */
public class CancelledApplicants extends AppCompatActivity {
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
     * Initializes the activity, sets up the UI components, retrieves data from intent extras,
     * and populates the list of cancelled applicants.
     *
     * @param savedInstanceState {@code null} if the activity is being created for the first time.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelled_applicants);

        if (getIntent().getSerializableExtra("Facility") == null){
            Toast.makeText(CancelledApplicants.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null){
            Toast.makeText(CancelledApplicants.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(CancelledApplicants.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");

        assert event != null;
        if (event.getRegistration() == false){
            Toast.makeText(CancelledApplicants.this, "Please Open Registration!", Toast.LENGTH_SHORT).show();
            finish();
        }


        notify_all = findViewById(R.id.cancelled_notify_button);


        notify_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancelledApplicants.this, LastChoice.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        //setting up list

        applicantListView = findViewById(R.id.cancelled_app_list);
        real_user = new ArrayList<User>();
        userList = new ArrayList<Cartable>();
        applicantArrayAdapter = new ApplicantArrayAdapter(this, userList);
        applicantListView.setAdapter(applicantArrayAdapter);

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
                                                if (!current.getAccepted()) {
                                                    userdisplay = new Cartable(profile.getUserName(), user.getDeviceId(), false, profile);
                                                    userList.add(userdisplay);
                                                }
                                            }

                                        }
                                    }
                                }
                                applicantArrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onReadFailure(Exception e) {
                                Toast.makeText(CancelledApplicants.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(CancelledApplicants.this, "Failed to load applicant list", Toast.LENGTH_SHORT).show();
            }
        });

        back = findViewById(R.id.cancelled_back_button);
        search = findViewById(R.id.cancelled_app_search);
        homepage = findViewById(R.id.cancelled_app_house);
        profile = findViewById(R.id.cancelled_app_person);

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
                Intent intent = new Intent(CancelledApplicants.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancelledApplicants.this, ViewOrganizer.class);
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
                Intent intent = new Intent(CancelledApplicants.this, ViewEvents.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                //intent.putExtra("EventList", eventList);
                startActivity(intent);

            }
        });
    }
}

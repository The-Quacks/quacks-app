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
 * The {@code DeclinedApplicants} class displays a list of all declined applicants for a specific event.
 * It allows organizers to view and manage the declined applicants, as well as send notifications or select further actions.
 */
public class DeclinedApplicants extends AppCompatActivity {
    private ListView applicantListView;
    private Cartable userdisplay;
    private ArrayList<Cartable> userList;
    private ArrayList<User> real_user;
    private ApplicantArrayAdapter applicantArrayAdapter;
    private Button back;
    private ImageButton search;
    private ImageButton profile;
    private ImageButton homepage;
    private Facility facility;
    private User user;
    private Button notify_all;
    private Button notify_options;
    private Event event;

    /**
     * Initializes the activity, sets up UI components, and displays the list of declined applicants.
     *
     * @param savedInstanceState If the activity is being reinitialized after being shut down,
     *                           this bundle contains the most recent data. Otherwise, it is {@code null}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.declined_applicants);

        if (getIntent().getSerializableExtra("Facility") == null){
            Toast.makeText(DeclinedApplicants.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null){
            Toast.makeText(DeclinedApplicants.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null){
            Toast.makeText(DeclinedApplicants.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");

        assert event != null;
        if (event.getRegistration() == false){
            Toast.makeText(DeclinedApplicants.this, "Please Open Registration!", Toast.LENGTH_SHORT).show();
            finish();

        }

        notify_all = findViewById(R.id.declined_notify_button);
        notify_options = findViewById(R.id.declined_select_button);

        notify_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclinedApplicants.this, Choices.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        notify_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclinedApplicants.this, NotifyOptions.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);
            }
        });

        //setting up list

        applicantListView = findViewById(R.id.declined_app_list);
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

                        CRUD.readStatic(applicantId, User.class, new ReadCallback<User>() {


                            @Override
                            public void onReadSuccess(User user) {
                                if (user != null) {
                                    //check to see if user was notified here
                                    //check to see if user was notified here
                                    real_user.add(user);
                                    UserProfile profile = user.getUserProfile();

                                    for (int i = 0; i < notifications.size(); i++){
                                        Notification current = notifications.get(i);
                                        if (current.getUser() != null) {
                                            User current_user = current.getUser();
                                            String first = current_user.getDeviceId();
                                            String second = user.getDeviceId();
                                            if (first.equals(second) && current.getWaitlistStatus().equals("Declined")) {
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
                                Toast.makeText(DeclinedApplicants.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(DeclinedApplicants.this, "Failed to load applicant list", Toast.LENGTH_SHORT).show();
            }
        });


        search = findViewById(R.id.declined_app_search);
        homepage = findViewById(R.id.declined_app_house);
        profile = findViewById(R.id.declined_app_person);

        back = findViewById(R.id.declined_back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclinedApplicants.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclinedApplicants.this, ViewOrganizer.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(DeclinedApplicants.this, ViewEvents.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });
    }
}
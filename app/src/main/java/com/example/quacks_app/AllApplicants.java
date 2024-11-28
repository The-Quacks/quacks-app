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

public class AllApplicants extends AppCompatActivity {
    private ListView applicantListView;
    private Cartable userdisplay;
    private ArrayList<Cartable> userList;
    private ArrayList<User> real_user;
    private ApplicantArrayAdapter applicantArrayAdapter;
    private Button select;
    private Button notify_all;
    private ImageButton search;
    private ImageButton homepage;
    private ImageButton profile;
    private Facility facility;
    private User user;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_applicants);

        if (getIntent().getSerializableExtra("Facility") == null) {
            Toast.makeText(AllApplicants.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null) {
            Toast.makeText(AllApplicants.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null) {
            Toast.makeText(AllApplicants.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        // Get the Event and ApplicantList ID
        Event event = (Event) getIntent().getSerializableExtra("Event");

        assert event != null;
        if (event.getApplicantList().equals("0")){
            Toast.makeText(AllApplicants.this, "Please Open Registration!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Buttons
        select = findViewById(R.id.all_select_button);
        notify_all = findViewById(R.id.all_notify_button);

        search = findViewById(R.id.all_app_search);
        homepage = findViewById(R.id.all_app_house);
        profile = findViewById(R.id.all_app_person);

        back = findViewById(R.id.all_back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllApplicants.this, OrganizerHomepage.class);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllApplicants.this, ViewOrganizer.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Intent intent = new Intent(AllApplicants.this, ViewEvents.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                startActivity(intent);
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this will go to the select applicant page
                Intent intent = new Intent(AllApplicants.this, NotifyOptions.class);
                intent.putExtra("Facility", facility);
                intent.putExtra("User", user);
                intent.putExtra("Event", event);
                startActivity(intent);

            }
        });

        notify_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set number of applicants then pool at random
                Toast.makeText(AllApplicants.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        //setting up list

        applicantListView = findViewById(R.id.all_app_list);
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
                                    real_user.add(user);
                                    UserProfile profile = user.getUserProfile();
                                    userdisplay = new Cartable(profile.getUserName().toString(), user.getDeviceId(), false, profile);
                                    userList.add(userdisplay);
                                }
                                applicantArrayAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onReadFailure(Exception e) {
                                Toast.makeText(AllApplicants.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(AllApplicants.this, "No applicant list found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(AllApplicants.this, "Failed to load applicant list", Toast.LENGTH_SHORT).show();
            }
        });

        //Suggest here click into other peoples profiles?

    }
}

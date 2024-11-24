package com.example.quacks_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_applicants);

        //Buttons
        select = findViewById(R.id.all_select_button);
        notify_all = findViewById(R.id.all_notify_button);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this will go to the select applicant page
                Toast.makeText(AllApplicants.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        notify_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set number of applicants then pool at random
                Toast.makeText(AllApplicants.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });


        applicantListView = findViewById(R.id.all_app_list);
        real_user = new ArrayList<User>();
        userList = new ArrayList<Cartable>();
        applicantArrayAdapter = new ApplicantArrayAdapter(this, userList);
        applicantListView.setAdapter(applicantArrayAdapter);

        // Get the Event and ApplicantList ID
        Event event = (Event) getIntent().getSerializableExtra("Event");

        if (event == null|| event.getApplicantList() == null) {
            Toast.makeText(this, "Registration is not open yet.", Toast.LENGTH_SHORT).show();
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
                                    applicantList.removeUser(user);
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
    }
}

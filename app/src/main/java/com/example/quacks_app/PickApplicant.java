package com.example.quacks_app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;


public class PickApplicant extends AppCompatActivity {
    private ListView applicantListView;
    private Cartable userdisplay;
    private ArrayList<Cartable> userList;
    private ArrayList<User> real_user;
    private ApplicantArrayAdapter applicantArrayAdapter;
    private Button select;
    private ImageButton homepage;
    private ImageButton search;
    private ImageButton profile;
    private Event event;
    int success = 0;
    int fail = 0;
    int total;
    private Button back;

    /*
    Selecting applicants from listview
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_applicants);

        select = findViewById(R.id.select_button);
        back = findViewById(R.id.pick_back_button);


        applicantListView = findViewById(R.id.app_list);
        real_user = new ArrayList<User>();
        userList = new ArrayList<Cartable>();
        applicantArrayAdapter = new ApplicantArrayAdapter(this, userList);
        applicantListView.setAdapter(applicantArrayAdapter);

        // Get the Event and ApplicantList ID
        Event event = (Event) getIntent().getSerializableExtra("Event");

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
                                Toast.makeText(PickApplicant.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(PickApplicant.this, "No applicant list found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(PickApplicant.this, "Failed to load applicant list", Toast.LENGTH_SHORT).show();
            }
        });

        applicantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cartable clickedUser = (Cartable) adapterView.getItemAtPosition(i);

                if (!clickedUser.Carted()) {
                    clickedUser.setCart(true);
                    view.setBackgroundColor(Color.parseColor("#E4D0D0"));
                } else {
                    clickedUser.setCart(false);
                    view.setBackgroundColor(Color.parseColor("#F5EBEB"));
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userList == null || real_user == null || event == null) {
                    Toast.makeText(PickApplicant.this, "Error: Data not initialized", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Cartable> selectedUsers = new ArrayList<>();
                List<User> selected = new ArrayList<>();
                int success = 0;
                for (Cartable user : userList) {
                    if (user.Carted()) {
                        String deviceId = user.getSubDisplay();
                        if (deviceId != null) {
                            //if the deviceId is found, then we create a notification with the user selected
                            //Notification noted = new Notification(user, );
                           // noted.setNotification(event);
                            ++success;
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }

                }
                if (success > 0) {
                    Toast.makeText(PickApplicant.this, " Users were Notified.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PickApplicant.this, " Users were not Notified.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}












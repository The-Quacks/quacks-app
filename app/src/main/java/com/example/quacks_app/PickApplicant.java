package com.example.quacks_app;

import android.content.Intent;
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
import java.util.concurrent.atomic.AtomicInteger;


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
    int fail = 0;
    int total;
    private Button back;
    private User actual_user;
    private Event actual_event;
    private Facility facility;
    private String applicantListId;
    private List<Notification> newNotifications = new ArrayList<>();
    private ArrayList<String> applicantListed = new ArrayList<>();
    private NotificationList notification_list;

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
        real_user = new ArrayList<>();
        userList = new ArrayList<Cartable>();
        applicantArrayAdapter = new ApplicantArrayAdapter(this, userList);
        applicantListView.setAdapter(applicantArrayAdapter);


        if (getIntent().getSerializableExtra("Event") == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        actual_event = (Event) getIntent().getSerializableExtra("Event");


        if (getIntent().getSerializableExtra("Facility") == null) {
            Toast.makeText(PickApplicant.this, "No Facility Passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (getIntent().getSerializableExtra("User") == null) {
            Toast.makeText(PickApplicant.this, "No User Passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        actual_user = (User) getIntent().getSerializableExtra("User");

        applicantListId = actual_event.getApplicantList();
        if (applicantListId == null) {
            Toast.makeText(this, "ApplicantListID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        ArrayList<Cartable> usernames = new ArrayList<>();
        // Load the applicants
        CRUD.readStatic(applicantListId, ApplicantList.class, new ReadCallback<ApplicantList>() {
            @Override
            public void onReadSuccess(ApplicantList applicantList) {
                if (applicantList != null) {

                    userList.clear();

                    for (String applicantId : applicantList.getApplicantIds()) {

                        applicantListed = applicantList.getApplicantIds();

                        CRUD.readStatic(applicantId, User.class, new ReadCallback<User>() {
                            @Override
                            public void onReadSuccess(User user) {
                                if (user != null) {
                                    real_user.add(user);
                                    UserProfile profile = user.getUserProfile();
                                    userdisplay = new Cartable(profile.getUserName(), user.getDeviceId(), false, profile);
                                    userdisplay.setField(profile.getUserName());
                                    userdisplay.setSubfield(user.getDeviceId());
                                    userdisplay.setCart(false);
                                    usernames.add(userdisplay);
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
                } else {
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
                if (userList == null || real_user == null || actual_event == null) {
                    Toast.makeText(PickApplicant.this, "Error: Data not initialized", Toast.LENGTH_SHORT).show();
                    return;
                }

                notification_list = actual_event.getNotificationList();

                if (notification_list == null) {
                    Toast.makeText(PickApplicant.this, "Notification list not initialized.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Notification> notifications = notification_list.getNotificationList();
                if (notifications == null) {
                    notifications = new ArrayList<>();
                    notification_list.setNotificationList(notifications);
                }
                AtomicInteger remaining = new AtomicInteger(userList.size());

                for (Cartable user : userList) {
                    int index = userList.indexOf(user);
                    if (user.Carted()) {
                        for (User current : real_user) {
                            UserProfile profile = current.getUserProfile();
                            if (profile != null && (index == real_user.indexOf(current))) {
                                //check if user is in the current notification list
                                boolean found = false;
                                for (int i = 0; i < notifications.size(); i++) {
                                    Notification notify = notifications.get(i);
                                    if (notify.getUser() != null) {
                                        User current_user = notify.getUser();
                                        String first = current_user.getDeviceId();
                                        String second = current.getDeviceId();

                                        if (second.equals(first)) {
                                            found = true;
                                            notify.setUser(current);
                                            notify.setApplicantListId(applicantListId);
                                            notify.setNotificationEventId(actual_event.getEventId());
                                            notify.setNotificationListId(notification_list.getNotificationListId());
                                            notify.setSentStatus("Not Sent");
                                            notify.setWaitlistStatus("Accepted");
                                            notify.setAccepted(false);

                                            CRUD.update(notify, new UpdateCallback() {
                                                @Override
                                                public void onUpdateSuccess() {
                                                    //notification_list.addNotification(notify);
                                                    Toast.makeText(PickApplicant.this, "Made IT.", Toast.LENGTH_SHORT).show();
                                                    checkCompletion(remaining.decrementAndGet());
                                                }

                                                @Override
                                                public void onUpdateFailure(Exception e) {
                                                    checkCompletion(remaining.decrementAndGet());

                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                                if (!found) {// creates notification
                                    Notification newer = new Notification();

                                    CRUD.create(newer, new CreateCallback() {
                                        @Override
                                        public void onCreateSuccess() {
                                            //Toast.makeText(PickApplicant.this, "notification was added to list", Toast.LENGTH_SHORT).show();

                                            newer.setUser(current);
                                            newer.setApplicantListId(applicantListId);
                                            newer.setNotificationEventId(actual_event.getEventId());
                                            newer.setNotificationListId(notification_list.getNotificationListId());
                                            newer.setSentStatus("Not Sent");
                                            newer.setWaitlistStatus("Declined");
                                            newer.setAccepted(false);
                                            CRUD.update(newer, new UpdateCallback() {
                                                @Override
                                                public void onUpdateSuccess() {
                                                    notification_list.addNotification(newer);
                                                    checkCompletion(remaining.decrementAndGet());
                                                }

                                                @Override
                                                public void onUpdateFailure(Exception e) {
                                                    checkCompletion(remaining.decrementAndGet());
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCreateFailure(Exception e) {
                                            checkCompletion(remaining.decrementAndGet());
                                        }
                                    });
                                }

                            } else {//otherwise find user and set as declined

                                for (int i = 0; i < notifications.size(); i++) {
                                    Notification notify = notifications.get(i);
                                    if (notify.getUser() != null) {
                                        User current_user = notify.getUser();
                                        String first = current_user.getDeviceId();
                                        String second = current.getDeviceId();


                                        if (second.equals(first)) {
                                            notify.setUser(current);
                                            notify.setApplicantListId(applicantListId);
                                            notify.setNotificationEventId(actual_event.getEventId());
                                            notify.setNotificationListId(notification_list.getNotificationListId());
                                            notify.setSentStatus("Not Sent");
                                            notify.setWaitlistStatus("Declined");
                                            notify.setAccepted(false);
                                            CRUD.update(notify, new UpdateCallback() {
                                                @Override
                                                public void onUpdateSuccess() {
                                                    //notification_list.addNotification(notify);
                                                    //Toast.makeText(PickApplicant.this, "Made IT.", Toast.LENGTH_SHORT).show();
                                                    checkCompletion(remaining.decrementAndGet());
                                                }

                                                @Override
                                                public void onUpdateFailure(Exception e) {
                                                    checkCompletion(remaining.decrementAndGet());
                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        checkCompletion(remaining.decrementAndGet());
                        //Toast.makeText(PickApplicant.this, "Couldn't find User", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    /**
     * Checks based from the count of userlist, that it has finished setting notifications for each user
     * @param remainingCount
     */
    private void checkCompletion(int remainingCount) {
        if (remainingCount == 0) {
            // All users processed
            CRUD.update(notification_list, new UpdateCallback() {
                @Override
                public void onUpdateSuccess() {
                    actual_event.setNotificationList(notification_list);
                    CRUD.update(actual_event, new UpdateCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            Toast.makeText(PickApplicant.this, "Users Notified!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PickApplicant.this, EventInfo.class);

                            intent.putExtra("Event", actual_event);
                            intent.putExtra("Facility", facility);
                            intent.putExtra("User", actual_user);
                            startActivity(intent);
                        }

                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(PickApplicant.this, "Could not update the event", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onUpdateFailure(Exception e) {
                    Toast.makeText(PickApplicant.this, "Could not update notification list.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}












package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code LastChoice} class allows organizers to finalize their choice on event applicants.
 * Organizers can accept or decline applicants and notify them of their final status.
 */
public class LastChoice extends AppCompatActivity {
    private Button back;
    private Button confirm;
    private CheckBox accept;
    private CheckBox decline;
    private Event event;
    private Facility facility;
    private User user;
    private NotificationList notificationList;

    /**
     * Called when the activity is created. Sets up the user interface and retrieves necessary data.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_choice);

        if (getIntent().getSerializableExtra("Facility") == null) {
            Toast.makeText(LastChoice.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null) {
            Toast.makeText(LastChoice.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null) {
            Toast.makeText(LastChoice.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");

        back = findViewById(R.id.last_all_back_button);
        confirm = findViewById(R.id.last_all_confirm_button);
        accept = findViewById(R.id.last_accept_check);

        assert event != null;
        if (event.getNotificationList() == null|| event.getNotificationList().getNotificationList() == null) {
            Toast.makeText(LastChoice.this, "No Notification List", Toast.LENGTH_SHORT).show();
            finish();
        }
        notificationList = event.getNotificationList();
        ArrayList<Notification> notifications = notificationList.getNotificationList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean condition1 = false;
                if (!accept.isChecked()) {
                    Toast.makeText(LastChoice.this, "Please Select one option", Toast.LENGTH_SHORT).show();
                } else {
                    // otherwise we get the applicants in the event
                    String id = event.getApplicantList();

                    CRUD.readStatic(id, ApplicantList.class, new ReadCallback<ApplicantList>() {
                        @Override
                        public void onReadSuccess(ApplicantList applicantList) {

                            if (applicantList != null) {
                                ArrayList<String> list = applicantList.getApplicantIds();//get applicant ids
                                AtomicInteger remaining = new AtomicInteger(list.size());
                                for (String user_id : list) {
                                    CRUD.readStatic(user_id, User.class, new ReadCallback<User>() {
                                        @Override
                                        public void onReadSuccess(User user) {//get the user
                                            //we create a new notification

                                            notificationList = event.getNotificationList();
                                            ArrayList<Notification> notifications = notificationList.getNotificationList();

                                            boolean found = false;
                                            for (int i = 0; i < notifications.size(); i++) {
                                                Notification current = notifications.get(i);
                                                if (current.getUser() != null) {
                                                    User current_user = current.getUser();
                                                    String first = current_user.getDeviceId();
                                                    String second = user.getDeviceId();


                                                    if (second.equals(first)) {
                                                        boolean condition1 = false;
                                                        if (accept.isChecked()) {
                                                            condition1 = true;
                                                        } else {
                                                            Toast.makeText(LastChoice.this, "Please Select one option", Toast.LENGTH_SHORT).show();
                                                        }

                                                        if (current.getAccepted()  == false && current.getWaitlistStatus().equals("Accepted")) {
                                                            //update their notification
                                                            current.setUser(user);
                                                            current.setApplicantListId(event.getApplicantList());
                                                            current.setNotificationEventId(event.getEventId());
                                                            current.setNotificationListId(notificationList.getNotificationListId());
                                                            current.setSentStatus("Not Sent");
                                                            current.setWaitlistStatus("Declined");

                                                            CRUD.update(current, new UpdateCallback() {
                                                                @Override
                                                                public void onUpdateSuccess() {
                                                                    notificationList.addNotification(current);
                                                                    CRUD.update(notificationList, new UpdateCallback() {
                                                                        @Override
                                                                        public void onUpdateSuccess() {
                                                                            checkCompletion(remaining.decrementAndGet());
                                                                        }

                                                                        @Override
                                                                        public void onUpdateFailure(Exception e) {

                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onUpdateFailure(Exception e) {
                                                                    checkCompletion(remaining.decrementAndGet());
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onReadFailure(Exception e) {
                                            Toast.makeText(LastChoice.this, "No User Found", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onReadFailure(Exception e) {
                            Toast.makeText(LastChoice.this, "No Applicant List Found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * Checks if all notifications have been processed and updates the database accordingly.
     *
     * @param remainingCount The count of remaining users to process.
     */
    private void checkCompletion(int remainingCount) {
        if (remainingCount == 0) {
            // All users processed
            CRUD.update(notificationList, new UpdateCallback() {
                @Override
                public void onUpdateSuccess() {
                    event.setNotificationList(notificationList);
                    CRUD.update(event, new UpdateCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            Toast.makeText(LastChoice.this, "Users Notified", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LastChoice.this, EventInfo.class);
                            intent.putExtra("Event", event);
                            intent.putExtra("Facility", facility);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        }

                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(LastChoice.this, "Could not update the event", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onUpdateFailure(Exception e) {
                    Toast.makeText(LastChoice.this, "Could not update notification list.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
 }


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

public class Choices extends AppCompatActivity {
    private Button back;
    private Button confirm;
    private CheckBox accept;
    private CheckBox decline;
    private Event event;
    private Facility facility;
    private User user;
    private NotificationList notificationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_all);

        if (getIntent().getSerializableExtra("Facility") == null) {
            Toast.makeText(Choices.this, "No Facility", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("User") == null) {
            Toast.makeText(Choices.this, "No User", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent().getSerializableExtra("Event") == null) {
            Toast.makeText(Choices.this, "No Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        facility = (Facility) getIntent().getSerializableExtra("Facility");
        user = (User) getIntent().getSerializableExtra("User");
        event = (Event) getIntent().getSerializableExtra("Event");

        back = findViewById(R.id.notify_all_back_button);
        confirm = findViewById(R.id.notify_all_confirm_button);
        accept = findViewById(R.id.accept_check);
        decline = findViewById(R.id.declined_check);

        assert event != null;
        if (event.getNotificationList() == null) {
            Toast.makeText(Choices.this, "No Notification List", Toast.LENGTH_SHORT).show();
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
                                                    found = true;
                                                    boolean condition1 = false;
                                                    boolean condition2 = false;
                                                    if (accept.isChecked()) {
                                                        condition1 = true;
                                                    }
                                                    if (decline.isChecked()) {
                                                        condition2 = true;
                                                    }

                                                    if ((condition1 == true && condition2 == true) || (condition1 == false && condition2 == false)) {
                                                        Toast.makeText(Choices.this, "Please Select one option", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        if ((condition1) && (notifications.size() > event.getRegistrationCapacity())) {
                                                            //make sure within waitlist limit
                                                            Toast.makeText(Choices.this, "Current Waitlist Capacity exceeds Registration Capacity", Toast.LENGTH_SHORT).show();
                                                        } else {

                                                            //update their notification
                                                            current.setUser(user);
                                                            current.setApplicantListId(event.getApplicantList());
                                                            current.setNotificationEventId(event.getEventId());
                                                            current.setNotificationListId(notificationList.getNotificationListId());
                                                            String condition = condition1 ? "Accepted" : "Declined";
                                                            current.setSentStatus("Not Sent");
                                                            current.setWaitlistStatus(condition);

                                                            CRUD.update(current, new UpdateCallback() {
                                                                @Override
                                                                public void onUpdateSuccess() {
                                                                    checkCompletion(remaining.decrementAndGet());
                                                                }

                                                                @Override
                                                                public void onUpdateFailure(Exception e) {
                                                                    checkCompletion(remaining.decrementAndGet());

                                                                }
                                                            });
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        if (!found) {
                                                    Notification notify = new Notification();

                                                    CRUD.create(notify, new CreateCallback() {
                                                        @Override
                                                        public void onCreateSuccess() {

                                                            notify.setUser(user);
                                                            notify.setApplicantListId(event.getApplicantList());
                                                            notify.setNotificationEventId(event.getEventId());
                                                            notify.setNotificationListId(notificationList.getNotificationListId());
                                                            notify.setAccepted(false);

                                                            boolean condition1 = false;
                                                            boolean condition2 = false;
                                                            if (accept.isChecked()) {
                                                                condition1 = true;
                                                            }
                                                            if (decline.isChecked()) {
                                                                condition2 = true;
                                                            }

                                                            if ((condition1 == true && condition2 == true) || (condition1 == false && condition2 == false)) {
                                                                Toast.makeText(Choices.this, "Please Select one option", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                if ((condition1) && (notifications.size() > event.getRegistrationCapacity())) {
                                                                    //make sure within waitlist limit
                                                                    Toast.makeText(Choices.this, "Current Waitlist Capacity exceeds Registration Capacity", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    String condition = condition1 ? "Accepted" : "Declined";
                                                                    notify.setSentStatus("Not Sent");
                                                                    notify.setWaitlistStatus(condition);

                                                                    CRUD.update(notify, new UpdateCallback() {
                                                                        @Override
                                                                        public void onUpdateSuccess() {
                                                                            notificationList.addNotification(notify);
                                                                            checkCompletion(remaining.decrementAndGet());
                                                                        }

                                                                        @Override
                                                                        public void onUpdateFailure(Exception e) {
                                                                            checkCompletion(remaining.decrementAndGet());

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCreateFailure(Exception e) {
                                                            Toast.makeText(Choices.this, "Users could not be notified", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }

                                    @Override
                                    public void onReadFailure(Exception e) {
                                        Toast.makeText(Choices.this, "No User Found", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(Choices.this, "No Applicant List Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

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
                            Toast.makeText(Choices.this, "Users Notified", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Choices.this, EventInfo.class);

                            intent.putExtra("Event", event);
                            intent.putExtra("Facility", facility);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        }

                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(Choices.this, "Could not update the event", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onUpdateFailure(Exception e) {
                    Toast.makeText(Choices.this, "Could not update notification list.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}





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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code SelectAtRandom} class represents an activity where the organizer can select a specified number of
 * applicants at random for an event. Notifications are sent to the selected applicants and updated in the system.
 */
public class SelectAtRandom extends AppCompatActivity {
    private EditText capacity;
    private Button back;
    private Button confirm;
    private Event event;
    private String applicantListId;
    private NotificationList notificationList;
    private ArrayList<String> Tracker = new ArrayList<>();
    private int final_value = 0;
    private Facility facility;
    private User user;

    /**
     * Initializes the activity and its UI components. Handles event, facility, and user data validation.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_random_applicants);

        capacity = findViewById(R.id.random_amount);
        back = findViewById(R.id.random_back_button);
        confirm = findViewById(R.id.random_confirm_button);

        if (getIntent().getSerializableExtra("Event") == null) {
            Toast.makeText(SelectAtRandom.this, "No event was passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        event = (Event) getIntent().getSerializableExtra("Event");


        if (getIntent().getSerializableExtra("Facility") == null) {
            Toast.makeText(SelectAtRandom.this, "No facility was passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");


        if (getIntent().getSerializableExtra("User") == null) {
            Toast.makeText(SelectAtRandom.this, "No User was passed", Toast.LENGTH_SHORT).show();
            finish();
        }
        user = (User) getIntent().getSerializableExtra("User");

        applicantListId = "";
        int waitlist_capacity = 0;

        try {
            assert event != null;
            applicantListId = event.getApplicantList();
            waitlist_capacity = event.getRegistrationCapacity();
        } catch (Exception E) {
            Toast.makeText(SelectAtRandom.this, "The applicant list or waitlist capacity is not set", Toast.LENGTH_SHORT).show();
            finish();
        }

        capacity.setText(String.valueOf(waitlist_capacity));

        int alreadyAcc = 0;
        for (Notification notif : event.getNotificationList().getNotificationList()){
            if (notif.getAccepted()){
                alreadyAcc += 1;
            }
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int finalAlreadyAcc = alreadyAcc;
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (applicantListId == null || applicantListId.isEmpty()) {
                    Toast.makeText(SelectAtRandom.this, "Applicant List ID is invalid.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                CRUD.readStatic(applicantListId, ApplicantList.class, new ReadCallback<ApplicantList>() {
                    @Override
                    public void onReadSuccess(ApplicantList applicantList) {
                        if (applicantList == null || applicantList.getApplicantIds().isEmpty()) {
                            Toast.makeText(SelectAtRandom.this, "Applicant list is empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int limit;
                        try {
                            limit = Integer.parseInt(capacity.getText().toString());
                            if (limit <= 0 || limit > applicantList.getApplicantIds().size()) {
                                Toast.makeText(SelectAtRandom.this, "Invalid number of applicants.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(SelectAtRandom.this, "Invalid capacity value.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        notificationList = event.getNotificationList();
                        if (notificationList == null) {
                            Toast.makeText(SelectAtRandom.this, "No notification list.", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        // Process applicants

                        List<String> all_ids = applicantList.getApplicantIds();
                        ArrayList<Notification> notifications = notificationList.getNotificationList();


                        AtomicInteger remaining = new AtomicInteger(all_ids.size());
                        AtomicInteger selected = new AtomicInteger(limit-finalAlreadyAcc);

                        for (String applicantId : all_ids) {

                            for (int i = 0; i < notifications.size(); i++) {
                                Notification current = notifications.get(i);
                                if (current.getUser() != null) {
                                    User current_user = current.getUser();
                                    String first = current_user.getDeviceId();
                                    String second = user.getDeviceId();


                                    if (second.equals(first)) {

                                        CRUD.readStatic(applicantId, User.class, new ReadCallback<User>() {
                                            @Override
                                            public void onReadSuccess(User user) {
                                                if (user != null) {

                                                    current.setUser(user);
                                                    current.setApplicantListId(applicantListId);
                                                    current.setNotificationEventId(event.getEventId());
                                                    current.setNotificationListId(notificationList.getNotificationListId());
                                                    String condition;
                                                    if (checkSelected(selected.decrementAndGet())){
                                                        condition = "Accepted";
                                                    }
                                                    else{
                                                        condition = "Declined";
                                                    }
                                                    current.setSentStatus("Not Sent");
                                                    current.setWaitlistStatus(condition);
                                                    current.setAccepted(false);
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
                                                } else {
                                                    checkCompletion(remaining.decrementAndGet());
                                                }
                                            }

                                            @Override
                                            public void onReadFailure(Exception e) {
                                                checkCompletion(remaining.decrementAndGet());
                                            }
                                        });
                                    } else {
                                        CRUD.readStatic(applicantId, User.class, new ReadCallback<User>() {
                                            @Override
                                            public void onReadSuccess(User user) {
                                                if (user != null) {
                                                    Notification notify = new Notification();

                                                    CRUD.create(notify, new CreateCallback() {
                                                        @Override
                                                        public void onCreateSuccess() {

                                                            notify.setUser(user);
                                                            notify.setApplicantListId(applicantListId);
                                                            notify.setNotificationEventId(event.getEventId());
                                                            notify.setNotificationListId(notificationList.getNotificationListId());
                                                            String condition;
                                                            if (checkSelected(selected.decrementAndGet())){
                                                                condition = "Accepted";
                                                            }
                                                            else{
                                                                condition = "Declined";
                                                            }
                                                            notify.setSentStatus("Not Sent");
                                                            notify.setWaitlistStatus(condition);
                                                            notify.setAccepted(false);

                                                            CRUD.update(notify, new UpdateCallback() {
                                                                @Override
                                                                public void onUpdateSuccess() {
                                                                    notificationList.addNotification(notify);

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

                                                        @Override
                                                        public void onCreateFailure(Exception e) {
                                                            checkCompletion(remaining.decrementAndGet());
                                                        }
                                                    });
                                                } else {
                                                    checkCompletion(remaining.decrementAndGet());
                                                }
                                            }

                                            @Override
                                            public void onReadFailure(Exception e) {
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
                        Toast.makeText(SelectAtRandom.this, "Error retrieving applicant list.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean checkSelected(int selected){
        return selected >= 0;
    }
    /**
     * Updates the notification and event list once all users are processed.
     *
     * @param remainingCount The remaining number of users to process.
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
                                    Toast.makeText(SelectAtRandom.this, "Users Notified!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SelectAtRandom.this, EventInfo.class);

                                    intent.putExtra("Event", event);
                                    intent.putExtra("Facility", facility);
                                    intent.putExtra("User", user);
                                    startActivity(intent);
                                }

                                @Override
                                public void onUpdateFailure(Exception e) {
                                    Toast.makeText(SelectAtRandom.this, "Could not update the event", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(SelectAtRandom.this, "Could not update notification list.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
}






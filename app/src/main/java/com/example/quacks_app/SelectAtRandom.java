package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/*
Selecting a specified number of participants per notification round
 */
public class SelectAtRandom extends AppCompatActivity {
    private EditText capacity;
    private Button back;
    private Button confirm;
    private FirebaseFirestore db;
    private ArrayList<String> Tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_random_applicants);

        db = FirebaseFirestore.getInstance();
        Tracker = new ArrayList<>();
        capacity = findViewById(R.id.amount);
        back = findViewById(R.id.back_button);
        confirm = findViewById(R.id.confirm_button);

        if (getIntent().getSerializableExtra("Event") == null) {
            finish();
        }
        Event event = (Event) getIntent().getSerializableExtra("Event");

        if (event.getApplicantList() == null) {
            Toast.makeText(SelectAtRandom.this, "Error loading applicant list ID", Toast.LENGTH_SHORT).show();
            finish();
        }
        db = FirebaseFirestore.getInstance();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String applicantListId = event.getApplicantList();

                if (applicantListId == null || applicantListId.isEmpty()) {
                    Toast.makeText(SelectAtRandom.this, "Applicant List ID is invalid.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference docRef = db.collection("ApplicantList").document(applicantListId);

                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            ApplicantList applicantList = document.toObject(ApplicantList.class);

                            if (applicantList != null) {
                                int limit;
                                try {
                                    limit = Integer.parseInt(capacity.getText().toString());
                                    if (limit <= 0 || limit > applicantList.getApplicantIds().size()) {
                                        Toast.makeText(SelectAtRandom.this, "Invalid number of applicants", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (NumberFormatException e) {
                                    Toast.makeText(SelectAtRandom.this, "Invalid capacity value.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                List<String> ids = applicantList.getApplicantIds().subList(0, limit);
                                int counter = 0;
                                for (String applicantId : ids) {
                                    db.collection("User").document(applicantId).get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    User user = documentSnapshot.toObject(User.class);
                                                    if (user != null) {
                                                        UserProfile profile = user.getUserProfile();
                                                        Notification notify = new Notification(user.getDeviceId());
                                                        notify.setNotification(event);
                                                        applicantList.removeUser(user);

                                                    }
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(SelectAtRandom.this, "Error notifying users.", Toast.LENGTH_SHORT).show();
                                            });
                                            Tracker.add("1");
                                }
                                if (!Tracker.isEmpty()){
                                    Toast.makeText(SelectAtRandom.this, "Users Notified.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SelectAtRandom.this, OrganizerHomepage.class);
                                    intent.putExtra("Event", event);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(SelectAtRandom.this, "Error notifying users.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }



                            } else {
                                Toast.makeText(SelectAtRandom.this, "Applicant list is empty.", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(SelectAtRandom.this, "Applicant list document does not exist.", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(SelectAtRandom.this, "Failed to retrieve applicant list.", Toast.LENGTH_SHORT).show();
                    }


                }).addOnFailureListener(e -> {
                    Toast.makeText(SelectAtRandom.this, "Error retrieving applicant list.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}




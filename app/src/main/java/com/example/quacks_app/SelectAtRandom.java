package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SelectAtRandom extends AppCompatActivity {
    private EditText capacity;
    private Button back;
    private Button confirm;
    private ApplicantList applicantList;
    private Event event;
    private FirebaseFirestore db;
    private CollectionReference applicantRef;
    private ApplicantList applicant_list;
    private CollectionReference userRef;
    private CollectionReference eventRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_random_applicants);

        db = FirebaseFirestore.getInstance();

        capacity = findViewById(R.id.amount);
        back = findViewById(R.id.back_button);
        confirm = findViewById(R.id.confirm_button);

        if (getIntent().getSerializableExtra("Event") == null) {
            finish();
        }
        event = (Event) getIntent().getSerializableExtra("Event");

        if (event.getApplicantList() == null) {
            Toast.makeText(SelectAtRandom.this, "Error loading applicant list ID", Toast.LENGTH_SHORT).show();
            finish();
        }




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = FirebaseFirestore.getInstance();
                applicantRef = db.collection("ApplicantList");


                applicantRef.whereEqualTo("applicantIds", event.getApplicantList()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Toast.makeText(SelectAtRandom.this, "We have an application List", Toast.LENGTH_SHORT).show();

                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                ApplicantList applicantList = document.toObject(ApplicantList.class);

                                if (applicantList != null) {
                                    ArrayList<String> lotterwinners = applicantList.getApplicantIds();

                                    //we have our lotterwinners, now for each of them we need to find the user and update their profile

                                    for (String winnerId : lotterwinners) {

                                        userRef = db.collection("User");

                                        userRef.whereEqualTo("deviceId", winnerId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                Toast.makeText(SelectAtRandom.this, "We found a user", Toast.LENGTH_SHORT).show();

                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                                        User user = document.toObject(User.class);

                                                        if (user != null){

                                                            UserProfile profile = user.getUserProfile();

                                                            profile.setNotification(event);
                                                            applicantList.removeUser(user);

                                                        }
                                                    }

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SelectAtRandom.this, "No User", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }


                                }

                            }

                        }else{
                            Toast.makeText(SelectAtRandom.this, "The waiting list was empty", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SelectAtRandom.this, "We dont have an application List"+event.getApplicantList(), Toast.LENGTH_SHORT).show();
                    }
                });

            }});
    }
}


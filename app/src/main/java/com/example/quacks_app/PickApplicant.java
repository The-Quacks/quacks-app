package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class PickApplicant extends AppCompatActivity {
    private ImageButton homepage;
    private ImageButton profile;
    private ImageButton search;
    private Event event;

    private FirebaseFirestore db;
    private CollectionReference eventsRef;


    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private ApplicantArrayAdapter applicantArrayAdapter;
    private ArrayList<User> dataList;
    private ApplicantList usered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_applicants);

        eventList = findViewById(R.id.app_list);

        if( getIntent().getSerializableExtra("Event") == null){
            finish();
        }


        dataList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        applicantArrayAdapter = new ApplicantArrayAdapter(this, dataList);
        eventList.setAdapter(applicantArrayAdapter);
        eventsRef = db.collection("ApplicantList");



        eventsRef.whereEqualTo("ApplicantList", event.getApplicantList()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                                                  @Override
                                                                                                  public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                                                                                      if (e != null) {
                                                                                                          Toast.makeText(PickApplicant.this, "Failed to load applicants", Toast.LENGTH_SHORT).show();
                                                                                                          return;
                                                                                                      }
                                                                                                      dataList.clear();
                                                                                                      // Loop through each document in the snapshot
                                                                                                      if (value != null){
                                                                                                          for (DocumentSnapshot document : value) {
                                                                                                              ApplicantList applicantList = document.toObject(ApplicantList.class);
                                                                                                              if (applicantList != null) {
                                                                                                                  ArrayList<String> strings = applicantList.getApplicantIds();
                                                                                                                  for (String id : strings) {
                                                                                                                      CollectionReference userref = db.collection("User");
                                                                                                                      userref.whereEqualTo("deviceId", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                                                          @Override
                                                                                                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                                                              if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                                                                                                                                  for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                                                                                      User agent = document.toObject(User.class);
                                                                                                                                      if (agent != null) {
                                                                                                                                          dataList.add(agent);
                                                                                                                                      }
                                                                                                                                  }
                                                                                                                              }
                                                                                                                          }
                                                                                                                      });
                                                                                                                  }
                                                                                                              }
                                                                                                      }


                                                                                                      }
                                                                                                      applicantArrayAdapter.notifyDataSetChanged();
                                                                                                  }
                                                                                              });



        //This is the bottom of the page directory
        homepage = findViewById(R.id.house);
        profile = findViewById(R.id.person);
        search = findViewById(R.id.search);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PickApplicant.this, OrganizerHomepage.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PickApplicant.this, ViewOrganizer.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Already here
                Toast.makeText(PickApplicant.this, "Already On Searchpage!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.quacks_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
This is the individual edit and delete screen to view events, can be multipurposed for not just admin
 */

public class AdminViewOrganizer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_admin);
        EdgeToEdge.enable(this);

        ApplicantList testSingle;



        Bundle bundle = getIntent().getExtras();
        Event editEvent = (Event) bundle.getSerializable("value");
        String id = bundle.getString("id");

        String appList = editEvent.getApplicantList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("ApplicantList").document(appList);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ApplicantList testSingle = document.toObject(ApplicantList.class);

                        EditText name = findViewById(R.id.Name);
                        EditText capacity = findViewById(R.id.capacity);
                        EditText available = findViewById(R.id.availability);
                        EditText location = findViewById(R.id.location);
                        String nameVal = "Name: " + editEvent.getDescription();
                        String capval = "Capacity: "+ testSingle.getLimit().toString();
                        String availval = "Availability: " + (testSingle.getLimit() - testSingle.getApplicantIds().size());
                        String locVal = "Location: " + editEvent.getFacility().getLocation();

                        name.setText(nameVal);
                        capacity.setText(capval);
                        available.setText(availval);
                        location.setText(locVal);

                        Button delete = findViewById(R.id.delete_button);
                        Button edit = findViewById(R.id.edit_button);
                        Button back = findViewById(R.id.back_button);

                        delete.setOnClickListener(view -> {
                            CRUD<Event> delEvent = new CRUD<>(Event.class);
                            DeleteCallback delCall = new DeleteCallback(){
                                @Override
                                public void onDeleteSuccess() {
                                    System.out.println("Document successfully deleted!");
                                }

                                @Override
                                public void onDeleteFailure(Exception e) {
                                    System.err.println("Error deleting document: " + e.getMessage());
                                }
                            };
                            delEvent.delete(id, delCall);
                            CRUD<ApplicantList> delAppList = new CRUD<>(ApplicantList.class);
                            delAppList.delete(editEvent.getApplicantList(), delCall);
                        });

                        edit.setOnClickListener(view -> {

                        });

                        back.setOnClickListener(view -> {
                            finish();
                        });


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



    }
}

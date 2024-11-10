package com.example.quacks_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button delete = findViewById(R.id.delete_button);
        Button edit = findViewById(R.id.edit_button);

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


        String appList = editEvent.getApplicantList();

        CRUD<Event> crud = new CRUD<>(Event.class);
        crud.readLive(id, new ReadCallback<Event>() {
            @Override
            public void onReadSuccess(Event data) {
                //ApplicantList testSingle = document.toObject(ApplicantList.class);

                EditText name = findViewById(R.id.Name);
                EditText capacity = findViewById(R.id.capacity);
                EditText available = findViewById(R.id.availability);
                EditText location = findViewById(R.id.location);
                String nameVal = "Name: " + data.getDescription();
                String capval = "Capacity: "+ "";
                String availval = "Availability: " + "";
                String locVal = "Location: " + "";

                name.setText(nameVal);
                capacity.setText(capval);
                available.setText(availval);
                location.setText(locVal);
            }

            @Override
            public void onReadFailure(Exception e) {
                Log.d(TAG, "get failed with ", e);

            }
        });
    }
}

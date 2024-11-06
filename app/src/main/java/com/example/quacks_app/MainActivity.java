package com.example.quacks_app;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Main Activity: Entrant Page: Identifies ID and redirects to Homepage
 */
public class MainActivity extends AppCompatActivity {
    private Button create_button;
    private Facilities facilities;
    private Context context;
    private String android_id;
    private boolean ROLE = false;
    private int created;
    private User user;
    private String deviceId;
    private RepoModel repoModel;
    private ArrayList<Role> roles;


    /**
     * On Create Method displays activity main and sets on click listeners
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Firebase implementation when merging
        FirebaseApp.initializeApp(this);
        facilities = new Facilities();
        facilities.createNew();
        user = new User();

        //Clicking on create facility button
        create_button = findViewById(R.id.CREATE_FACILITY);
        create_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {//this is where they click the facility button
                //Get the user ID, and check if they are in the database
                //For testing purposes
                // Proceed with the UI update or next steps after successful read
                if (!ROLE) {
                    Intent intent = new Intent(MainActivity.this, CreateFacility.class);
                    startActivityForResult(intent, 1); // Pass the data if needed
                }else {

                    // Log error for debugging
                    Toast.makeText(MainActivity.this, "Device Id Not Recognized", Toast.LENGTH_SHORT).show();

                    // Provide feedback to user on failure (e.g., navigate to another activity)
                    Intent new_intent = new Intent(MainActivity.this, OrganizerHomepage.class);
                    startActivity(new_intent);
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1)&&(resultCode == RESULT_OK)&&(data != null)){
            Facility new_facility = (Facility) data.getSerializableExtra("Facility");
            if (new_facility!=null){
                ROLE = true;
                facilities.updateFacilities(new_facility);
                Toast.makeText(MainActivity.this, "Facility Created!", Toast.LENGTH_SHORT).show();

                Intent new_intent = new Intent(MainActivity.this, OrganizerHomepage.class);
                new_intent.putExtra("Facility", new_facility);
                startActivity(new_intent);
            }
        }
    }

}

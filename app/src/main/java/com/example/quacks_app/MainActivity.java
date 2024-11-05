package com.example.quacks_app;
import android.content.Context;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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

    //Firebase implementation VV
    //private FirebaseFirestore db;
    //private CollectionReference facilitiesRef;
    //private User user;
    //private String deviceId;



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
        facilities = new Facilities();
        facilities.createNew();

        //Clicking on create facility button
        create_button = findViewById(R.id.CREATE_FACILITY);
        create_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {//this is where they click the facility button
                //Get the user ID, and check if they are in the database
                //For testing purposes



                if (!ROLE) {//while the role of the user id is false it will execute this
                    //Not in database--creating a facility
                    Intent intent = new Intent(MainActivity.this, CreateFacility.class);
                    startActivityForResult(intent, 1);

                } else {
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

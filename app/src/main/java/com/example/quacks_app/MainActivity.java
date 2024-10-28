package com.example.quacks_app;
import android.content.Context;
import android.provider.Settings.Secure;


import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button create_button = findViewById(R.id.CREATE_FACILITY);
    private Context context;
    private String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

    /**
     * On Create Method displays activity main and sets on click listeners
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Clicking on create facility button

        create_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Get database references ** this will probably need to be modified once I know the firebase structure

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usernameReference =  databaseReference.child("Users");
                DatabaseReference idReference = usernameReference.child("Id");

                idReference.child(android_id).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){//then they are in the database

                            //If the organizer has a profile: Goes to the create facility page
                            Intent intent = new Intent(MainActivity.this, CreateFacility.class);
                            startActivity(intent);

                        }
                        else{

                            //If the organizer does not have a profile: make a profile
                            Intent intent = new Intent(MainActivity.this, MakeOrganizerProfile.class);
                            startActivity(intent.putExtra("android_id", android_id));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast toast = Toast.makeText(context.getApplicationContext(), "Identification Failed", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });



            }

        });

    }
}
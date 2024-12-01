package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class OpenRegistration  extends AppCompatActivity {
    private Button back;
    private Button confirm;
    private EditText capacity;
    private Event event;
    private int flagger = 0;
    private int old;
    private Facility facility;
    private User user;

    /*
    Opens Registration for applicants
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_registration);

        capacity = findViewById(R.id.amount);
        back = findViewById(R.id.back_button);
        confirm = findViewById(R.id.confirm_button);



        if (getIntent().getSerializableExtra("Event") == null){
            finish();
        }
        event = (Event) getIntent().getSerializableExtra("Event");
        if (getIntent().getSerializableExtra("Facility") == null){
            finish();
        }
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        if (getIntent().getSerializableExtra("User") == null){
            finish();
        }
        user = (User) getIntent().getSerializableExtra("User");


        //check if there is an applicant list already made
        if (event.getRegistration()){
            Toast.makeText(OpenRegistration.this, "This event is already Open!", Toast.LENGTH_SHORT).show();
            finish();
        }

        int number = event.getWaitlistCapacity();
        try {
            capacity.setText(String.valueOf(number));
        } catch(Exception E){
            Toast.makeText(OpenRegistration.this, "There was no waitlist capacity set for this event", Toast.LENGTH_SHORT).show();
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
                //this is where they have entered in an amount want to close

                String value = capacity.getText().toString().trim();
                int parsedValue = 0;
                try {
                    parsedValue = Integer.parseInt(value);

                } catch (Exception e) {
                    Toast.makeText(OpenRegistration.this, "Capacity needs to be an integer value", Toast.LENGTH_SHORT).show();
                    flagger = 1;
                }

                if (flagger == 0) {
                    capacity.setText(value);

                    ApplicantList app = new ApplicantList();
                    if (event.getFinal_list() != null) {

                        app = event.getFinal_list();
                        String ID = app.getDocumentId();
                        event.setApplicantList(ID);
                        app.setLimit(parsedValue);
                        event.setWaitlistCapacity(parsedValue);
                        event.setApplicantList(app.getDocumentId());
                        CRUD.update(event, new UpdateCallback() {
                            @Override
                            public void onUpdateSuccess() {
                                Toast.makeText(OpenRegistration.this, "Registration is Now Open!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OpenRegistration.this, EventInfo.class);
                                intent.putExtra("Event", event);
                                intent.putExtra("Facility", facility);
                                intent.putExtra("User", user);
                                startActivity(intent);
                            }

                            @Override
                            public void onUpdateFailure(Exception e) {
                                Toast.makeText(OpenRegistration.this, "Failed to update event.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        app.setLimit(parsedValue);
                        event.setWaitlistCapacity(parsedValue);
                        String randomId = UUID.randomUUID().toString();
                        app.setDocumentId(randomId);


                        ApplicantList finalApp = app;
                        CRUD.create(app, new CreateCallback() {
                            @Override
                            public void onCreateSuccess() {
                                event.setApplicantList(finalApp.getDocumentId());
                                CRUD.update(event, new UpdateCallback() {
                                    @Override
                                    public void onUpdateSuccess() {
                                        Toast.makeText(OpenRegistration.this, "Registration is Now Open!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OpenRegistration.this, EventInfo.class);
                                        intent.putExtra("Event", event);
                                        intent.putExtra("Facility", facility);
                                        intent.putExtra("User", user);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onUpdateFailure(Exception e) {
                                        Toast.makeText(OpenRegistration.this, "Failed to update event.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onCreateFailure(Exception e) {
                                Toast.makeText(OpenRegistration.this, "Failed to open registration.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    } else{
                        Toast.makeText(OpenRegistration.this, "Validation Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                        flagger = 0;
                    }
                }
            });
        }
    }

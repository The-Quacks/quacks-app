package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OpenRegistration  extends AppCompatActivity {
    private Button back;
    private Button confirm;
    private EditText capacity;
    private Event event;
    private int flagger = 0;

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
        }else{
            event = (Event) getIntent().getSerializableExtra("Event");
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

                    app.setLimit(parsedValue);
                    CRUD.create(app, new CreateCallback() {
                        @Override
                        public void onCreateSuccess() {
                            event.setApplicantList(app.getDocumentId());
                            CRUD.update(event, new UpdateCallback() {
                                @Override
                                public void onUpdateSuccess() {
                                    Toast.makeText(OpenRegistration.this, "Registration is Now Open!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(OpenRegistration.this, OrganizerHomepage.class);
                                    startActivity(intent);
                                    finish();
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

                } else {
                    Toast.makeText(OpenRegistration.this, "Validation Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                    flagger = 0;
                }
            }
        });
    }
}

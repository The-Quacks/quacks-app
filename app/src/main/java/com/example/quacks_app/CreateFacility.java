package com.example.quacks_app;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateFacility extends AppCompatActivity {
    private EditText bussiness_name;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText location;
    private EditText phone_number;
    private EditText facility_deets;
    private EditText accessibility;
    private Facility new_facility;
    private Button confirm;
    private Button back;
    private int round_one = 0;
    private int round_two = 0;
    private int round_three = 0;
    private int round_four = 0;
    private int round_five = 0;
    private int breaker = 0;
    private int round_six = 0;
    private int round_7 = 0;
    private int round_8 = 0;
    private Context context;
    private User user;


    /**
     * This is where they can create a Facility Page
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_facility);


        user = (User) getIntent().getSerializableExtra("User");

        context = this;
        //on back click nothing is saved
        back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //on confirm click

                new_facility = new Facility();

                bussiness_name = findViewById(R.id.Name);
                location = findViewById(R.id.location);
                phone_number = findViewById(R.id.contact_info);
                facility_deets = findViewById(R.id.facility_details);
                accessibility = findViewById(R.id.accessibility);
                username = findViewById(R.id.username);
                password = findViewById(R.id.password);
                email = findViewById(R.id.email);


                String test_1 = bussiness_name.getText().toString();
                //Doesn't matter what business name is
                if (test_1.length() >= 40) {
                    Toast.makeText(CreateFacility.this, "Please enter a Business Name that's 40 characters or less", Toast.LENGTH_SHORT).show();

                } else {
                    round_one = 1;
                }


                String test_2 = location.getText().toString();
                if (test_2.length() <= 0) {
                    Toast.makeText(CreateFacility.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                } else {
                    round_two = 1;
                }


                String test_3 = phone_number.getText().toString().trim();

                if (test_3.length() >= 11) {
                    //780-289-6694 format
                    char firstDash = test_3.charAt(3);
                    char secondDash = test_3.charAt(7);

                    //converting the rest to integers
                    String substring1 = test_3.substring(0, 3);
                    String substring2 = test_3.substring(4, 7);
                    String substring3 = test_3.substring(8, 11);

                    int wrong = 0;
                    try {
                        Integer.parseInt(substring1);
                        Integer.parseInt(substring2);
                        Integer.parseInt(substring3);
                        //Phone number needs to comply with the format ###-###-####
                    } catch (NumberFormatException e) {
                        Toast.makeText(CreateFacility.this, "substring1" + substring1 + "substring2" + substring2 + "substring3" + substring3, Toast.LENGTH_SHORT).show();
                        wrong = 1;
                    }
                    if (wrong == 0) {
                        round_three = 1;
                    }

                } else {

                    Toast.makeText(CreateFacility.this, "Phone number needs to comply with the format ###-###-####", Toast.LENGTH_SHORT).show();
                }

                String test_4 = facility_deets.getText().toString();
                if (!test_4.isEmpty()) {
                    round_four = 1;
                } else {
                    Toast.makeText(CreateFacility.this, "Please Enter Some details about your Facility", Toast.LENGTH_SHORT).show();
                }

                String test_5 = accessibility.getText().toString();
                if ((test_5.contains("Yes") || test_5.contains("No") || test_5.contains("yes") || test_5.contains("no"))) {
                    round_five = 1;
                } else {
                    Toast.makeText(CreateFacility.this, "Accessibility must be specified with at least Yes/No", Toast.LENGTH_SHORT).show();
                }


                String test_6 = username.getText().toString().trim();
                if ((test_6.length() >= 1)) {
                    round_six = 1;
                }

                String test_7 = email.getText().toString().trim();
                if (test_7.contains("@") && (test_7.contains(".com") || test_7.contains(".ca"))) {
                    round_7 = 1;
                } else {
                    Toast.makeText(CreateFacility.this, "Email has form abc@domain.com", Toast.LENGTH_SHORT).show();
                }

                String test_8 = password.getText().toString().trim();
                if (test_8.length() >= 6 && test_8.matches(".*[A-Z].*")) {
                    round_8 = 1;
                } else {
                    Toast.makeText(CreateFacility.this, "Passwords need at least 6 letters and one capital", Toast.LENGTH_SHORT).show();
                }

                if (round_one == 1 && round_two == 1 && round_three == 1 && round_four == 1 && round_five == 1 && round_six == 1 && round_7 == 1 && round_8 == 1) {


                    new_facility = new Facility();

                    new_facility.setName(test_1);

                    Geocoder geocoder = new Geocoder(CreateFacility.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(test_2, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address addressObj = addresses.get(0);
                            double latitude = addressObj.getLatitude();
                            double longitude = addressObj.getLongitude();

                            GeoPoint geoPoint = new GeoPoint(latitude, longitude);

                            new_facility.setGeoPoint(geoPoint);

//                        Toast.makeText(MakeOrganizerProfile.this, "Geocoded Location: Latitude " + latitude + ", Longitude " + longitude, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateFacility.this, "Address not found. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.getMessage();
                        Toast.makeText(CreateFacility.this, "Geocoding failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    new_facility.setContactInfo(test_3);
                    new_facility.setDetails(test_4);
                    new_facility.setaccessibilityStat(test_5);

                    UserProfile userProfile = new UserProfile(test_6, test_7, test_3); // Example user profile
                    ArrayList<Role> roles = new ArrayList<>();
                    roles.add(Role.ORGANIZER);


                    user.setUserProfile(userProfile);
                    user.setRoles(roles);


                    CRUD.update(user, new UpdateCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            new_facility.setOrganizerId(user.getDocumentId());
                            EventList new_event_list = new EventList();
                            CRUD.create(new_event_list, new CreateCallback() {
                                @Override
                                public void onCreateSuccess() {
                                    String evListId = new_event_list.getDocumentId();
                                    new_facility.setEventListId(evListId);
                                    CRUD.create(new_facility, new CreateCallback() {
                                        @Override
                                        public void onCreateSuccess() {
                                            Toast.makeText(CreateFacility.this, "Profile Created!", Toast.LENGTH_SHORT).show();
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("User", user);
                                            resultIntent.putExtra("Facility", new_facility);
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                        }
                                        @Override
                                        public void onCreateFailure(Exception e) {
                                            Toast.makeText(CreateFacility.this, "Error creating facility Created!", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                                @Override
                                public void onCreateFailure(Exception e) {

                                }
                            });
                        }
                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(CreateFacility.this, "Error creating user, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(CreateFacility.this, "Error creating user, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

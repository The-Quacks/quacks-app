package com.example.quacks_app;

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

import java.util.List;
import java.util.Locale;


public class MakeOrganizerProfile extends AppCompatActivity {// or the correct XML layout file
    private Button back;
    private Button confirm;
    private Facility facility;
    private EditText bussiness_name;
    private EditText location;
    private EditText phone_number;
    private EditText facility_deets;
    private EditText accessibility;
    private EditText emailed;
    private EditText usernamed;
    private EditText password;
    private Facility new_facility;
    private User current;
    private UserProfile userProfile;
    private int round_one = 0;
    private int round_two = 0;
    private int round_three = 0;
    private int round_four = 0;
    private int round_five = 0;
    private int round_six = 0;
    private int round_7 = 0;
    private int round_8 = 0;

    /**
     * Otherwise known as the Edit Facility/Organizer profile page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_organizer_profile);// or the correct XML layout file

        new_facility = (Facility) getIntent().getSerializableExtra("Facility");
        current = (User) getIntent().getSerializableExtra("User");
        UserProfile profile = current.getUserProfile();

        bussiness_name = findViewById(R.id.edit_Name);
        location = findViewById(R.id.edit_location);
        phone_number = findViewById(R.id.edit_contact_info);
        facility_deets = findViewById(R.id.edit_facility_details);
        accessibility = findViewById(R.id.edit_accessibility);
        emailed = findViewById(R.id.edit_email);
        usernamed = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);

        // Setting with prior data
        bussiness_name.setText(new_facility.getName());
        location.setText(new_facility.getGeoPointString(MakeOrganizerProfile.this));
        phone_number.setText(new_facility.getPhone());
        facility_deets.setText(new_facility.getDetails());
        accessibility.setText(new_facility.getAccessible());
        emailed.setText(profile.getEmail());
        usernamed.setText(profile.getUserName());



        //on back click nothing is saved
        back = findViewById(R.id.edit_back_button);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm = findViewById(R.id.edit_confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //on confirm click
                String test_1 = bussiness_name.getText().toString();
                //Doesn't matter what business name is
                if (test_1.length() >= 40 ){
                    Toast.makeText(MakeOrganizerProfile.this, "Please enter a Business Name that's 40 characters or less", Toast.LENGTH_SHORT).show();

                }
                else {
                    round_one = 1;
                }


                String address = location.getText().toString().trim();
                if (address.length() <= 0){
                    Toast.makeText(MakeOrganizerProfile.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
                else{
                    round_two = 1;
                }

//              Geocoding functionality:
                Geocoder geocoder = new Geocoder(MakeOrganizerProfile.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address addressObj = addresses.get(0);
                        double latitude = addressObj.getLatitude();
                        double longitude = addressObj.getLongitude();

                        GeoPoint geoPoint = new GeoPoint(latitude, longitude);

                        new_facility.setGeoPoint(geoPoint);
                    } else {
                        Toast.makeText(MakeOrganizerProfile.this, "Address not found. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.getMessage();
                    Toast.makeText(MakeOrganizerProfile.this, "Failed to find coordinates: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }



                String test_3 = phone_number.getText().toString().trim();

                if (test_3.length()>=11) {
                    //780-289-6694 format
                    char firstDash = test_3.charAt(3);
                    char secondDash = test_3.charAt(7);

                    //converting the rest to integers
                    String substring1 = test_3.substring(0, 3);
                    String substring2 = test_3.substring(4, 7);
                    String substring3 = test_3.substring(8, 11);

                    int wrong = 0;
                    try{
                        Integer.parseInt(substring1);
                        Integer.parseInt(substring2);
                        Integer.parseInt(substring3);
                        //Phone number needs to comply with the format ###-###-####
                    } catch(NumberFormatException e){
                        Toast.makeText(MakeOrganizerProfile.this, "substring1"+substring1+"substring2"+substring2+"substring3"+substring3, Toast.LENGTH_SHORT).show();
                        wrong = 1;
                    }
                    if (wrong == 0){
                        round_three = 1;
                    }

                }else{

                    Toast.makeText(MakeOrganizerProfile.this, "Phone number needs to comply with the format ###-###-####", Toast.LENGTH_SHORT).show();
                }

                String test_4 = facility_deets.getText().toString();
                if (!test_4.isEmpty()){
                    round_four = 1;
                }
                else{
                    Toast.makeText(MakeOrganizerProfile.this, "Please Enter Some details about your Facility", Toast.LENGTH_SHORT).show();
                }

                String test_5 = accessibility.getText().toString();
                if ((test_5.contains("Yes")||test_5.contains("No")||test_5.contains("yes")||test_5.contains("no"))){
                    round_five = 1;
                }else{
                    Toast.makeText(MakeOrganizerProfile.this, "Accessibility must be specified with at least Yes/No", Toast.LENGTH_SHORT).show();
                }


                String test_6 = usernamed.getText().toString().trim();
                if ((test_6.length()>=1)) {
                    round_six = 1;
                }

                String test_7 = emailed.getText().toString().trim();
                if (test_7.contains("@") && (test_7.contains(".com")||test_7.contains(".ca"))){
                    round_7 = 1;
                }else {
                    Toast.makeText(MakeOrganizerProfile.this, "Email has form abc@domain.com", Toast.LENGTH_SHORT).show();
                }

                String test_8 =password.getText().toString().trim();
                if (test_8.length() >= 6 && test_8.matches(".*[A-Z].*")) {
                    round_8 = 1;
                }else {
                    Toast.makeText(MakeOrganizerProfile.this, "Passwords need at least 6 letters and one capital", Toast.LENGTH_SHORT).show();
                }

                if (round_one == 1 && round_two == 1 && round_three == 1 && round_four == 1 && round_five == 1 && round_six == 1 &&round_7 == 1 && round_8 == 1) {

                    new_facility.setName(test_1);
                    new_facility.setContactInfo(test_3);
                    new_facility.setDetails(test_4);
                    new_facility.setaccessibilityStat(test_5);
                    new_facility.setOrganizerId(current.getDocumentId());

                    CRUD.createOrUpdate(new_facility, new UpdateCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            Toast.makeText(MakeOrganizerProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onUpdateFailure(Exception e) {
                            Toast.makeText(MakeOrganizerProfile.this, "Error creating user, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

                    UserProfile profile = current.getUserProfile();
                    profile.setEmail(test_7);
                    profile.setUserName(test_6);
                    profile.setPhoneNumber(test_3);
                    current.setUserProfile(profile);
                }else{
                    Toast.makeText(MakeOrganizerProfile.this, "Error creating user, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


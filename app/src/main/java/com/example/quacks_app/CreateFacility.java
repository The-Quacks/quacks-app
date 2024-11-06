package com.example.quacks_app;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateFacility extends AppCompatActivity {
    private EditText bussiness_name;
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

    /**
     * This is where they can create a Facility Page
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_facility);


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



                String test_1 = bussiness_name.getText().toString();
                //Doesn't matter what business name is
                if (test_1.length() >= 40 ){
                    Toast.makeText(CreateFacility.this, "Please enter a Business Name that's 40 characters or less", Toast.LENGTH_SHORT).show();

                }
                else {
                    round_one = 1;
                }


                String test_2 = location.getText().toString();
                if (test_2.length() <=1){
                    breaker = 1;
                    Toast.makeText(CreateFacility.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
                else{
                    round_two = 1;
                }



                String test_3 = phone_number.getText().toString();

                if (test_3.length()>=11&& breaker == 0) {
                    //780-289-6694 format
                    char firstDash = test_3.charAt(3);
                    char secondDash = test_3.charAt(7);

                    //converting the rest to integers
                    String substring1 = test_3.substring(0, 2);
                    String substring2 = test_3.substring(4, 6);
                    String substring3 = test_3.substring(8, 11);

                    int wrong = 0;
                    try{
                        Integer.parseInt(substring1);
                        Integer.parseInt(substring2);
                        Integer.parseInt(substring3);
                        //Phone number needs to comply with the format ###-###-####
                    } catch(NumberFormatException e){
                        breaker = 1;
                        Toast.makeText(CreateFacility.this, "substring1"+substring1+"substring2"+substring2+"substring3"+substring3, Toast.LENGTH_SHORT).show();
                        wrong = 1;
                    }
                    if (wrong == 0){
                        round_three = 1;
                    }

                }else{
                    breaker = 1;
                    Toast.makeText(CreateFacility.this, "Phone number needs to comply with the format ###-###-####", Toast.LENGTH_SHORT).show();
                }

                String test_4 = facility_deets.getText().toString();
                if (!test_4.isEmpty()&& breaker == 0){
                    round_four = 1;
                }
                else{
                    breaker = 1;
                    Toast.makeText(CreateFacility.this, "Please Enter Some details about your Facility", Toast.LENGTH_SHORT).show();
                }

                String test_5 = accessibility.getText().toString();
                if ((test_5.contains("Yes")||test_5.contains("No")||test_5.contains("yes")||test_5.contains("no"))&& breaker == 0){
                    round_five = 1;
                }else{
                    breaker = 1;
                    Toast.makeText(CreateFacility.this, "Accessibility must be specified with at least Yes/No", Toast.LENGTH_SHORT).show();
                }

                if (round_one == 1 && round_two == 1 && round_three == 1 && round_four == 1 && round_five == 1 && breaker == 0){
                    new_facility.setName(test_1);
                    new_facility.setLocation(test_2);
                    new_facility.setContactInfo(test_3);
                    new_facility.setDetails(test_4);
                    new_facility.setaccessibilityStat(test_5);

                    Intent result = new Intent();
                    result.putExtra("Facility", new_facility);
                    setResult(RESULT_OK, result);
                    finish();

                }
            }
        });

    }
}

package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateEntrantProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_entrant_profile);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            finish();
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            Intent home = new Intent(this, EntrantHome.class);
            EditText name = findViewById(R.id.nameInput);
            EditText email = findViewById(R.id.emailInput);
            EditText phoneNumber = findViewById(R.id.editTextPhone);

            if (name.getText().toString().isEmpty()) {
                Toast nameToast = Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT);
                nameToast.show();
                return;
            }

            if (email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                Toast emailToast = Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT);
                emailToast.show();
                return;
            }

            int phoneNumberLength = phoneNumber.getText().toString().length();
            if (!phoneNumber.getText().toString().isEmpty() && phoneNumberLength != 10) {
                Toast phoneToast = Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT);
                phoneToast.show();
                return;
            }

            UserProfile newProfile = new UserProfile(name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString());

            String mId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(Role.ENTRANT);
            User newUser = new User(mId, roles, newProfile);

            CRUD<User> crud = new CRUD<>(User.class);
            CreateCallback createCallback = new CreateCallback() {
                @Override
                public void onCreateSuccess() {
                    EntrantHome.hasProfile = true;
                    finish();
                }

                @Override
                public void onCreateFailure(Exception e) {
                    Toast.makeText(CreateEntrantProfile.this, "Error creating user, please try again", Toast.LENGTH_SHORT).show();
                }
            };
            crud.create(newUser, createCallback);
        });
    }
}
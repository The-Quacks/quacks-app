package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEntrantProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_entrant_profile);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, EntrantHome.class);
            startActivity(intent);
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            Intent home = new Intent(this, EntrantHome.class);
            EditText name = findViewById(R.id.nameInput);
            EditText email = findViewById(R.id.emailInput);
            EditText phoneNumber = findViewById(R.id.editTextPhone);

            UserProfile newProfile = new UserProfile();
            newProfile.setUserName(name.getText().toString());
            newProfile.setEmail(email.getText().toString());
            newProfile.setPhoneNumber(phoneNumber.getText().toString());

            String mId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//            User newUser = new User(mId);
//            newUser.setUserProfile(newProfile);
//
//            CRUD<User> crud = new CRUD<>(User.class);
//            crud.create(newUser);
//            startActivity(home);
        });
    }
}

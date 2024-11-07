package com.example.quacks_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private EditText userName, email, phoneNumber;
    private Button saveButton;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profilePicture = findViewById(R.id.profilePicture);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        saveButton = findViewById(R.id.saveButton);

        // Load the user profile data
        loadUserProfile();

        // Set up listeners
        profilePicture.setOnClickListener(v -> changeProfilePicture());
        saveButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserProfile() {
        // Assume we're passing UserProfile as a Serializable extra
        userProfile = (UserProfile) getIntent().getSerializableExtra("userProfile");

        if (userProfile != null) {
            // Populate fields with existing data
            userName.setText(userProfile.getUserName());
            email.setText(userProfile.getEmail());
            phoneNumber.setText(userProfile.getPhoneNumber());

            // Display profile picture if exists
            Bitmap picture = userProfile.getProfilePicture();
            if (picture != null) {
                profilePicture.setImageBitmap(picture);
            }
        }
    }

    private void changeProfilePicture() {
        // Placeholder for code to change the profile picture (e.g., open a file picker or camera)
        Toast.makeText(this, "Change Profile Picture clicked", Toast.LENGTH_SHORT).show();
    }

    private void saveProfileChanges() {
        // Save changes to the UserProfile object
        userProfile.setUserName(userName.getText().toString());
        userProfile.setEmail(email.getText().toString());
        userProfile.setPhoneNumber(phoneNumber.getText().toString());

        // Optionally: Update profile picture if changed
        // For now, just showing a toast message
        Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();

        // Return updated profile to calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedUserProfile", userProfile);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

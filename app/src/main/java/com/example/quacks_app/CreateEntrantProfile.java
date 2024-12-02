package com.example.quacks_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * The {@code CreateEntrantProfile} class is used by entrants to add their personal information
 * the first time they use the app. It validates the user's input, and then creates a new User
 * in the database.
 */
public class CreateEntrantProfile extends AppCompatActivity {
    private ImageView profilePicture;
    private User user;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_entrant_profile);

        user = (User) getIntent().getSerializableExtra("User");
        profilePicture = findViewById(R.id.profilePicture);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            finish();
        });

        initializeLaunchers();
        initializePictureButtons();

        Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> saveProfile());
    }

    /**
     * Initializes the {@link ActivityResultLauncher} objects for taking a picture or selecting one
     * from the gallery.
     */
    private void initializeLaunchers() {
        // Camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            profilePicture.setImageBitmap(imageBitmap);
                        }
                    }
                });

        // Gallery launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri imageUri = data.getData();
                            profilePicture.setImageURI(imageUri);
                        }
                    }
                });
    }

    /**
     * Sets up listeners for the "Take Picture" and "Upload Picture" buttons, allowing
     * the user to capture or select a profile picture.
     */
    private void initializePictureButtons() {
        Button takePicture = findViewById(R.id.takePicture);
        takePicture.setOnClickListener(view -> ImageUpload.takePicture(cameraLauncher));

        Button uploadPicture = findViewById(R.id.uploadPicture);
        uploadPicture.setOnClickListener(view -> ImageUpload.openGallery(galleryLauncher));
    }


    /**
     * Validates user input and saves the profile information to the database if all fields are valid.
     */
    private void saveProfile() {
        EditText name = findViewById(R.id.nameInput);
        EditText email = findViewById(R.id.emailInput);
        EditText phoneNumber = findViewById(R.id.editTextPhone);

        // Validate input
        if (!validateInput(name, email, phoneNumber)) return;

        // Create a new profile
        UserProfile newProfile = new UserProfile(
                name.getText().toString().trim(),
                email.getText().toString().trim(),
                phoneNumber.getText().toString().trim()
        );

        // Assign the profile to the user
        user.setUserProfile(newProfile);

        // Update the user in the database
        UpdateCallback updateCallback = new UpdateCallback() {
            @Override
            public void onUpdateSuccess() {
                EntrantHome.hasProfile = true;
                finish();
            }

            @Override
            public void onUpdateFailure(Exception e) {
                Toast.makeText(CreateEntrantProfile.this, "Error creating user, please try again", Toast.LENGTH_SHORT).show();
            }
        };
        CRUD.update(user, updateCallback);
    }

    /**
     * Validates the user's input fields.
     *
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean validateInput(EditText name, EditText email, EditText phoneNumber) {
        if (name.getText().toString().isEmpty()) {
            showToast("Please enter a valid name");
            return false;
        }

        if (email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast("Please enter a valid email");
            return false;
        }

        String phone = phoneNumber.getText().toString();
        if (!phone.isEmpty() && (!PhoneNumberUtils.isGlobalPhoneNumber(phone) || phone.length() != 10)) {
            showToast("Please enter a valid phone number");
            return false;
        }

        if (profilePicture.getDrawable() == null) {
            showToast("Please select or take a profile picture");
            return false;
        }

        return true;
    }

    /**
     * Displays a toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

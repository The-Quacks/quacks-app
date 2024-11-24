package com.example.quacks_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * ProfileActivity handles the display, editing, and management of the user's profile.
 */
public class ProfileActivity extends AppCompatActivity implements EditDialogueFragment.OnProfileUpdatedListener{

    private static final int STORAGE_PERMISSION_CODE = 101;

    // UI elements
    private ImageView profilePicture;
    private TextView userNameInput, emailInput, phoneNumberInput;

    // ActivityResultLauncher for handling the photo picker
    private ActivityResultLauncher<Intent> pickImageLauncher;

    // UserProfile object to hold the user's information
    private UserProfile userProfile;
    private User user;
    private Facility facility;

    /**
     * Initializes the activity and sets up Firebase, UI elements, and event listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements
        profilePicture = findViewById(R.id.profilePicture);
        userNameInput = findViewById(R.id.userNameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        Button saveProfileButton = findViewById(R.id.saveProfileButton);
        Button editPictureButton = findViewById(R.id.editPictureButton);
        Button removePictureButton = findViewById(R.id.removePictureButton);
        Button editProfileDetailsButton = findViewById(R.id.editProfileButton);
        Button backButton = findViewById(R.id.backButton);
        Button createFacilityButton = findViewById(R.id.createFacilityButton);

        // Load user profile data
        loadUserProfile();

        // Initialize the ActivityResultLauncher for selecting an image from the gallery
        initializePickImageLauncher();

        // Set up listeners for buttons
        editProfileDetailsButton.setOnClickListener(view -> openEditProfileDialog());
        editPictureButton.setOnClickListener(v -> requestStoragePermissionAndOpenGallery());
        removePictureButton.setOnClickListener(v -> removeProfilePicture());

        updateUI(createFacilityButton);

        // Set up the ActivityResultLauncher
        ActivityResultLauncher<Intent> createFacilityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Get updated user and facility from the result
                        user = (User) result.getData().getSerializableExtra("User");
                        facility = (Facility) result.getData().getSerializableExtra("Facility");

                        // Update the UI based on the new data
                        updateUI(createFacilityButton);

                        Toast.makeText(this, "Facility created successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Set up listener for Create Facility button
        createFacilityButton.setOnClickListener(v -> {
            if (user != null) {
                if (facility == null) {
                    Intent intent = new Intent(ProfileActivity.this, CreateFacility.class);
                    intent.putExtra("User", user);
                    createFacilityLauncher.launch(intent);
                } else {
                    Toast.makeText(ProfileActivity.this, "Facility already exists", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "User is not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });

        saveProfileButton.setOnClickListener(v -> saveProfileChanges());
        backButton.setOnClickListener(view -> {
            finish();
        });
    }

    /**
     * Updates the UI dynamically based on the user's facility and role status.
     */
    private void updateUI(Button createFacilityButton) {
        // Hide Create Facility button if facility exists or the user is an organizer
        if (facility != null || (user != null && user.getRoles().contains(Role.ORGANIZER))) {
            createFacilityButton.setVisibility(View.GONE);
        } else {
            createFacilityButton.setVisibility(View.VISIBLE);
        }
    }

    private void initializePickImageLauncher() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Display the selected image on the ImageView
                        profilePicture.setImageURI(imageUri);

                        // Optionally, upload the image to Firebase
                        ImageUpload.uploadImageToFirebase(
                                this,
                                imageUri,
                                profilePicture,
                                userProfile,
                                this::saveUserProfileToFirestore
                        );
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * Loads the user profile from Firestore and updates the UI.
     */
    private void loadUserProfile() {
        user = (User) getIntent().getSerializableExtra("User");

        // Check if the user object and documentId are not null
        if (user != null && user.getDocumentId() != null) {
            // Fetch the user profile from Firestore using the deviceId
            CRUD.readStatic(user.getDocumentId(), User.class, new ReadCallback<User>() {
                @Override
                public void onReadSuccess(User data) {
                    userProfile = data.getUserProfile();
                    if (userProfile != null) {
                        // Debug log to confirm data retrieval
                        Log.d("ProfileActivity", "UserProfile fetched: " + userProfile.toString());

                        // Update the UI with the user's profile data
                        if (userProfile.getUserName() != null) {
                            userNameInput.setText(userProfile.getUserName());
                        }
                        if (userProfile.getEmail() != null) {
                            emailInput.setText(userProfile.getEmail());
                        }
                        if (userProfile.getPhoneNumber() != null) {
                            phoneNumberInput.setText(userProfile.getPhoneNumber());
                        }

                        // Load profile picture if it exists
                        if (userProfile.getProfilePicturePath() != null) {
                            CRUD.downloadImage(userProfile.getProfilePicturePath(), new ReadCallback<Bitmap>() {
                                @Override
                                public void onReadSuccess(Bitmap data) {
                                    profilePicture.setImageBitmap(data);
                                }

                                @Override
                                public void onReadFailure(Exception e) {
                                    Log.e("ProfileActivity", "Failed to load profile picture: " + e.getMessage());
                                    // Generate and display a default profile picture with initials
                                    Bitmap defaultImage = ImageUpload.generateDefaultProfilePicture(userProfile.getUserName());
                                    profilePicture.setImageBitmap(defaultImage);
                                }
                            });

                        }
                        if (userProfile.getProfilePicturePath() == null){
                            Bitmap defaultImage = ImageUpload.generateDefaultProfilePicture(
                                    userProfile != null && userProfile.getUserName() != null ? userProfile.getUserName() : "User"
                            );
                            profilePicture.setImageBitmap(defaultImage);
                        }

                    }
                }

                @Override
                public void onReadFailure(Exception e) {
                    Log.e("ProfileActivity", "Failed to load profile: " + e.getMessage());
                }
            });

        } else {
            Log.e("ProfileActivity", "User or DeviceId is null");
        }
    }

    /**
     * Requests storage permission and opens the gallery if granted.
     */
    private void requestStoragePermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openGallery();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                openGallery();
            }
        }
    }

    /**
     * Opens the gallery to pick an image.
     */
    private void openGallery() {
        Intent intent = ImageUpload.createGalleryIntent();
        pickImageLauncher.launch(intent);
    }

    /**
     * Removes the profile picture and sets a default one.
     */
    private void removeProfilePicture() {
        if (userProfile == null || userProfile.getUserName() == null) {
            Toast.makeText(this, "User profile not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageUpload.removeProfilePicture(
                this,
                userProfile,
                profilePicture,
                this::saveUserProfileToFirestore
        );
    }


    /**
     * Saves profile data to Firestore.
     */
    private void saveUserProfileToFirestore() {
        if (user != null && user.getDeviceId() != null && userProfile != null) {
            user.setUserProfile(userProfile);
            CRUD.update(user, new UpdateCallback() {
                @Override
                public void onUpdateSuccess() {
                    Toast.makeText(ProfileActivity.this, "Profile picture updated in Firestore", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUpdateFailure(Exception e) {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile picture in Firestore", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * Saves the changes made to the user's profile.
     */
    private void saveProfileChanges() {
        if (userProfile != null) {
            userProfile.setUserName(userNameInput.getText().toString().trim());
            userProfile.setEmail(emailInput.getText().toString().trim());
            userProfile.setPhoneNumber(phoneNumberInput.getText().toString().trim());
            saveUserProfileToFirestore();
            Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens the EditDialogueFragment to allow the user to edit their profile details.
     * Sets the current activity as the listener for the profile update events.
     * This method displays a dialog fragment where the user can update their profile information.
     */
    private void openEditProfileDialog() {
        EditDialogueFragment editProfileDialog = new EditDialogueFragment();
        editProfileDialog.setOnProfileUpdatedListener(this);
        editProfileDialog.show(getSupportFragmentManager(), "EditProfileDialog");
    }

    /**
     * Called when the profile is updated from the EditDialogueFragment.
     * Updates the local UserProfile object and reflects the changes in the UI.
     *
     * @param updatedProfile The updated UserProfile object containing the new data.
     */
    @Override
    public void onProfileUpdated(UserProfile updatedProfile) {
        // Update the userProfile object with the new data
        this.userProfile = updatedProfile;

        // Update the UI elements with the new profile data
        userNameInput.setText(updatedProfile.getUserName());
        emailInput.setText(updatedProfile.getEmail());
        phoneNumberInput.setText(updatedProfile.getPhoneNumber());

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }
}
package com.example.quacks_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
    private Button saveProfileButton, editPictureButton, removePictureButton, editProfileDetailsButton, backButton;

    // ActivityResultLauncher for handling the photo picker
    private ActivityResultLauncher<Intent> pickImageLauncher;

    // UserProfile object to hold the user's information
    private UserProfile userProfile;
    private User user;

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
        saveProfileButton = findViewById(R.id.saveProfileButton);
        editPictureButton = findViewById(R.id.editPictureButton);
        removePictureButton = findViewById(R.id.removePictureButton);
        editProfileDetailsButton = findViewById(R.id.editProfileButton);
        backButton = findViewById(R.id.backButton);


        // Load user profile data
        loadUserProfile();

        // Initialize the ActivityResultLauncher for selecting an image from the gallery
        // Initialize the ActivityResultLauncher for selecting an image from the gallery
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Display the selected image on the ImageView
                        profilePicture.setImageURI(imageUri);

                        // Optionally, upload the image to Firebase
                        uploadImageToFirebase(imageUri);
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        // Set up listeners for buttons
        editProfileDetailsButton.setOnClickListener(view -> openEditProfileDialog());
        editPictureButton.setOnClickListener(v -> requestStoragePermissionAndOpenGallery());
        removePictureButton.setOnClickListener(v -> removeProfilePicture());
        saveProfileButton.setOnClickListener(v -> saveProfileChanges());
        backButton.setOnClickListener(view -> {
            finish();
        });
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
                        CRUD.downloadImage(userProfile.getProfilePicturePath(), new ReadCallback<Bitmap>() {
                            @Override
                            public void onReadSuccess(Bitmap data) {
                                profilePicture.setImageBitmap(data);
                            }

                            @Override
                            public void onReadFailure(Exception e) {
                                Log.e("ProfileActivity", "Failed to load profile picture: " + e.getMessage());
                                // Generate and display a default profile picture with initials
                                Bitmap defaultImage = generateDefaultProfilePicture(userProfile.getUserName());
                                profilePicture.setImageBitmap(defaultImage);

                            }
                        });
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
     * Generates a default profile picture with the user's initials.
     *
     * @param userName The user's name to generate initials from.
     * @return A Bitmap containing the profile picture.
     */
    private Bitmap generateDefaultProfilePicture(String userName) {
        // Create a blank bitmap
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Set background color
        canvas.drawColor(Color.LTGRAY);

        // Draw initials text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        paint.setTextAlign(Paint.Align.CENTER);

        // Extract initials from the user's name
        String initials = getInitials(userName);
        canvas.drawText(initials, 100, 120, paint);

        return bitmap;
    }

    /**
     * Extracts initials from the given name.
     *
     * @param name The name to extract initials from.
     * @return A string containing the initials.
     */
    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "U"; // Default to 'U' for Unknown
        String[] words = name.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    /**
     * Uploads the selected image to Firebase Storage.
     *
     * @param imageUri The URI of the image to upload.
     */
    private void uploadImageToFirebase(Uri imageUri) {
        if (user == null || user.getDeviceId() == null) return;

        CRUD.storeImage(imageUri, new ReadCallback<String>() {
            @Override
            public void onReadSuccess(String path) {
                userProfile.setProfilePicturePath(path);
                saveUserProfileToFirestore();

                CRUD.downloadImage(path, new ReadCallback<Bitmap>() {
                    @Override
                    public void onReadSuccess(Bitmap data) {
                        profilePicture.setImageBitmap(data);
                        Toast.makeText(ProfileActivity.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(ProfileActivity.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Removes the profile picture and sets a default one.
     */
    private void removeProfilePicture() {
        if (userProfile == null || userProfile.getUserName() == null) {
            Toast.makeText(this, "User profile not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a default profile picture with the user's initials
        Bitmap defaultImage = generateDefaultProfilePicture(userProfile.getUserName());

        // Set the default image in the ImageView
        profilePicture.setImageBitmap(defaultImage);

        // Show a confirmation message
        Toast.makeText(this, "Profile picture removed", Toast.LENGTH_SHORT).show();
    }

    /**
     * Saves profile data to Firestore.
     */
    private void saveUserProfileToFirestore() {
        if (user != null && user.getDeviceId() != null && userProfile != null) {
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

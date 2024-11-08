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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * ProfileActivity handles the display, editing, and management of the user's profile.
 */
public class ProfileActivity extends AppCompatActivity implements EditDialogueFragment.OnProfileUpdatedListener{

    private static final int STORAGE_PERMISSION_CODE = 101;

    // UI elements
    private ImageView profilePicture;
    private TextView userNameInput, emailInput, phoneNumberInput;
    private Button saveProfileButton, editPictureButton, removePictureButton, editProfileDetailsButton, backButton;

    // Firebase instances
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

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

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

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
        user = (User) getIntent().getSerializableExtra("user");

        // Check if the user object and deviceId are not null
        if (user != null && user.getDeviceId() != null) {
            // Fetch the user profile from Firestore using the deviceId
            firestore.collection("User").document(user.getDeviceId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve user profile data from Firestore
                            userProfile = documentSnapshot.toObject(UserProfile.class);

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
                                if (userProfile.getProfilePictureUrl() != null && !userProfile.getProfilePictureUrl().isEmpty()) {
                                    Glide.with(this).load(userProfile.getProfilePictureUrl()).into(profilePicture);
                                } else {
                                    // Generate and display a default profile picture with initials
                                    Bitmap defaultImage = generateDefaultProfilePicture(userProfile.getUserName());
                                    profilePicture.setImageBitmap(defaultImage);
                                }
                            } else {
                                Log.e("ProfileActivity", "UserProfile is null");
                            }
                        } else {
                            Toast.makeText(this, "No profile found for this user", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ProfileActivity", "Failed to load profile: " + e.getMessage());
                        Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("ProfileActivity", "User or DeviceId is null");
        }
    }



    /**
     * Generates a default profile picture with the user's initials.
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
     * Extracts initials from a given name.
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
     * Uploads the selected image to Firebase Storage and updates the profile picture URL.
     */
    private void uploadImageToFirebase(Uri imageUri) {
        if (user == null || user.getDeviceId() == null) return;

        StorageReference storageRef = storage.getReference().child("profile_pictures/" + user.getDeviceId() + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        userProfile.setProfilePictureUrl(uri.toString());
                        saveUserProfileToFirestore();

                        // Update the ImageView with the new image
                        Glide.with(this)
                                .load(uri) // Load the image from the URI
                                .into(profilePicture); // Display it in the ImageView

                        Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    /**
     * Removes the profile picture and sets a default one.
     */
    /**
     * Removes the profile picture from Firebase Storage and sets a default one with the user's initials.
     */
    private void removeProfilePicture() {
        if (user == null || user.getDeviceId() == null) return;

        // Reference to the profile picture in Firebase Storage
        StorageReference storageRef = storage.getReference().child("profile_pictures/" + user.getDeviceId() + ".jpg");

        // Delete the picture from Firebase Storage
        storageRef.delete().addOnSuccessListener(aVoid -> {
            // Successfully deleted the image, now update the UI with a default image
            Bitmap defaultImage = generateDefaultProfilePicture(userProfile.getUserName());
            profilePicture.setImageBitmap(defaultImage);

            // Update the user profile to remove the profile picture URL
            userProfile.setProfilePictureUrl(null);
            saveUserProfileToFirestore();

            Toast.makeText(this, "Profile picture removed", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to remove profile picture", Toast.LENGTH_SHORT).show();
        });
    }


    /**
     * Saves profile data to Firestore.
     */
    /**
     * Saves the profile picture URL to Firestore.
     */
    private void saveUserProfileToFirestore() {
        if (user != null && user.getDeviceId() != null && userProfile != null) {
            firestore.collection("User").document(user.getDeviceId())
                    .update("profilePictureUrl", userProfile.getProfilePictureUrl())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Profile picture updated in Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update profile picture in Firestore", Toast.LENGTH_SHORT).show();
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

    private void openEditProfileDialog() {
        EditDialogueFragment editProfileDialog = new EditDialogueFragment();
        editProfileDialog.setOnProfileUpdatedListener(this);
        editProfileDialog.show(getSupportFragmentManager(), "EditProfileDialog");
    }


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

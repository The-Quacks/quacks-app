package com.example.quacks_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * ProfileActivity handles the display, editing, and management of the user's profile.
 * It allows users to update their profile picture, name, email, and phone number.
 * The data is stored in Firebase Firestore and Firebase Storage.
 */
public class ProfileActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;

    // UI elements
    private ImageView profilePicture;
    private EditText userNameInput, emailInput, phoneNumberInput;
    private Button saveProfileButton, editPictureButton, removePictureButton, backButton;

    // UserProfile object to hold the user's information
    private UserProfile userProfile;

    // Firebase instances
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    // ActivityResultLauncher for handling the photo picker
    private ActivityResultLauncher<Intent> pickImageLauncher;

    /**
     * Initializes the activity and sets up Firebase, UI elements, and event listeners.
     *
     * @param savedInstanceState The saved instance state.
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
        backButton = findViewById(R.id.back_button);

        // Load user profile data
        loadUserProfile();


        // Initialize the ActivityResultLauncher for selecting an image from the gallery
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        uploadImageToFirebase(imageUri);
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Set up listeners for buttons
        editPictureButton.setOnClickListener(v -> requestStoragePermissionAndOpenGallery());
        removePictureButton.setOnClickListener(v -> removeProfilePicture());
        saveProfileButton.setOnClickListener(v -> saveProfileChanges());
        backButton.setOnClickListener(v -> {finish();
        });
    }

    /**
     * Loads the user profile from the intent and updates the UI fields.
     */
    private User user;
    private void loadUserProfile() {
        user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            userProfile = user.getUserProfile();
            if (userProfile != null) {
                userNameInput.setText(userProfile.getUserName());
                emailInput.setText(userProfile.getEmail());
                phoneNumberInput.setText(userProfile.getPhoneNumber());

                if (userProfile.getProfilePictureUrl() != null) {
                    Glide.with(this).load(userProfile.getProfilePictureUrl()).into(profilePicture);
                } else {
                    Bitmap defaultImage = UpdateProfilePicture.generateDefaultProfilePicture(userProfile.getUserName());
                    profilePicture.setImageBitmap(defaultImage);
                }
            }
        }
    }


    /**
     * Requests storage permission and opens the gallery if permission is granted.
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
     * Handles the result of the permission request for accessing external storage.
     *
     * @param requestCode  The request code passed to requestPermissions().
     * @param permissions  The requested permissions.
     * @param grantResults The results for the requested permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Uploads the selected image to Firebase Storage and updates the user's profile picture URL.
     *
     * @param imageUri The URI of the selected image.
     */
    private void uploadImageToFirebase(Uri imageUri) {
        if (user == null || user.getDeviceId() == null) return;

        StorageReference storageRef = storage.getReference().child("profile_pictures/" + user.getDeviceId() + ".jpg");

        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                userProfile.setProfilePictureUrl(uri.toString());
                saveUserProfileToFirestore();
                Glide.with(this).load(uri).into(profilePicture);
                Toast.makeText(this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }


    /**
     * Removes the user's profile picture and updates it with a default image.
     */
    private void removeProfilePicture() {
        if (userProfile == null) return;

        Bitmap defaultImage = UpdateProfilePicture.generateDefaultProfilePicture(userProfile.getUserName());
        profilePicture.setImageBitmap(defaultImage);
        userProfile.setProfilePictureUrl(null);
        saveUserProfileToFirestore();
        Toast.makeText(this, "Profile Picture Removed", Toast.LENGTH_SHORT).show();
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
     * Saves the user's profile data to Firestore.
     */
    private void saveUserProfileToFirestore() {
        if (user != null) {
            firestore.collection("users").document(user.getDeviceId())
                    .set(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Profile saved to Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

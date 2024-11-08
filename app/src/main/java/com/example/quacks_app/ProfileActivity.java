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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;

    private ImageView profilePicture;
    private EditText userNameInput, emailInput, phoneNumberInput;
    private Button saveProfileButton, editPictureButton, removePictureButton;
    private UserProfile userProfile;

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    // Define an ActivityResultLauncher for the Photo Picker
    private ActivityResultLauncher<Intent> pickImageLauncher;

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

        // Load user profile data
        loadUserProfile();

        // Initialize the ActivityResultLauncher
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
    }

    private void loadUserProfile() {
        userProfile = (UserProfile) getIntent().getSerializableExtra("userProfile");

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

    private void requestStoragePermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, no manual permissions are needed for the photo picker
            openGallery();
        } else {
            // For Android 12 and below, check for READ_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

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

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = storage.getReference().child("profile_pictures/" + userProfile.getUserName() + ".jpg");

        // Upload the image
        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    userProfile.setProfilePictureUrl(uri.toString());
                    saveUserProfileToFirestore();
                    Toast.makeText(ProfileActivity.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                })
        ).addOnFailureListener(e ->
                Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show()
        );
    }

    private void removeProfilePicture() {
        Bitmap defaultImage = UpdateProfilePicture.generateDefaultProfilePicture(userProfile.getUserName());
        profilePicture.setImageBitmap(defaultImage);
        userProfile.setProfilePictureUrl(null);
        saveUserProfileToFirestore();
        Toast.makeText(this, "Profile Picture Removed", Toast.LENGTH_SHORT).show();
    }

    private void saveProfileChanges() {
        if (userProfile != null) {
            userProfile.setUserName(userNameInput.getText().toString().trim());
            userProfile.setEmail(emailInput.getText().toString().trim());
            userProfile.setPhoneNumber(phoneNumberInput.getText().toString().trim());
            saveUserProfileToFirestore();
            Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserProfileToFirestore() {
        firestore.collection("users").document(userProfile.getUserName())
                .set(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Profile saved to Firestore", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

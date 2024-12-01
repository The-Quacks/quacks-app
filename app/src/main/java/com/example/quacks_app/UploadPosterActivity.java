package com.example.quacks_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UploadPosterActivity extends AppCompatActivity {
    private Event event;
    private User user;
    private Facility facility;
    private EventList eventList;
    private ImageView eventPoster;

    private static final int STORAGE_PERMISSION_CODE = 101;

    // ActivityResultLauncher for handling the photo picker
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.upload_poster);

        event = (Event) getIntent().getSerializableExtra("Event");
        user = (User) getIntent().getSerializableExtra("User");
        eventList = (EventList) getIntent().getSerializableExtra("EventList");
        facility = (Facility) getIntent().getSerializableExtra("Facility");

        initializePickImageLauncher();

        eventPoster = findViewById(R.id.poster);
        eventPoster.setOnClickListener(v ->
            requestStoragePermissionAndOpenGallery()
        );
        Button next = findViewById(R.id.next_button_poster);
        next.setOnClickListener(v -> {
            Intent intent = new Intent(UploadPosterActivity.this, ViewEvents.class);
            intent.putExtra("EventList", eventList);
            intent.putExtra("User", user);
            intent.putExtra("Facility", facility);
            intent.putExtra("Event", event);
            startActivity(intent);
            finish();

        });
    }

    private void initializePickImageLauncher() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Display the selected image on the ImageView
                        eventPoster.setImageURI(imageUri);

                        // Optionally, upload the image to Firebase
                        ImageUpload.uploadEventPosterToFirebase(
                                this,
                                imageUri,
                                event,
                                this::saveEventToFirestore
                        );
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void saveEventToFirestore(){
        if (event.getEventId() != null) {
            Log.d("SaveEvent", "Saving event with poster path: " + event.getEventPosterPath());
            CRUD.update(event, new UpdateCallback() {
                @Override
                public void onUpdateSuccess() {
                    Toast.makeText(UploadPosterActivity.this, "Profile picture updated in Firestore", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUpdateFailure(Exception e) {
                    Toast.makeText(UploadPosterActivity.this, "Failed to update Profile picture in Firestore", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Requests storage permission and opens the gallery if granted.
     */
    private void requestStoragePermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openGallery();
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

}

package com.example.quacks_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

public class ImageUpload {
    private static final int STORAGE_PERMISSION_CODE = 101;

    /**
     * Opens the gallery to pick an image.
     *
     * @param launcher The ActivityResultLauncher to handle the gallery result.
     */
    public static void openGallery(ActivityResultLauncher<Intent> launcher) {
        launcher.launch(createGalleryIntent());
    }

    /**
     * Creates an Intent to pick an image from the gallery.
     *
     * @return The Intent for picking an image.
     */
    public static Intent createGalleryIntent() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * Launches the camera to take a picture.
     *
     * @param launcher The ActivityResultLauncher to handle the camera result.
     */
    public static void takePicture(ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launcher.launch(intent);
    }

    /**
     * Removes the profile picture and sets a default one.
     *
     * @param context       The context for showing messages.
     * @param userProfile   The UserProfile object to update.
     * @param imageView     The ImageView to update with the default image.
     * @param onRemoveCallback A callback to handle post-removal actions.
     */
    public static void removeProfilePicture(Context context, UserProfile userProfile, ImageView imageView, Runnable onRemoveCallback) {
        if (userProfile == null || userProfile.getUserName() == null) {
            Toast.makeText(context, "User profile not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the default profile picture
        Bitmap defaultImage = generateDefaultProfilePicture(userProfile.getUserName());
        imageView.setImageBitmap(defaultImage);

        // Check if a profile picture path exists
        String profilePicturePath = userProfile.getProfilePicturePath();
        if (profilePicturePath == null || profilePicturePath.isEmpty()) {
            Toast.makeText(context, "No profile picture to remove", Toast.LENGTH_SHORT).show();
            if (onRemoveCallback != null) {
                onRemoveCallback.run();
            }
            return;
        }

        // Remove the image from Firebase Storage
        CRUD.removeImage(profilePicturePath, new DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                // Clear the profile picture path in the UserProfile
                userProfile.setProfilePicturePath(null);
                Toast.makeText(context, "Profile picture removed", Toast.LENGTH_SHORT).show();

                // Execute the callback if provided
                if (onRemoveCallback != null) {
                    onRemoveCallback.run();
                }
            }

            @Override
            public void onDeleteFailure(Exception e) {
                Log.e("ImageUpload", "Failed to remove profile picture: " + e.getMessage());
                Toast.makeText(context, "Failed to remove profile picture", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Generates a default profile picture bitmap with initials.
     *
     * @param userName The user's name to generate initials from.
     * @return A Bitmap containing the profile picture.
     */
    public static Bitmap generateDefaultProfilePicture(String userName) {
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Set background color
        canvas.drawColor(Color.LTGRAY);

        // Draw initials
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
        paint.setTextAlign(Paint.Align.CENTER);

        // Extract initials
        String initials = getInitials(userName);
        canvas.drawText(initials, 100, 120, paint);

        return bitmap;
    }

    /**
     * Extracts initials from a full name.
     *
     * @param name The full name.
     * @return A string with the initials.
     */
    private static String getInitials(String name) {
        if (name == null || name.isEmpty()) return "U";
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
     * Uploads an image to Firebase and updates the ImageView on success.
     *
     * @param context       The context for showing messages.
     * @param imageUri      The URI of the image to upload.
     * @param imageView     The ImageView to update with the uploaded image.
     * @param userProfile   The UserProfile object to update with the image path.
     * @param onSaveCallback A callback to save the updated profile after uploading.
     */
    public static void uploadImageToFirebase(Context context, Uri imageUri,
                                             ImageView imageView, UserProfile userProfile, Runnable onSaveCallback
    ) {
        CRUD.storeImage(imageUri, new ReadCallback<String>() {
            @Override
            public void onReadSuccess(String path) {
                userProfile.setProfilePicturePath(path);

                CRUD.downloadImage(path, new ReadCallback<Bitmap>() {
                    @Override
                    public void onReadSuccess(Bitmap data) {
                        imageView.setImageBitmap(data);
                        Toast.makeText(context, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                        if (onSaveCallback != null) {
                            onSaveCallback.run();
                        }
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(context, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void uploadEventImageToFirebase(Context context, Uri imageUri, ImageView imageView,
                                                  Event event, Runnable onSaveCallback
    ) {
        // Use a unique path for storing event images
        String path = "events/" + System.currentTimeMillis();

        CRUD.storeImage(imageUri, new ReadCallback<String>() {
            @Override
            public void onReadSuccess(String uploadedPath) {
                event.setQrCodePath(path); // Associate the path with the event object

                // Download the image and display it in the ImageView
                CRUD.downloadImage(uploadedPath, new ReadCallback<Bitmap>() {
                    @Override
                    public void onReadSuccess(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap); // Update the UI with the uploaded image
                        Toast.makeText(context, "Event image uploaded successfully!", Toast.LENGTH_SHORT).show();

                        // Save the event data if required
                        if (onSaveCallback != null) {
                            onSaveCallback.run();
                        }
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(context, "Failed to retrieve event image", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(context, "Failed to upload event image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

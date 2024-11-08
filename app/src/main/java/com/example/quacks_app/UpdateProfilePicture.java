package com.example.quacks_app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;


public class UpdateProfilePicture {

    private static final FirebaseStorage storage = FirebaseStorage.getInstance();

    public static Bitmap generateDefaultProfilePicture(String userName) {
        Bitmap defaultImage = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(defaultImage);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY); // Background color
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, 150, 150, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        String initials = getInitials(userName);
        canvas.drawText(initials, 75, 90, paint);

        return defaultImage;
    }

    private static String getInitials(String userName) {
        if (userName == null || userName.isEmpty()) return "";
        String[] names = userName.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String name : names) {
            if (!name.isEmpty()) {
                initials.append(name.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }

    public static void uploadProfilePicture(UserProfile userProfile, Bitmap newImage, String deviceId) {
        if (newImage == null || deviceId == null) return;

        // Convert Bitmap to ByteArray
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Create a reference in Firebase Storage using deviceId
        StorageReference storageRef = storage.getReference().child("profile_pictures/" + deviceId + ".jpg");

        // Upload the image to Firebase Storage
        storageRef.putBytes(imageData).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    userProfile.setProfilePictureUrl(imageUrl);
                    Log.d("Upload", "Profile picture uploaded: " + imageUrl);
                })
        ).addOnFailureListener(e -> Log.e("Upload", "Failed to upload profile picture", e));
    }

    public static void removeProfilePicture(UserProfile userProfile, String deviceId) {
        if (deviceId == null) return;

        // Create a reference to the profile picture in Firebase Storage
        StorageReference storageRef = storage.getReference().child("profile_pictures/" + deviceId + ".jpg");

        // Delete the profile picture
        storageRef.delete().addOnSuccessListener(aVoid -> {
            userProfile.setProfilePictureUrl(null);
            Log.d("Remove", "Profile picture removed for deviceId: " + deviceId);
        }).addOnFailureListener(e -> Log.e("Remove", "Failed to remove profile picture", e));
    }
}

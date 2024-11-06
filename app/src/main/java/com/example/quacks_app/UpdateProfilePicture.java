package com.example.quacks_app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class UpdateProfilePicture {

    public static Bitmap generateDefaultProfilePicture(String userName) {
        // Create a simple image with user's initials as the default
        Bitmap defaultImage = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(defaultImage);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY); // Background color
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, 100, 100, paint);

        // Set initials text in the middle
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        String initials = getInitials(userName);
        canvas.drawText(initials, 50, 60, paint);

        return defaultImage;
    }

    private static String getInitials(String userName) {
        String[] names = userName.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String name : names) {
            if (!name.isEmpty()) {
                initials.append(name.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }

    public static void uploadProfilePicture(UserProfile userProfile, Bitmap newImage) {
        userProfile.setProfilePicture(newImage);
    }

    public static void removeProfilePicture(UserProfile userProfile) {
        Bitmap defaultImage = generateDefaultProfilePicture(userProfile.getUserName());
        userProfile.setProfilePicture(defaultImage);
    }
}

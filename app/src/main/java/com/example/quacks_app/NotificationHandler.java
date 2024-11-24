//package com.example.quacks_app;
//
//import static androidx.core.app.ActivityCompat.requestPermissions;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//public class NotificationHandler {
//    public static final String EVENTS_CHANNEL_ID = "events_channel";
//    private static final int REQUEST_CODE = 1;
//    public static void createChannel(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            if (notificationManager != null && notificationManager.getNotificationChannel(EVENTS_CHANNEL_ID) == null) {
//                NotificationChannel channel = new NotificationChannel(EVENTS_CHANNEL_ID, "Events Channel", NotificationManager.IMPORTANCE_HIGH);
//                channel.setDescription("A channel for sending updates about events you expressed interest in.");
//                channel.enableVibration(true);
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
//    }
//
//    public static void sendNotification(Context context, String title, String messageShort, int icon) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, EVENTS_CHANNEL_ID)
//                .setSmallIcon(icon)
//                .setContentTitle(title)
//                .setContentText(messageShort)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(10, builder.build());
//    }
//
//    public static void sendNotificationVerbose(Context context, String title, String messageShort, String messageLong, int icon) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, EVENTS_CHANNEL_ID)
//                .setSmallIcon(icon)
//                .setContentTitle(title)
//                .setContentText(messageShort)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(messageLong))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(10, builder.build());
//    }
//
//    public static void sendActionedNotificationVerbose(Context context, String title, String messageShort, String messageLong, int icon, Intent intent) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, EVENTS_CHANNEL_ID)
//                .setSmallIcon(icon)
//                .setContentTitle(title)
//                .setContentText(messageShort)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(messageLong))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(10, builder.build());
//    }
//
//    public static void askForPermission(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
//        }
//    }
//}

package com.example.quacks_app;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * <p>This class is responsible for instantiating and managing system notifications for this app.
 * It creates the channel that the notifications will be broadcast to, the notifications themselves,
 * and ensures notification permissions are confirmed and acknowledged.</p>
 * <p>Note that due to the nature of permission requests in applications, there is some necessary functionality
 * that only exists in activities that employ this class, that handle said functionality on this class's behalf.</p>
 * @author Marko Srnic
 * @see EntrantHome
 * @version 1.1
 */
public class NotificationHandler implements Serializable {
    //Declaration of attributes
    public static final String EVENTS_CHANNEL_ID = "events_channel";
    private static final int REQUEST_CODE = 1;
    private ArrayList<Integer> notifIDs;
    private int newNotifID;
    public boolean appEnabled;
    public boolean phoneEnabled;

    /**
     * The constructor for the handler. All it does is set the first notification ID and define the notification list.
     */
    public NotificationHandler() {
        this.newNotifID = 1;
        notifIDs = new ArrayList<>();
    }

    /**
     * Creates the notification channel that notifications will be broadcast to.
     * This channel is called Events Channel, and is "A channel for sending updates about events you expressed interest in.", you as in the user.
     *
     * @param context - the activity that this channel is being created in.
     */
    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null && notificationManager.getNotificationChannel(EVENTS_CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(EVENTS_CHANNEL_ID, "Events Channel", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("A channel for sending updates about events you expressed interest in.");
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * This method creates and emits a notification that has a small, always visible message.
     * The notification is associated with the created notification channel, so there must be an existing channel before this method can work.
     * Additionally, there is a suppressed SecurityException for when a notification is sent, specifically in
     * "notificationManagerCompat.notify(newNotifID, builder.build());", where there is a worry by the system that a
     * notification may be sent without a check of the proper permissions. These are intentionally suppressed because in a given activity,
     * this method is never called unless notifications are allowed in the first place, effectively checking for permission first before allowing
     * any notifications to be made.
     *
     * @param context      - The activity the notification is sent over.
     * @param title        - The main title of the notification.
     * @param messageShort - The content of the notification.
     * @param icon         - An image resource that serves as the logo for the notification.
     */
    @SuppressLint("MissingPermission")
    public void sendNotification(Context context, String title, String messageShort, int icon) {
        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, EVENTS_CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(messageShort)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        //Set up the emitter and emit the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(newNotifID, builder.build());
        //Add the notification to the active list and associate it with an ID
        notifIDs.add(newNotifID);
        newNotifID += 1;
    }

    /**
     * This method creates and emits a notification that has a small, always visible message.
     * It also contains a field for a larger/longer message that can be viewed in the system's notification tab by swiping up.
     * The notification is associated with the created notification channel, so there must be an existing channel before this method can work.
     * Additionally, there is a suppressed SecurityException for when a notification is sent, specifically in
     * "notificationManagerCompat.notify(newNotifID, builder.build());", where there is a worry by the system that a
     * notification may be sent without a check of the proper permissions. These are intentionally suppressed because in a given activity,
     * this method is never called unless notifications are allowed in the first place, effectively checking for permission first before allowing
     * any notifications to be made.
     *
     * @param context      - The activity the notification is sent over.
     * @param title        - The main title of the notification.
     * @param messageShort - The content of the notification.
     * @param messageLong  - The content of the longer text portion of the notification.
     * @param icon         - An image resource that serves as the logo for the notification.
     */
    @SuppressLint("MissingPermission")
    public void sendNotificationVerbose(Context context, String title, String messageShort, String messageLong, int icon) {
        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, EVENTS_CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(messageShort)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageLong))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        //Set up the emitter and emit the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(newNotifID, builder.build());
        //Add the notification to the active list and associate it with an ID
        notifIDs.add(newNotifID);
        newNotifID += 1;
    }

    // UNFINISHED NOTIFICATION TYPE, MAY BE UNNEEDED (duplicate of verbose notification)

//    @SuppressLint("MissingPermission")
//    public void sendActionedNotificationVerbose(Context context, String title, String messageShort, String messageLong, int icon, Intent intent) {
//        //Build the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, EVENTS_CHANNEL_ID)
//                .setSmallIcon(icon)
//                .setContentTitle(title)
//                .setContentText(messageShort)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(messageLong))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//        //Set up the emitter and emit the notification
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(newNotifID, builder.build());
//        //Add the notification to the active list and associate it with an ID
//        notifIDs.add(newNotifID);
//        newNotifID += 1;
//    }

    /**
     * This method explicitly asks for notification permission from the user, using the built-in
     * standardized permission prompt by Android. Additionally, because these permissions were
     * introduced on a specific version of Android, there is a check to ensure the user has an OS that is
     * at least a specific threshold OS (the one that introduced the permissions.)
     *
     * @param activity - the activity that is asking for permission.
     */
    public static void askForPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
        }
    }

    /**
     * Based on the user passed it queries for the notifications that are not delivered and adds them to an arraylist
     * @param user
     * @return an Arraylist of notifications
     */
    public void getNotificationForUser(User user, ReadMultipleCallback<Notification> callback ) {
        ArrayList<Notification> user_specified_notifications = new ArrayList<Notification>();
        CRUD.readAllStatic(Notification.class, callback);
    }

    /**
     * Gets all notifications that are not sent--- NOT DEPENDENT ON USER
     * @return notifications - the list of all notifications, regardless of user.
     */

    public ArrayList<Notification> getAllUnsentNotifications(){
        ArrayList<Notification> notifications = new ArrayList<>();

        CRUD.readAllLive(Notification.class, new ReadMultipleCallback<Notification>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<Notification> data) {
                if (data != null){
                    for (Notification notification : data){
                        if (notification.getSentStatus().equals("Not Sent")){
                            notifications.add(notification);
                        }
                    }
                }
            }

            @Override
            public void onReadMultipleFailure(Exception e) {

            }
        });
        return notifications;

    }
    public void sendUnreadNotifications(Context context, ArrayList<Notification> notifications) {
        for (int i = 0; i < notifications.size(); i++) {
            String wStatus = notifications.get(i).getWaitlistStatus();
            if (wStatus.contains("Accepted")) {
                CRUD.readStatic(notifications.get(i).getNotificationEventId(), Event.class, new ReadCallback<Event>() {
                    @Override
                    public void onReadSuccess(Event data) {
                        sendNotificationVerbose(context, "Accepted Into Event!", "Accepted into event " + data.getEventName() + "!", "Accepted into event " + data.getEventName() + "!",  R.drawable.new_email);
                    }

                    @Override
                    public void onReadFailure(Exception e) {

                    }
                });

            }
        }
    }
}

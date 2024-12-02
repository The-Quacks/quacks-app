package com.example.quacks_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

/**
 * <p>
 * This is the activity that runs for the Notification Center page in the app.
 * It allows the entrant to toggle in-app notifications at the top with a switch.</p>
 * <p>This toggle also controls the display of the Notification Center. If in-app
 * notifications are disabled, then a screen with a prompt telling the user that
 * notifcations are disabled appears, and shows them how to enable both in-app notifications
 * and phone (system) notifications. If in-app notifications are enabled, then the display
 * instead shows the current unresolved notifications in order from newest to oldest in a list.</p>
 * <p>When a notification in the list is clicked on, it takes you to a screen with more information
 * on the event the notification is associated with, and some options for actions based on the context
 * of the notification.</p>
 * @author Marko Srnic
 * @see NotificationHandler
 * @see EntrantHome
 * @version 1.0
 */
public class NotificationCenter extends AppCompatActivity {
    //Initializations
    boolean notifsAllowed;
    Switch notifToggle;
    TextView falseTitle, falseWaitlistText, falseAppNotifText, falsePhoneNotifText, trueTitle;
    Button settingsButton;
    ImageButton homeButton;
    ListView notifList;

    /**
     * This overidden method is the function that loads and implements the display, based on the attributes
     * that are in the notification_center.xml file.
     * @param savedInstanceState needed for the super call of this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.notification_center);
        //Grab Notification Handler value from previous activity (EntrantHome)
        notifsAllowed = Objects.requireNonNull(getIntent().getExtras()).getBoolean("Notif_Permissions", false);
        //Define elements
        notifToggle = (Switch) findViewById(R.id.appNotifsSwitch);
        falseTitle = findViewById(R.id.notificationFalseTitle);
        falseWaitlistText = findViewById(R.id.notificationFalseText);
        falseAppNotifText = findViewById(R.id.notificationFalseText2);
        falsePhoneNotifText = findViewById(R.id.notificationFalseText3);
        settingsButton = (Button) findViewById(R.id.notificationSettingsButton);
        homeButton = (ImageButton) findViewById(R.id.homeIcon);
        trueTitle = findViewById(R.id.notificationTrueTitle);
        notifList = findViewById(R.id.notif_list);
        //Display Notification List or disabled prompt, depending on if notifications are allowed
        //THIS IS ON INITIAL STARTUP OF THE ACTIVITY ONLY, and is therefore needed because the switch does not exist yet.
        if (notifsAllowed) {
            notifToggle.setChecked(true);
            falseTitle.setVisibility(View.GONE);
            falseWaitlistText.setVisibility(View.GONE);
            falseAppNotifText.setVisibility(View.GONE);
            falsePhoneNotifText.setVisibility(View.GONE);
            settingsButton.setVisibility(View.GONE);
            trueTitle.setVisibility(View.VISIBLE);
            notifList.setVisibility(View.VISIBLE);
        } else {
            notifToggle.setChecked(false);
            falseTitle.setVisibility(View.VISIBLE);
            falseWaitlistText.setVisibility(View.VISIBLE);
            falseAppNotifText.setVisibility(View.VISIBLE);
            falsePhoneNotifText.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            trueTitle.setVisibility(View.GONE);
            notifList.setVisibility(View.GONE);
        }
        //Send user to Settings App's notification settings for this app when clicked.
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri URI = Uri.fromParts("package", getPackageName(), null);
            intent.setData(URI);
            startActivity(intent);
        });
        //When the home button in the bottom bar is clicked, return back to entrant home page.
        homeButton.setOnClickListener(v -> {
            finish();
        });
        //Listener for the notifications switch toggle at the top of the page.
        //Flips the display of the notification center and enables/disables in-app notifications.
        notifToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * A method for Switch elements that defines what occurs when a Switch is toggled.
             * @param buttonView - the button (in this case Switch) whose state has changed.
             * @param isChecked - the boolean that enumerates whether the state is on (true) or off (false).
             */
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //If on/true, enable the Notification Center and hide settings prompt.
                if (isChecked) {
                    notifsAllowed = true;
                    notifToggle.setChecked(true);
                    falseTitle.setVisibility(View.GONE);
                    falseWaitlistText.setVisibility(View.GONE);
                    falseAppNotifText.setVisibility(View.GONE);
                    falsePhoneNotifText.setVisibility(View.GONE);
                    settingsButton.setVisibility(View.GONE);
                    trueTitle.setVisibility(View.VISIBLE);
                    notifList.setVisibility(View.VISIBLE);
                //If off/false, enable the Notification Settings prompt and hide the Notification Center.
                } else {
                    notifsAllowed = false;
                    notifToggle.setChecked(false);
                    falseTitle.setVisibility(View.VISIBLE);
                    falseWaitlistText.setVisibility(View.VISIBLE);
                    falseAppNotifText.setVisibility(View.VISIBLE);
                    falsePhoneNotifText.setVisibility(View.VISIBLE);
                    settingsButton.setVisibility(View.VISIBLE);
                    trueTitle.setVisibility(View.GONE);
                    notifList.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * Gets the current user object, I used user as a parameter in notification handler so I thought this might be useful
     */
    public User getUser(){
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final User[] user = {null};
        CRUD.readLive(androidId, User.class, new ReadCallback<User>() {
            @Override
            public void onReadSuccess(User data) {
                if (data != null) {
                    user[0] = data;

                }
            }

            @Override
            public void onReadFailure(Exception e) {

            }
        });
        return user[0];
    }

    /**
     * Gets the current event from notification object, returns event object
     */
    public Event getEvent(Notification notification){
        String eventid = notification.getNotificationEventId();
        String appliantid = notification.getApplicantListId();
        final Event[] event = {null};
        CRUD.readStatic(eventid, Event.class, new ReadCallback<Event>() {
            @Override
            public void onReadSuccess(Event data) {
                if (data != null){
                    if (data.getApplicantList().equals(appliantid) && eventid.equals(data.getEventId())){
                        //they are the same event
                        event[0] = data;
                    }
                }
            }

            @Override
            public void onReadFailure(Exception e) {

            }
        });
        return event[0];
    }
}

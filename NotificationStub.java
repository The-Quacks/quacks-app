package com.example.quacks_app;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class NotificationStub extends AppCompatActivity {
    //There are THREE sections of code you need to implement in your activity if you want to use notifications
    //Those sections of code and the class of course
    //Buttons for stub test
    Button nButton, nButton2, nButton3, nButton4, nButton5, nButton6;
    //NEED this variable for permission request handler, 1st section you need
    private static final int REQUEST_CODE = 1;

    //Default onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.notification_stub);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Initialize notification permissions and notification channel
        //These two lines make up the second section you need
        NotificationHandler.askForPermission(this);
        NotificationHandler.createChannel(this);

        //Define the stub buttons and what they do
        nButton = (Button) findViewById(R.id.notifButton);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationHandler.sendNotificationVerbose(NotificationStub.this,
                        "Event Signup: Denied",
                        "Better luck next time!",
                        "Unfortunately, you were not selected for this Event you signed up for. Please, try again by signing up for other Events.",
                        R.drawable.ic_launcher_background);
            }
        });
        nButton2 = (Button) findViewById(R.id.notifButton2);
        nButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationHandler.sendNotification(NotificationStub.this,
                        "Event Signup: Denied",
                        "Better luck next time!",
                        R.drawable.ic_launcher_background);
            }
        });
        nButton3 = (Button) findViewById(R.id.notifButton3);
        nButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationHandler.sendNotification(NotificationStub.this,
                        "Event Signup: Accepted",
                        "Congratulations, you're in!",
                        R.drawable.ic_launcher_background);
            }
        });
        nButton4 = (Button) findViewById(R.id.notifButton4);
        nButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationHandler.sendNotificationVerbose(NotificationStub.this,
                        "Event Signup: Accepted",
                        "Congratulations, you're in!",
                        "Great news! You have been selected by the automated lottery system to participate in this Event you signed up for!",
                        R.drawable.ic_launcher_background);
            }
        });
        nButton5 = (Button) findViewById(R.id.notifButton5);
        nButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationHandler.sendNotificationVerbose(NotificationStub.this,
                        "Event Signup Invitation",
                        "You have been invited!",
                        "Great news! You have been personally selected by the Organizer to participate in this Event you signed up for!",
                        R.drawable.ic_launcher_background);
            }
        });
        nButton6 = (Button) findViewById(R.id.notifButton6);
        nButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationHandler.sendNotification(NotificationStub.this,
                        "Event Signup Invitation",
                        "You have been invited!",
                        R.drawable.ic_launcher_background);
            }
        });
    }

    //Separate overidden function outside onCreate - this is the third and final section you need
    //This function responds with a toast if notifications are enabled/disabled
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
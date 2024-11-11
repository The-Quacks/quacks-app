package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code EntrantHome} class is used on the home page for entrants. It includes buttons to
 * go to different parts of the app. Currently, the scan QR code button is implemented, which allows
 * the user to scan a QR code, see the event information, and join the waitlist. The profile button
 * also only lets users create a profile if they don't already have one.
 */

public class EntrantHome extends AppCompatActivity {
    static boolean hasProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_home);
        EdgeToEdge.enable(this);

        ImageButton profile = findViewById(R.id.profileButton);
        ImageButton waitlist = findViewById(R.id.waitlistButton);
        ImageButton notifications = findViewById(R.id.notificationsButton);
        ImageButton scanQRCode = findViewById(R.id.scanQRCodeButton);

        ReadMultipleCallback<User> readMultipleCallback = new ReadMultipleCallback<User>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<User> data) {
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                for (User user : data) {
                    if (user.getDeviceId().equals(deviceId)) {
                        hasProfile = true;
                        break;
                    }
                }
            }

            @Override
            public void onReadMultipleFailure(Exception e) {
                Toast.makeText(EntrantHome.this, "Could not connect to database", Toast.LENGTH_SHORT).show();
            }
        };

        Map<String, Object> deviceId = new HashMap<>();
        deviceId.put("deviceId", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        CRUD.readQueryStatic(deviceId, User.class, readMultipleCallback);

        profile.setOnClickListener(view -> {
            if (!hasProfile) {
                startActivity(new Intent(EntrantHome.this, CreateEntrantProfile.class));
            } else {
                Toast.makeText(EntrantHome.this, "Go to profile activity", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(EntrantHome.this, EntrantProfile.class));
            }
        });

//        waitlist.setOnClickListener(view -> {
//            startActivity(new Intent(EntrantHome.this, Waitlist.class));
//        });

//        notifications.setOnClickListener(view -> {
//            startActivity(new Intent(EntrantHome.this, Notifications.class));
//        });

        scanQRCode.setOnClickListener(view -> {
            GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                    .build();

            GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(this, options);
            scanner.startScan()
                    .addOnSuccessListener(barcode -> {
                        String id = barcode.getRawValue();
                        Intent switchActivityIntent = new Intent(getApplicationContext(),
                                EventDescription.class);
                        switchActivityIntent.putExtra("id", id);
                        startActivity(switchActivityIntent);
                    })
                    .addOnCanceledListener(this::finish)
                    .addOnFailureListener(e -> {
                    });
        });
    }
}

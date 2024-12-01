package com.example.quacks_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DummyLocationTest extends AppCompatActivity {
    Geolocation geolocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_location_test);

        Button button = findViewById(R.id.button);
        geolocation = new Geolocation(this, this);
        geolocation.setLocationCallback(new Geolocation.LocationCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                Toast.makeText(DummyLocationTest.this,
                        "Location: " + latitude + ", " + longitude,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationError(String error) {
                Toast.makeText(DummyLocationTest.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnClickListener(v -> geolocation.requestLocationPermissions());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        geolocation.handlePermissionResult(requestCode, permissions, grantResults);
    }
}

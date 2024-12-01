package com.example.quacks_app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.GeoPoint;

import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code EntrantsMapActivity} class populates the entrants for a given event on the map, and
 * uses the {@code EntrantsMap} class to do so.
 */
public class EntrantsMapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrants_map_layout);

        MapView mapView = findViewById(R.id.map);
        EntrantsMap customMap = new EntrantsMap(this, mapView);

        ArrayList<User> entrants = (ArrayList<User>) getIntent().getSerializableExtra("Entrants");
        if (entrants == null) {
            finish();
        }

        List<GeoPoint> points = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        for (User user : entrants) {
            points.add(user.getGeoPoint());
            titles.add(user.getUserProfile().getUserName());
            descriptions.add(user.getUserProfile().getEmail());
        }

        Drawable markerIcon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_mylocation);
        if (markerIcon != null) {
            int width = 200;
            int height = 200;
            markerIcon.setBounds(0, 0, width, height);
            markerIcon.setTint(ContextCompat.getColor(this, R.color.black));
        }


        List<org.osmdroid.util.GeoPoint> convertedPoints = new ArrayList<>();
        for (GeoPoint point : points) {
            convertedPoints.add(new org.osmdroid.util.GeoPoint(point.getLatitude(), point.getLongitude()));
        }

        customMap.addMarkers(convertedPoints, titles, descriptions, markerIcon);

        customMap.centerMap(convertedPoints.get(0).getLatitude(), convertedPoints.get(0).getLongitude(), 15);
    }
}

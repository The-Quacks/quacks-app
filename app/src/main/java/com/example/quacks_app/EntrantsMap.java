package com.example.quacks_app;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class EntrantsMap {
    private final MapView mapView;

    public EntrantsMap(Context context, MapView mapView) {
        this.mapView = mapView;

        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mapView.setMultiTouchControls(true);
    }

    public void addMarker(double latitude, double longitude, String title, String description, Drawable icon) {
        GeoPoint point = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle(title);
        marker.setSubDescription(description);
        if (icon != null) {
            marker.setIcon(icon);
        }
        mapView.getOverlays().add(marker);
    }

    public void addMarkers(List<GeoPoint> points, List<String> titles, List<String> descriptions, Drawable icon) {
        for (int i = 0; i < points.size(); i++) {
            addMarker(points.get(i).getLatitude(), points.get(i).getLongitude(), titles.get(i), descriptions.get(i), icon);
        }
    }

    public void centerMap(double latitude, double longitude, int zoomLevel) {
        GeoPoint centerPoint = new GeoPoint(latitude, longitude);
        mapView.getController().setCenter(centerPoint);
        mapView.getController().animateTo(centerPoint, (double) zoomLevel, 1000L);
    }

    public void clearMarkers() {
        mapView.getOverlays().clear();
    }
}
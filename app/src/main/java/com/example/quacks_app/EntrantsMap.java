package com.example.quacks_app;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

/**
 * The {@code EntrantMap} class uses a built-in Android class to display a map, and then add
 * markers representing each entrant.
 */
public class EntrantsMap {
    private final MapView mapView;

    /**
     * Initialize the map
     * @param context The current activity
     * @param mapView The map layout file
     */
    public EntrantsMap(Context context, MapView mapView) {
        this.mapView = mapView;

        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mapView.setMultiTouchControls(true);
    }

    /**
     * Adds a single marker to the map with the specified settings
     * @param latitude The latitude of the marker to be added
     * @param longitude The longitude of the marker to be added
     * @param title The title to display when the marker is selected
     * @param description The description to display under the title when the marker is selected
     * @param icon The icon that is displayed on the map
     */
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

    /**
     * Adds multiple markers to the map at once. The order of each of the lists must match
     * @param points A list of the coordinates
     * @param titles A list of the titles
     * @param descriptions A list of the descriptions
     * @param icon The icon to use for all markers
     */
    public void addMarkers(List<GeoPoint> points, List<String> titles, List<String> descriptions, Drawable icon) {
        for (int i = 0; i < points.size(); i++) {
            addMarker(points.get(i).getLatitude(), points.get(i).getLongitude(), titles.get(i), descriptions.get(i), icon);
        }
    }

    /**
     * Centers the map on the given point
     * @param latitude The latitude to center on
     * @param longitude The longitude to center on
     * @param zoomLevel The amount of zoom (a larger value is more zoomed in)
     */
    public void centerMap(double latitude, double longitude, int zoomLevel) {
        GeoPoint centerPoint = new GeoPoint(latitude, longitude);
        mapView.getController().setCenter(centerPoint);
        mapView.getController().animateTo(centerPoint, (double) zoomLevel, 1000L);
    }

    /**
     * Removes all markers from the current map.
     */
    public void clearMarkers() {
        mapView.getOverlays().clear();
    }
}
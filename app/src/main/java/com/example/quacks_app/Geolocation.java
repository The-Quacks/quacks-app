package com.example.quacks_app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;

/**
 * The {@code Geolocation} class is used to get the device's current location, including the
 * permissions that are required to do so. It saves the location as the latitude and longitude to
 * be used by other functionality. To use it, you must create a Geolocation object, and then
 * define callbacks for when the location is received, or when there is an error.
 */
public class Geolocation {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private final Context context;
    private final Activity activity;
    private LocationCallback locationCallback;

    /**
     * The callback functions that need to be defined.
     */
    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
        void onLocationError(String error);
    }

    /**
     * Constructor for the Geolocation class.
     * @param context The current context
     * @param activity The current activity
     */
    public Geolocation(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * Stores the callback functions in the object
     * @param callback The LocationCallback functions
     */
    public void setLocationCallback(LocationCallback callback) {
        this.locationCallback = callback;
    }

    /**
     * This function asks the user for permission to access their location.
     * If access is granted, the current location is fetched, and then used elsewhere.
     * If access is denied, other functionality will not work.
     */
    public void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, "Location permission is required for this feature", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    /**
     * This function is called automatically by the overriden onRequestPermissionsResult function
     * @param requestCode An integer that identifies this permission request
     * @param permissions An array of the requested permissions. Not directly used by this function
     * @param grantResults An array indicating whether each permission was granted or not
     */
    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            } else {
                Toast.makeText(context, "Location permissions not granted", Toast.LENGTH_SHORT).show();
                if (locationCallback != null) {
                    locationCallback.onLocationError("Permissions denied");
                }
            }
        }
    }

    /**
     * Gets the device's current location
     */
    private void fetchCurrentLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) {
            if (locationCallback != null) {
                locationCallback.onLocationError("Location services not available");
            }
            return;
        }

        List<String> enabledProviders = locationManager.getProviders(true);
        Log.d("Geolocation", "Enabled providers: " + enabledProviders);

        String provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? LocationManager.NETWORK_PROVIDER : LocationManager.PASSIVE_PROVIDER;

        try {
            locationManager.requestSingleUpdate(provider, location -> {
                if (location != null && locationCallback != null) {
                    locationCallback.onLocationReceived(location.getLatitude(), location.getLongitude());
                }
            }, null);

            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null && locationCallback != null) {
                locationCallback.onLocationReceived(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            }
        } catch (SecurityException e) {
            Log.e("Geolocation", "Permission error: " + e.getMessage());
            if (locationCallback != null) {
                locationCallback.onLocationError("Location could not be found due to permission settings");
            }
        }
    }
}

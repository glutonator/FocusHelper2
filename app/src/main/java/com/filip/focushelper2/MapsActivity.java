package com.filip.focushelper2;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private String profileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //get profile name
        Intent previousIntent = getIntent();
        profileName = previousIntent.getStringExtra("profileName");
        if (profileName == null) {
            profileName = "temp";
            deleteSharedPreferences(profileName);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng positionMy;
        mMap = googleMap;
        getDeviceLocation();
        if (mLastKnownLocation == null) {
            positionMy = new LatLng(52.218942, 21.011645);
        } else {
            positionMy = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        }

        mMap.addMarker(new MarkerOptions().position(positionMy).title("Marker in myPosition"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMy));
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
//            if (mLocationPermissionGranted) {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location)task.getResult();
                        //
                        mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).title("Marker in myPosition"));
                        //
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.wtf("location", "Current location is null. Using defaults.");
                        Log.wtf("Exeception", "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
//            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void OnButtonClickSetLocation (View view) {
        double latitude =  mLastKnownLocation.getLatitude();
        double longitude =  mLastKnownLocation.getLongitude();
        Toast.makeText(this, "Hello!"+" "+latitude+" "+longitude, Toast.LENGTH_SHORT).show();

        String profileNameParams = profileName + "_Params";
        android.content.SharedPreferences sharedPreferences = getSharedPreferences(profileNameParams, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("latitude",(float)latitude);
        editor.putFloat("longitude",(float)longitude);
        editor.apply();
        finish();

    }
}

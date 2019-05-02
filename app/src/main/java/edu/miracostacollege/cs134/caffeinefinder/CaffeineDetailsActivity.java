package edu.miracostacollege.cs134.caffeinefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import edu.miracostacollege.cs134.caffeinefinder.model.Location;

public class CaffeineDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caffeine_details);

        TextView detailsNameTextView = findViewById(R.id.detailsNameTextView);
        TextView detailsAddressTextView = findViewById(R.id.detailsAddressTextView);
        TextView detailsPhoneTextView = findViewById(R.id.detailsPhoneTextView);
        TextView detailsCoordinatesTextView = findViewById(R.id.detailsCoordinatesTextView);

        Intent detailsIntent = getIntent();
        // Retrieve pet object from intent
        location = detailsIntent.getParcelableExtra("Selected Location");

        // Set TextViews
        detailsNameTextView.setText(location.getName());
        detailsAddressTextView.setText(location.getFullAddress());
        detailsPhoneTextView.setText(location.getPhone());
        detailsCoordinatesTextView.setText(location.getFormattedLatLng());

        // Load map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailsMapFragment);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        // Specify our location with LatLng class
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

        // Add Location's position to the map
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(location.getName()));

        // Create new camera location at location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(15.0f)
                .build();
        // Update the position (move to location)
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        // Instruct map to move to this position
        map.moveCamera(cameraUpdate);
    }
}

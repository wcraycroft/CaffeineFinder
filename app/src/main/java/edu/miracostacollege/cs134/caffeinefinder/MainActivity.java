package edu.miracostacollege.cs134.caffeinefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.miracostacollege.cs134.caffeinefinder.model.DBHelper;
import edu.miracostacollege.cs134.caffeinefinder.model.Location;
import edu.miracostacollege.cs134.caffeinefinder.model.LocationListAdapter;

//TODO: (1) Implement the OnMapReadyCallback interface for Google Maps
//TODO: First, you'll need to compile GoogleMaps in build.gradle
//TODO: and add permissions and your Google Maps API key in the AndroidManifest.xml
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DBHelper db;
    private List<Location> allLocationsList;
    private ListView locationsListView;
    private LocationListAdapter locationListAdapter;

    // TODO: Member variable to store the GoogleMap
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importLocationsFromCSV("locations.csv");

        allLocationsList = db.getAllCaffeineLocations();
        locationsListView = findViewById(R.id.locationsListView);
        locationListAdapter = new LocationListAdapter(this, R.layout.location_list_item, allLocationsList);
        locationsListView.setAdapter(locationListAdapter);

        //TODO: (2) Load the support map fragment asynchronously
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    // TODO: (3) Implement the onMapReady method, which will add a special "marker" for our current location,
    // TODO: which is  33.190802, -117.301805  (OC4800 building)
    // TODO: Then add normal markers for all the caffeine locations from the allLocationsList.
    // TODO: Set the zoom level of the map to 15.0f
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i("CaffeineFinder", "Entering onMapReady");    // debug

        map = googleMap;
        // Specify our location with LatLng class
        LatLng myPosition = new LatLng(33.190802, -117.301805);

        // Add position to the map
        map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));

        // Create new camera location at current location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myPosition)
                .zoom(15.0f)
                .build();
        // Update the position (move to location)
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        // Instruct map to move to this position
        map.moveCamera(cameraUpdate);

        // Add all caffeine locations
        LatLng position;
        for (Location location : allLocationsList)
        {
            position = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions()
            .position(position)
            .title(location.getName()));
        }
    }


    // TODO: (4) Create a viewLocationDetails(View v) method to create a new Intent to the
    // TODO: CaffeineDetailsActivity class, sending it the selectedLocation the user picked from the locationsListView
    public void viewLocationDetails(View view) {
        // Get Location object sent in list item object's tag property
        Location selectedLocation = (Location) view.getTag();
        // Instantiate details intent
        Intent detailsIntent = new Intent(this, CaffeineDetailsActivity.class);
        detailsIntent.putExtra("Selected Location", selectedLocation);
        // Launch activity
        startActivity(detailsIntent);
    }
}

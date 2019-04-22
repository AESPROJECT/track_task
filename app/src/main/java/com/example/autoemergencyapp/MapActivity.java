package com.example.autoemergencyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener , View.OnClickListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location last_location;
    LocationRequest locationRequest;
    private Button Setting , Logout;
    private FirebaseAuth mAuth;
    private FirebaseUser CutrrentUser;
    private  boolean EmergencyRes_Status = false;
    private static final int LOCATION_REQUEST = 50;
    private String[] LOCATION_PERMS = {android.Manifest.permission.ACCESS_FINE_LOCATION };
    private String[] LOCATION_PERMS2 = {Manifest.permission.ACCESS_COARSE_LOCATION};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mAuth = FirebaseAuth.getInstance();
        CutrrentUser = mAuth.getCurrentUser();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
 Setting = findViewById(R.id.settings);
        Logout = findViewById(R.id.logOut);

        Setting.setOnClickListener(this);
        Logout.setOnClickListener(this);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BuildGoogleApiClient();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
      /*  ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);*/

               mMap.setMyLocationEnabled(true);


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
    last_location = location;
    LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        String  userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference EmergencyResponderAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("Emergency Responder Available");
        GeoFire geoFire = new GeoFire(EmergencyResponderAvailabilityRef);
        geoFire.setLocation(userID, new GeoLocation(location.getLatitude() , location.getLongitude()));

    }

    @Override
    protected void onStop() {
        super.onStop();

      if(! EmergencyRes_Status)
          Dissconnect_The_Emergency_Responder();
    }

    private void Dissconnect_The_Emergency_Responder() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference EmergencyResponderAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("Emergency Responder Available");
        GeoFire geoFire = new GeoFire(EmergencyResponderAvailabilityRef);
        geoFire.removeLocation(userID);
    }

    protected  synchronized  void  BuildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
switch (v.getId())
{
    case R.id.settings:
        break;
    case R.id.logOut:
    {
        EmergencyRes_Status = true;
        Dissconnect_The_Emergency_Responder();
        mAuth.signOut();
        Logout();
    }
        break;
}
    }

    private void Logout() {
        Intent WelcomeIntent = new Intent(MapActivity.this , welcomActivity.class);
        WelcomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(WelcomeIntent);
        finish();
    }
}

package com.example.mypc.dragonradar;

        import android.Manifest;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentSender;
        import android.content.pm.PackageManager;
        import android.graphics.drawable.AnimationDrawable;
        import android.location.Location;
        import android.location.LocationManager;
        import android.media.MediaPlayer;
        import android.os.Build;
        import android.provider.Settings;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.common.api.ApiException;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.PendingResult;
        import com.google.android.gms.common.api.ResolvableApiException;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationCallback;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationResult;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.location.LocationSettingsRequest;
        import com.google.android.gms.location.LocationSettingsResponse;
        import com.google.android.gms.location.LocationSettingsResult;
        import com.google.android.gms.location.LocationSettingsStatusCodes;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapFragment;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.CameraPosition;
        import com.google.android.gms.maps.model.GroundOverlay;
        import com.google.android.gms.maps.model.GroundOverlayOptions;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap m_map;
    private Marker myMarker = null;
    private boolean mapReady = false;
    private int PERMISSION_CODE = 1;

    private TextView locationText;
    private LatLng myLoc = null;

    // GPS variables definition
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    // Zoom
    private float zoom1 = 16;
    public static float maxZoomOut = 14;
    private float maxZoomIn = 20;
    private float zoomStep = 0.5f;

    private GroundOverlay myGroundOverlay = null;
    private Cheats cheats;
    private Thread thread;
    private DBallDraw dBallDraw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkGPS())
        {
            setContentView(R.layout.activity_main);

            // Screen won't turn off automatically
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            locationText = findViewById(R.id.locText);

            locationUpdates();



            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
            mapFragment.getMapAsync(this);

        }
        else
        {
            setContentView(R.layout.bg);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;

        m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // For testing
        //m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng loc = new LatLng(54.717995, 25.294198);

        // Initialize myGroundOverlay
        myGroundOverlay = m_map.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.a25))
                .position(loc, 4000, 4000));

        //Initialize myMarker
        myMarker = m_map.addMarker(new MarkerOptions().position(loc));
        myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.center_dot));

        // Update camera position
        CameraPosition target = CameraPosition.builder().target(loc).zoom(zoom1).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

        zoomFunc();

        dBallDraw = new DBallDraw(this, m_map);
        thread = new Thread(dBallDraw);
        thread.start();

        cheats = new Cheats(this, m_map);
        cheats.disableButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (thread == null && mapReady)
        {
            thread = new Thread(dBallDraw);
            thread.start();
        }

        if (checkGPS())
        {
            startLocationUpdates();
        }

        if (dBallDraw != null)
        {
            dBallDraw.PlaySound(true);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        if (thread != null)
        {
            dBallDraw.PlaySound(false);
            mapReady = false;
            Thread m = thread;
            thread = null;
            m.interrupt();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (dBallDraw != null)
        {
            dBallDraw.PlaySound(false);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if (dBallDraw != null)
        {
            dBallDraw.PlaySound(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void locationUpdates()
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1500); //use a value fo about 10 to 15s for a real app
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations())
                {
                    //Update UI with location data
                    if (location != null)
                    {
                        myLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        if (mapReady)
                        {
                            myMarker.setPosition(myLoc);
                            myGroundOverlay.setPosition(myLoc);

                            CameraPosition target = CameraPosition.builder().target(myLoc).zoom(zoom1).build();
                            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

                            locationText.setText("Latitude: " + Math.round(location.getLatitude() * 100000) / 100000.0 + "\nLongtitude: " + Math.round(location.getLongitude() * 100000) / 100000.0);
                        }
                    }
                }
            }
        };
    }

    private void startLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
            }
        }
    }

    private void stopLocationUpdates()
    {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private boolean checkGPS() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private void zoomFunc()
    {
        Button btnZoomIn = findViewById(R.id.zoomIn);
        btnZoomIn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                if (mapReady && myLoc != null)
                {
                    if (zoom1 < maxZoomIn)
                    {
                        zoom1 = zoom1 + zoomStep;
                        CameraPosition target = CameraPosition.builder().target(myLoc).zoom(zoom1).build();
                        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
                    }

                }
            }
        });

        Button btnZoomOut = findViewById(R.id.zoomOut);
        btnZoomOut.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                if (mapReady && myLoc != null)
                {
                    if (zoom1 > maxZoomOut)
                    {
                        zoom1 = zoom1 - zoomStep;
                        CameraPosition target = CameraPosition.builder().target(myLoc).zoom(zoom1).build();
                        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
                    }
                }
            }
        });
    }
}

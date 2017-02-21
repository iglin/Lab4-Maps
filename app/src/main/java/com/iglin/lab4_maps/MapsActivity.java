package com.iglin.lab4_maps;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.iglin.lab4_maps.db.JourneyContentProvider;
import com.iglin.lab4_maps.model.Journey;
import com.iglin.lab4_maps.model.Point;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_POINT_CREATION = 1;

    private GoogleMap mMap;

    private JourneyContentProvider contentProvider;

    private Random random = new Random(System.currentTimeMillis());

    private Marker selectedMarker;
    private boolean makingJourney = false;
    private Journey currentJourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        contentProvider = new JourneyContentProvider(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Intent intent = new Intent(getApplicationContext(), NewPointActivity.class);
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);
                startActivityForResult(intent, REQUEST_POINT_CREATION);

                currentJourney = null;
                selectedMarker = null;
                makingJourney = false;
             //   findViewById(R.id.start_journey).setVisibility(View.INVISIBLE);
            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedMarker = marker;
             //   findViewById(R.id.start_journey).setVisibility(View.VISIBLE);

                if (makingJourney) {
                    if (marker.getPosition().latitude == currentJourney.getStartPoint().getLat() &&
                            marker.getPosition().longitude == currentJourney.getStartPoint().getLng()) {
                        Toast.makeText(getApplicationContext(), "You must choose different ending point!", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Title");

                    final EditText input = new EditText(getApplicationContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentJourney.setName(input.getText().toString());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                    Point point = contentProvider.readPoint(marker.getPosition().latitude, marker.getPosition().longitude);
                    currentJourney.setEndPoint(point);
                    Journey result = contentProvider.insertJourney(currentJourney);
                    if (result == null) {
                        Toast.makeText(getApplicationContext(), "These points are alrready connected!", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    makingJourney = false;
                    loadPoints();
                }
                openOptionsMenu();
                return false;
            }
        });

        loadPoints();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!((LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER)){
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.refresh:
               // intent = new Intent(this, NewRecordActivity.class);
              //  startActivity(intent);
                loadPoints();
                return true;
            case R.id.delete:
                // intent = new Intent(this, NewRecordActivity.class);
                //  startActivity(intent);
                contentProvider.deletePoint(selectedMarker.getPosition().latitude, selectedMarker.getPosition().longitude);
                loadPoints();
                return true;
            case R.id.start_journey:
                // intent = new Intent(this, NewRecordActivity.class);
                //  startActivity(intent);
                currentJourney = new Journey();
                Point point = contentProvider.readPoint(selectedMarker.getPosition().latitude, selectedMarker.getPosition().longitude);
                currentJourney.setStartPoint(point);
                makingJourney = true;
            //    findViewById(R.id.start_journey).setVisibility(View.INVISIBLE);
            //    findViewById(R.id.cancel_journey).setVisibility(View.VISIBLE);
                return true;
            case R.id.cancel_journey:
                // intent = new Intent(this, NewRecordActivity.class);
                //  startActivity(intent);
                makingJourney = false;
              //  findViewById(R.id.cancel_journey).setVisibility(View.INVISIBLE);
             //   findViewById(R.id.start_journey).setVisibility(View.VISIBLE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_POINT_CREATION && resultCode == RESULT_OK) {
          //  Intent id = data.getIntExtra()
       //     Bundle extras = data.getExtras();
         //   Bitmap imageBitmap = (Bitmap) extras.get("data");
            loadPoints();
        }
    }

    private void loadPoints() {
        List<Point> points = contentProvider.readPointsForMap();
        mMap.clear();
        for (Point point : points) {
            MarkerOptions markerOptions = new MarkerOptions().title(point.getTitle())
                    .snippet(point.getDescription())
                    .position(new LatLng(point.getLat(), point.getLng()));
            if (point.getIcon() != null) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(point.getIcon()));
            }
            mMap.addMarker(markerOptions);
        }

        List<Journey> journeys = contentProvider.readJourneys();
        for (Journey journey : journeys) {
            int color = - Color.BLACK + random.nextInt(Color.BLACK);
            mMap.addPolyline(journey.toPolyLine(color));
        }
    }
}

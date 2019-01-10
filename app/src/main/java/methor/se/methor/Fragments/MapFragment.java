package methor.se.methor.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

import methor.se.methor.Activities.MinigameActivity;
import methor.se.methor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LocationManager locationManager;
    private Location lastLocation;
    private LocList locationListener;
    private final float SPAWN_RADIUS = 0.005f;
    private Random rand;
    private boolean markersAreSpawned = false;
    private Marker userMarker;

    private String[] game_IDs = {GameMenuFragment.RPS_ID, GameMenuFragment.TTS_ID,
            GameMenuFragment.COMPASS_ID, GameMenuFragment.RICH_ID,
            GameMenuFragment.DICE_ID, GameMenuFragment.SHAKE_ID};


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        initLocation();
        rand = new Random();
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (!marker.getTitle().equals("User")) {

                    Intent intent = new Intent(getActivity().getApplicationContext(), MinigameActivity.class);
                    intent.putExtra("FragmentID", marker.getTitle());
                    startActivity(intent);
                    marker.remove();
                    createGameMarker(lastLocation,game_IDs[rand.nextInt(game_IDs.length)]);
                }


                return false;
            }
        });
    }


    public void setUserMarker(final double latitude, final double longitude) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {


                final LatLng name = new LatLng(latitude, longitude);
                userMarker = googleMap.addMarker(new MarkerOptions()
                        .position(name)
                        .title("User")


                );
                userMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.user));


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude)).zoom(15).build();

                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

            }


        });


    }


    private void initLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocList();

    }


    private void createGameMarkers(Location location) {

        for (int i = 0; i < 5; i++) {
            createGameMarker(location, game_IDs[rand.nextInt(game_IDs.length)]);

        }


    }

    private void createGameMarker(Location location, String id) {

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(getRandomLocation(location))
                .title(id)

        );

//        switch (id) {
//            case GameMenuFragment.RPS_ID:
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.scissor));
//                break;
//            case GameMenuFragment.COMPASS_ID:
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.scissor));
//                break;
//            case GameMenuFragment.DICE_ID:
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.scissor));
//                break;
//            case GameMenuFragment.RICH_ID:
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.scissor));
//                break;
//            case GameMenuFragment.SHAKE_ID:
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.scissor));
//                break;
//            case GameMenuFragment.TTS_ID:
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.scissor));
//                break;
//
//        }
        Log.d("MapFragment", "createGameMarker: " + marker.getPosition().latitude + ", " + marker.getPosition().longitude);
    }

    private LatLng getRandomLocation(Location location) {
        double offset = (rand.nextFloat() * SPAWN_RADIUS * 2) - SPAWN_RADIUS;
        Log.d("randomLoc", "getRandomLocation: Long" + offset);
        double longitude = location.getLongitude() + offset;


        offset = (rand.nextFloat() * SPAWN_RADIUS * 2) - SPAWN_RADIUS;
        double latitude = location.getLatitude() + offset;
        Log.d("randomLoc", "getRandomLocation: Lat" + offset);


        return new LatLng(latitude, longitude);
    }

    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
    }

    private class LocList implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;

            Log.d("LOCATION", "onLocationChanged: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());

            setUserMarker(location.getLatitude(), location.getLongitude());
            if (!markersAreSpawned && lastLocation != null) {
                createGameMarkers(lastLocation);
                markersAreSpawned = true;
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}


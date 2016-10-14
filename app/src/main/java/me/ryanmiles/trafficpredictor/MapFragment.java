package me.ryanmiles.trafficpredictor;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Ryan Miles on 10/13/2016.
 */

public class MapFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private Location mCurrentLocation;

    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initCamera(new LatLng(32.746748, -117.191312));
    }

    private void initCamera(final LatLng latLng) {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                CameraPosition position = CameraPosition.builder()
                        .target(latLng)
                        .zoom(15f)
                        .bearing(0.0f)
                        .tilt(0.0f)
                        .build();

                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), null);
                googleMap.setTrafficEnabled(false);
                googleMap.setMapType(MAP_TYPES[curMapTypeIndex]);
                googleMap.setTrafficEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(true);


                //Marker
                ArrayList<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(32.746748, -117.191312));
                latLngs.add(new LatLng(32.746784, -117.191904));
                latLngs.add(new LatLng(32.750068, -117.196046));
                latLngs.add(new LatLng(32.749919, -117.196234));
                latLngs.add(new LatLng(32.743605, -117.184656));
                latLngs.add(new LatLng(32.743425, -117.1848));
                for (LatLng lng : latLngs) {
                    MarkerOptions options = new MarkerOptions().position(lng);

                    options.icon(BitmapDescriptorFactory.defaultMarker());
                    googleMap.addMarker(options);
                }

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


}

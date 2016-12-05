package me.ryanmiles.trafficpredictor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import me.ryanmiles.trafficpredictor.event.UpdateDoftwEvent;
import me.ryanmiles.trafficpredictor.event.UpdateMonthEvent;
import me.ryanmiles.trafficpredictor.event.UpdateTimesEvent;
import me.ryanmiles.trafficpredictor.helper.SaveData;
import me.ryanmiles.trafficpredictor.helper.Util;
import me.ryanmiles.trafficpredictor.model.Station;
import me.ryanmiles.trafficpredictor.model.StationList;


/**
 * Created by Ryan Miles on 10/13/2016.
 */

public class MapFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = MapFragment.class.getCanonicalName();
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    boolean visible = true;
    private StationList stationList;
    private int curMapTypeIndex = 1;
    private GoogleMap googleMap;
    private int mMonthPos = 0;
    private int mDoftwPos = 0;
    private int mTimePos = 0;
    private ArrayList<Polyline> mPolylines;
    private ArrayList<Marker> mMarkers;
    private Station tempStation;
    private int temp1 = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        stationList = StationList.get(getContext());
        mPolylines = new ArrayList<>();
        mMarkers = new ArrayList<>();
        initCamera(new LatLng(32.746748, -117.191312));
    }

    private void initCamera(final LatLng latLng) {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googlemap) {
                googleMap = googlemap;
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
                try {
                    for (Station station : stationList.getStations()) {
                        if (!station.getLat().equals("")) {
                            MarkerOptions options = new MarkerOptions().position(new LatLng(Double.parseDouble(station.getLat()), Double.parseDouble(station.getLng())));
                            options.title(station.getName() + " (" + station.getID() + ")");
                            options.snippet("Click for more details");
                            options.icon(BitmapDescriptorFactory.defaultMarker());
                            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    int p = mMarkers.indexOf(marker);
                                    tempStation = stationList.getStationFromPostion(p);
                                    new MaterialDialog.Builder(getActivity())
                                            .title(tempStation.getName() + " (" + tempStation.getID() + "}")
                                            .content(tempStation.getInfo()).show();
                                }
                            });

                            mMarkers.add(googleMap.addMarker(options));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connectLines();
            }
        });
    }


    private void connectLines() {
        Log.d(TAG, "connectLines: ");

        clearPolyLines();

        for (Station station : stationList.getStations()) {
            if (station.getDirections() != null) {
                String color = Util.getColor(station.getmMonths().get(mMonthPos).getDays().get(mDoftwPos).getHours().get(mTimePos).getDelay());
                Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                        .addAll(station.getDirections())
                        .width(10)
                        .color(Color.parseColor(color))
                        .geodesic(true)
                );
                mPolylines.add(polyline);
            }
        }

    }

    private void clearPolyLines() {
        for (Polyline polyline : mPolylines) {
            polyline.remove();
        }
        mPolylines.clear();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(UpdateMonthEvent event) {
        Log.d(TAG, "onMessageEvent: UpdateMonth");
        mMonthPos = event.getPos();
        connectLines();
    }

    @Subscribe
    public void onMessageEvent(UpdateDoftwEvent event) {
        Log.d(TAG, "onMessageEvent: UpdateDoftw");
        mDoftwPos = event.getPos();
        connectLines();
    }

    @Subscribe
    public void onMessageEvent(UpdateTimesEvent event) {
        Log.d(TAG, "onMessageEvent: UpdateTime");
        mTimePos = event.getPos();
        connectLines();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggleMarkers:
                visible = !visible;
                for (Marker marker : mMarkers) {
                    marker.setVisible(visible);
                }
                break;
            case R.id.reset:
                SaveData.delete();
                break;


        }
        return true;

    }


}

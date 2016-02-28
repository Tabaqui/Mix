package ru.motleycrew.scheduler;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by User on 26.02.2016.
 */
public class PlaceFragment extends SupportMapFragment {

    private GoogleApiClient mClient;
    private GoogleMap mMap;

    public static PlaceFragment newInstance() {
        return new PlaceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(false.);
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateMap();
            }
        });
    }

    private void updateMap() {
        if (mMap == null) {
            return;
        }

        LatLng myPoint = new LatLng(55.0, 37.0);
        MarkerOptions myMarker = new MarkerOptions()
                .position(myPoint);
        LatLng nearPoint = new LatLng(60.0, 40.0);
        MarkerOptions nearMarker = new MarkerOptions()
                .position(nearPoint);
        mMap.clear();
        mMap.addMarker(myMarker);
        mMap.addMarker(nearMarker);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(myPoint)
                .include(nearPoint)
                .build();
        int margin = getResources().getDimensionPixelOffset(R.dimen.map_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}

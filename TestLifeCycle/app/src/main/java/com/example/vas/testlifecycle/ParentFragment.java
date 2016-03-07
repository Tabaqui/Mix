package com.example.vas.testlifecycle;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.zip.Inflater;

/**
 * Created by Vas on 02.03.16.
 */
public class ParentFragment extends Fragment {

    private static final String TAG = "ParentFragment";
    private static final String SAVED_MY_POINT = "myPoint";

    private String mValue;
    private GoogleMap mMap;
    private MapView mMapView;
    private GoogleApiClient mClient;
    private LatLng mMyPoint;
    private boolean mNoRefresh;


    public static Fragment newInstance() {
        return new ParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initGoogleClient();
        Log.d(TAG, "onCreate()");
    }

    private void initGoogleClient() {
        if (mClient != null) {
            return;
        }
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "Api client connected");
                        updateMyPoint();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "Api client suspended");
                    }
                })
                .build();
    }

    private void updateMyPoint() {
        if (mNoRefresh) {
            return;
        }
        mNoRefresh = !mNoRefresh;
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(0);
        request.setNumUpdates(1);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mMyPoint = new LatLng(location.getLatitude(), location.getLongitude());
                            updateUI();
                        }
                    });
        } catch (SecurityException se) {
            Log.e(TAG, "Perm not granted: " + se);
        }
    }

    private void updateUI() {
        if (mMyPoint == null) {
            return;
        }
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mMyPoint, 15);
        mMap.moveCamera(update);
        mMapView.onResume();
    }



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()" + hashCode());


        return initGoogleMap(inflater, container, savedInstanceState);
    }

    private View initGoogleMap(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mMapView != null && mMap != null) {
            if (savedInstanceState != null) {
                mMyPoint = savedInstanceState.getParcelable(SAVED_MY_POINT);
                if (mMyPoint != null) {
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mMyPoint, 15);
                    mMap.animateCamera(update);
                }
            }
            super.onCreateView(inflater, container, savedInstanceState);
        }
        View v = inflater.inflate(R.layout.fragment_text, container, false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
        mMapView.add
        return v;
//        MapsInitializer.initialize(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_MY_POINT, mMyPoint);
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
        Log.d(TAG, "onStart()");
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView");
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
    }
}

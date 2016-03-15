package ru.motleycrew.routes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by User on 26.02.2016.
 */
public class PlaceFragment extends Fragment {

    private static final String TAG = "PlaceFragment";
    private static final String WAY_POINT_STATE = "mWayPoint";
    private static final String MY_POINT_STATE = "mMyPoint";
    private static final String BUTTON_STATE = "buttonState";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String ARG_POINT_NAME = "pointName";

    private LatLng mMyPoint;
    private LatLng mWayPoint;
    private GoogleApiClient mClient;
    private Button mButton;
    private MapView mMapView;
    private GoogleMap mMap;
    private UICallback mUiUpdater;
    private String mPointName;

    public static final PlaceFragment getInstance(String pointName) {
        PlaceFragment fragment = new PlaceFragment();

        Bundle args = new Bundle();
        args.putString(ARG_POINT_NAME, pointName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPointName = getArguments().getString(ARG_POINT_NAME);
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "On connected called");
                        Log.d(TAG, "" + mClient.isConnecting());
                        Log.d(TAG, "" + mClient.isConnected());
                        findMe();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "On suspended called");
                    }
                })
                .build();
    }

    private void findMe() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.i(TAG, "Got a fix: " + location);
                            mMyPoint = new LatLng(location.getLatitude(), location.getLongitude());
                            updateCamera();
                        }
                    });
        } catch (SecurityException ex) {
            Log.e(TAG, "Google request permission exception: " + ex);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUiUpdater = (UICallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUiUpdater = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Call connect");
        mClient.connect();
        Log.d(TAG, "Connect have been called");
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mWayPoint != null) {
            outState.putParcelable(WAY_POINT_STATE, mWayPoint);
        }
        if (mMyPoint != null) {
            outState.putParcelable(MY_POINT_STATE, mMyPoint);
        }
        if (mButton.getText() != null) {
            outState.putString(BUTTON_STATE, mButton.getText().toString());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        mWayPoint = savedInstanceState.getParcelable(WAY_POINT_STATE);
        mMyPoint = savedInstanceState.getParcelable(MY_POINT_STATE);
        if (savedInstanceState.getString(BUTTON_STATE) != null) {
            mButton.setText(savedInstanceState.getString(BUTTON_STATE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Create view");
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        mMapView = (MapView) view.findViewById(R.id.map_container);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.isInfoWindowShown()) {
                            marker.hideInfoWindow();
                        } else {
                            marker.showInfoWindow();
                        }
                        return true;
                    }
                });
                updateCamera();
            }
        });

        mButton = (Button) view.findViewById(R.id.address);
        mButton.setText("...");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAutocompleteOverlay();
            }
        });
        return view;
    }

    private void createAutocompleteOverlay() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().
                    getErrorDialog(getActivity(), e.getConnectionStatusCode(), 0)
                    .show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mButton.setText(place.getName());
                mWayPoint = place.getLatLng();
                updateCamera();
                Log.i(TAG, "Place Selected: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void updateCamera() {
        mUiUpdater.update();
        mMap.clear();
        int margin = getResources().getDimensionPixelOffset(R.dimen.map_margin);
        if (mMyPoint != null && mWayPoint != null) {
            addMyMarker();
            addWayMarker();
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(mMyPoint)
                    .include(mWayPoint)
                    .build();
            CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
            mMap.animateCamera(update);
        } else if (mMyPoint != null) {
            addMyMarker();
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mMyPoint, 10);
            mMap.moveCamera(update);
        } else if (mWayPoint != null) {
            addWayMarker();
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mWayPoint, 10);
            mMap.moveCamera(update);
        }
    }

    public LatLng getWayPoint() {
        return mWayPoint;
    }

    public interface UICallback {
        void update();
    }

    private void addMyMarker() {
        MarkerOptions myMarker = new MarkerOptions();
        myMarker.position(mMyPoint);
        myMarker.title("You");
        mMap.addMarker(myMarker);
    }

    private void addWayMarker() {
        MarkerOptions wayMarker = new MarkerOptions();
        wayMarker.position(mWayPoint);
        wayMarker.title(mPointName);
        mMap.addMarker(wayMarker);
    }



}

package ru.motleycrew.scheduler;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;


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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by User on 26.02.2016.
 */
public abstract class PlaceFragment extends Fragment {

    private static final String TAG = "PlaceFragment";
    private static final String WAY_POINT_STATE = "mWayPoint";

    private LatLng myPoint;
    private LatLng mWayPoint;
    private GoogleApiClient mClient;
    private AutoCompleteTextView textView;
    private MapView mMapView;
    private GoogleMap mMap;
    private UICallback mUiUpdater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        findMe();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    public void setUiUpdater(UICallback uiUpdater) {
        mUiUpdater = uiUpdater;
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
                            myPoint = new LatLng(location.getLatitude(), location.getLongitude());
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
        mClient.connect();
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
        Log.d(TAG, "Search fragment destroyed");
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        LatLng point = savedInstanceState.getParcelable(WAY_POINT_STATE);
        mWayPoint = point;
        if (mWayPoint != null) {
            textView.setAdapter(null);
            textView.clearFocus();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Create view");
        View view = inflater.inflate(R.layout.fragment_from, container, false);
        final ArrayAdapter<LocationItem> adapter = new PredictionsAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        textView = (AutoCompleteTextView) view.findViewById(R.id.from_autocomplete);

        textView.setThreshold(3);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationItem item = (LocationItem) parent.getItemAtPosition(position);
                textView.setText(item.getPrediction());
                new SearchPlaceTask(item.getPlaceId()).execute();
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getAdapter() == null) {
                    textView.setAdapter(adapter);
                }
            }
        });
        mMapView = (MapView) view.findViewById(R.id.from_map_container);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                updateCamera();
            }
        });
        return view;
    }

    private void updateCamera() {
        if (myPoint == null) {
            if (mClient.isConnected()) {
                findMe();
            }
            return;
        }
        if (mWayPoint == null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myPoint, 10);
            mMap.moveCamera(cameraUpdate);
            return;
        }
        mMap.clear();
        MarkerOptions wayMarker = new MarkerOptions();
        wayMarker.position(mWayPoint);
        mMap.addMarker(wayMarker);
        int margin = getResources().getDimensionPixelOffset(R.dimen.map_margin);
        LatLngBounds bounds = LatLngBounds.builder()
                .include(myPoint)
                .include(mWayPoint)
                .build();
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);
        mUiUpdater.update();
    }

    private class SearchPlaceTask extends AsyncTask<Void, Void, Void> {
        private final String mPlaceId;
        private LatLng mPlace;

        public SearchPlaceTask(String placeId) {
            this.mPlaceId = placeId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            LocationsFetcher fetcher = new LocationsFetcher();
            mPlace = fetcher.getPlace(mPlaceId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mWayPoint = mPlace;
            updateCamera();
        }
    }

    public LatLng getWayPoint() {
        return mWayPoint;
    }

    private class PredictionsAdapter extends ArrayAdapter<LocationItem> {

        private LocationsFetcher mLocationsFetcher;

        public PredictionsAdapter(Context context, int resource) {
            super(context, resource);
            mLocationsFetcher = new LocationsFetcher();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            }
            LocationItem item = getItem(position);
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(item.getPrediction());
            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if (constraint != null) {
                        List<LocationItem> items = mLocationsFetcher.getPredictions(constraint.toString(), new LatLng(55.0, 37.0));
                        results.values = items;
                        results.count = items.size();
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        clear();
                        addAll((List<LocationItem>) results.values);
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public interface UICallback {
        void update();
    }

}

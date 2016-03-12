package ru.motleycrew.routes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vas on 04.03.16.
 */
public class WayFragment extends SupportMapFragment {

    private static final String ARG_FROM = "wayPoints";

    private LatLng mWayFrom;
    private LatLng mWayTo;
    private GoogleMap mMap;
    private List<LatLng> mMapWayPoints;

    public static final WayFragment getInstance(LatLng wayFrom, LatLng wayTo) {
        WayFragment fragment = new WayFragment();
        Bundle args = new Bundle();
        ArrayList<LatLng> wayPoints = new ArrayList<>();
        wayPoints.add(wayFrom);
        wayPoints.add(wayTo);
        args.putParcelableArrayList(ARG_FROM, wayPoints);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<LatLng> wayPoints = this.getArguments().getParcelableArrayList(ARG_FROM);
        mWayFrom = wayPoints.get(0);
        mWayTo = wayPoints.get(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);
                drawWay();
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void drawWay() {
        new DirectionAsyncTask(mWayFrom, mWayTo).execute();
    }

    private class DirectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private LatLng mWayFrom;
        private LatLng mWayTo;

        private List<LatLng> result;

        public DirectionAsyncTask(LatLng wayFrom, LatLng wayTo) {
            mWayFrom = wayFrom;
            mWayTo = wayTo;
        }

        @Override
        protected Void doInBackground(Void... params) {
            LocationsFetcher fetcher = new LocationsFetcher();
            result = fetcher.getDirections(mWayFrom, mWayTo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mMapWayPoints = result;
            updateMap();
        }
    }

    private void updateMap() {
        if (mMapWayPoints == null || mMapWayPoints.isEmpty()) {
            return;
        }
        mMap.clear();
        LatLngBounds bounds = LatLngBounds.builder()
                .include(mWayFrom)
                .include(mWayTo)
                .build();
        int margin = getResources().getDimensionPixelOffset(R.dimen.map_margin);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(cameraUpdate);
        PolylineOptions route = new PolylineOptions()
                .width(10)
                .color(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .addAll(mMapWayPoints)
                .geodesic(true);
        Polyline polyline = mMap.addPolyline(route);
    }
}

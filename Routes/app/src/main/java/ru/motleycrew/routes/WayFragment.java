package ru.motleycrew.routes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vas on 04.03.16.
 */
public class WayFragment extends SupportMapFragment {

    private static final String ARG_POINTS = "wayPoints";

    private LatLng mWayFrom;
    private LatLng mWayTo;
    private GoogleMap mMap;
    private Route mMapRoute;
    private MapOverlayCallback mOverlayCallback;

    public static final WayFragment getInstance(LatLng wayFrom, LatLng wayTo) {
        WayFragment fragment = new WayFragment();
        Bundle args = new Bundle();
        ArrayList<LatLng> wayPoints = new ArrayList<>();
        wayPoints.add(wayFrom);
        wayPoints.add(wayTo);
        args.putParcelableArrayList(ARG_POINTS, wayPoints);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<LatLng> wayPoints = this.getArguments().getParcelableArrayList(ARG_POINTS);
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
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        mOverlayCallback.update(mMapRoute);
                        return true;
                    }
                });
                drawWay();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOverlayCallback = (MapOverlayCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOverlayCallback = null;
    }

    private void drawWay() {
        new DirectionAsyncTask(mWayFrom, mWayTo).execute();
    }

    private class DirectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private LatLng mWayFrom;
        private LatLng mWayTo;

        private RouteResponse mRouteResponse;

        public DirectionAsyncTask(LatLng wayFrom, LatLng wayTo) {
            mWayFrom = wayFrom;
            mWayTo = wayTo;
        }

        @Override
        protected Void doInBackground(Void... params) {
            LocationsFetcher fetcher = new LocationsFetcher();
            mRouteResponse = fetcher.getDirections(mWayFrom, mWayTo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mRouteResponse.getState() == ResponseState.SUCCESS) {
                mMapRoute = mRouteResponse.getRoute();
                updateMap();
            } else {
                Toast.makeText(getActivity(), R.string.route_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateMap() {
        if (mMapRoute.getPolyline().isEmpty()) {
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
                .addAll(mMapRoute.getPolyline())
                .geodesic(true);
        mMap.addPolyline(route);
        double centerLat = (bounds.northeast.latitude + bounds.southwest.latitude) / 2;
        double centerLng = (bounds.northeast.longitude + bounds.southwest.longitude) / 2;
        LatLng center = new LatLng(centerLat, centerLng);
        double minDist = Double.MAX_VALUE;
        LatLng nearPoint = bounds.northeast;
        for (LatLng point : mMapRoute.getPolyline()) {
            if (dist(center, point) < minDist) {
                minDist = dist(center, point);
                nearPoint = point;
            }
        }
        MarkerOptions infoMarker = new MarkerOptions();
        infoMarker.position(nearPoint);
        infoMarker.title("Route");
        infoMarker.snippet(mMapRoute.getDistance() + "\n" + mMapRoute.getDuration() + "\n" + mMapRoute.getStartAddress() + "\n" + mMapRoute.getEndAddress());
        mMap.addMarker(infoMarker);
    }

    private double dist(LatLng from, LatLng to) {
        double horizontal = from.longitude - to.longitude;
        double vertical = from.latitude - to.latitude;
        return Math.sqrt(horizontal * horizontal + vertical * vertical);
    }

    interface MapOverlayCallback {
        void update(Route route);
    }
}

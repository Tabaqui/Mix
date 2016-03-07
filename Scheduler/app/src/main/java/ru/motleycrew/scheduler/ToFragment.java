package ru.motleycrew.scheduler;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 26.02.2016.
 */
public class ToFragment extends PlaceFragment {


    public static final ToFragment getInstance() {
        return new ToFragment();
    }

    public LatLng getToWayPoint() {
        return this.getWayPoint();
    }
}

package ru.motleycrew.scheduler;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 26.02.2016.
 */
public class FromFragment extends PlaceFragment {


    public static final FromFragment getInstance() {
        return new FromFragment();
    }

    public LatLng getFromWayPoint() {
        return this.getWayPoint();
    }
}

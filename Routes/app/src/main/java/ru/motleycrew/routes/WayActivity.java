package ru.motleycrew.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vas on 05.03.16.
 */
public class WayActivity extends AppCompatActivity {

    private static final String EXTRA_WAY_POINT = "ru.motleycrew.routes.way_point";

    public static Intent newIntent(Context context, LatLng pointFrom, LatLng pointTo) {
        Intent i = new Intent(context, WayActivity.class);
        ArrayList<Parcelable> extraPoints = new ArrayList<>();
        extraPoints.add(pointFrom);
        extraPoints.add(pointTo);
        i.putParcelableArrayListExtra(EXTRA_WAY_POINT, extraPoints);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        List<LatLng> wayPoints = getIntent().getParcelableArrayListExtra(EXTRA_WAY_POINT);
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.map_container);
        if (f == null) {
            f = WayFragment.getInstance(wayPoints.get(0), wayPoints.get(1));
            fm.beginTransaction()
                    .add(R.id.map_container, f)
                    .commit();
        }
    }
}

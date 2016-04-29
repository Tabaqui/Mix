package ru.motleycrew.presentation.event;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import ru.motleycrew.App;
import ru.motleycrew.R;
import ru.motleycrew.database.EventLab;
import ru.motleycrew.di.components.LoginComponent;
import ru.motleycrew.model.Event;

/**
 * Created by vnikolaev on 20.01.2016.
 */
public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {

    private static final String EXTRA_CRIME_ID = "ru.motleycrew.workbook.event_id";

    private ViewPager mViewPager;
    private List<Event> mEvents;

    @Inject
    EventLab eventLab;

    private LoginComponent component;

    protected void initDI() {
        component = ((App) getApplication())
                .getAppComponent()
                .plusLoginComponent();
        component.inject(this);
    }

    public static Intent newIntent(Context packageContext, String crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Event event) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        initDI();
        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        String crimeId = (String) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mEvents = eventLab.getEvent();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Event event = mEvents.get(position);
                return CrimeFragment.newInstance(event.getId());
            }

            @Override
            public int getCount() {
                return mEvents.size();
            }
        });
        for (int i = 0; i < mEvents.size(); i++) {
            if (mEvents.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

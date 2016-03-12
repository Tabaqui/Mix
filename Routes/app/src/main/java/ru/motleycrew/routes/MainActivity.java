package ru.motleycrew.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlaceFragment.UICallback {

    private static final String TAG = "MainActivity";

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PlaceFragment mFromFragment;
    private PlaceFragment mToFragment;

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);


        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity destroyed");
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<PageFragment<PlaceFragment>> mFragments;


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>(2);
            PageFragment pageFragment = new PageFragment("From");
            mFragments.add(pageFragment);
            pageFragment = new PageFragment("To");
            mFragments.add(pageFragment);
        }

        @Override
        public Fragment getItem(int position) {
            PlaceFragment fragment = mFragments.get(position).getFragment();
            if (fragment == null) {
                fragment = PlaceFragment.getInstance();
                mFragments.get(position).setFragment(fragment);
                if (position == 0) {
                    mFromFragment = fragment;
                }
                if (position == 1) {
                    mToFragment = fragment;
                }
            }
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PlaceFragment managedFragment = (PlaceFragment) super.instantiateItem(container, position);
            Fragment fragment = mFragments.get(position).getFragment();
            if (!managedFragment.equals(fragment)) {
                mFragments.get(position).setFragment(managedFragment);
                if (position == 0) {
                    mFromFragment = managedFragment;
                }
                if (position == 1) {
                    mToFragment = managedFragment;
                }
            }
            return managedFragment;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            MenuItem item = menu.findItem(R.id.menu_find_way);
            item.setEnabled(pointsReady());
            item.getIcon().setAlpha(pointsReady() ? 255 : 55);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_find_way:
                    Intent i = WayActivity.newIntent(this,
                            mFromFragment.getWayPoint(),
                            mToFragment.getWayPoint());
                    startActivity(i);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @Override
        public void update() {
            invalidateOptionsMenu();
        }

        private boolean pointsReady() {

            if (mFromFragment == null || mToFragment == null) {
                return false;
            } else {
                return mFromFragment.getWayPoint() != null && mToFragment.getWayPoint() != null;
            }
        }
    }

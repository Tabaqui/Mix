package com.example.vas.testlifecycle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "" + hashCode());
        setContentView(R.layout.fragment_parent);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.parent_container);
        if (fragment == null) {
            Log.d(TAG, "onCreate, create new fragment");
            fragment = ParentFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.parent_container, fragment)
                    .commit();
        } else {
            Log.d(TAG, "onCreate, exists");
        }
    }
}

package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by User on 29.03.2016.
 */
public class UserListActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT_ID = "com.bignerdranch.android.criminalintent.event_id";

    public static Intent newIntent(Context packageContext, UUID eventId) {
        Intent intent = new Intent(packageContext, UserListActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_user);
        UUID eventId = (UUID) getIntent().getSerializableExtra(EXTRA_EVENT_ID);
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.user_fragment_container);
        if (f == null) {
            f = UserFragment.newInstance(eventId);
            fm.beginTransaction()
                    .add(R.id.user_fragment_container, f)
                    .commit();
        }
    }
}

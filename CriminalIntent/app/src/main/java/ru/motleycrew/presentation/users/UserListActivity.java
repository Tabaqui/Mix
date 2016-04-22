package ru.motleycrew.presentation.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.motleycrew.R;

/**
 * Created by User on 29.03.2016.
 */
public class UserListActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT_ID = "ru.motleycrew.workbook.event_id";

    public static Intent newIntent(Context packageContext, String eventId) {
        Intent intent = new Intent(packageContext, UserListActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_user);
        String eventId = (String) getIntent().getSerializableExtra(EXTRA_EVENT_ID);
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.user_fragment_container);
        if (f == null) {
            f = UserListFragment.newInstance(eventId);
            fm.beginTransaction()
                    .add(R.id.user_fragment_container, f)
                    .commit();
        }
    }
}

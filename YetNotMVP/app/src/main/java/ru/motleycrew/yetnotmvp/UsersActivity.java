package ru.motleycrew.yetnotmvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by User on 22.03.2016.
 */
public class UsersActivity extends AppCompatActivity {

    public static Intent create(Context context) {
        Intent i = new Intent(context, UsersActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentManager.enableDebugLogging(true);
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = UsersFragment.getInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        fragmentManager.executePendingTransactions();
    }
}

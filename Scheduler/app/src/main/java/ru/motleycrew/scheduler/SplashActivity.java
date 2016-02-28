package ru.motleycrew.scheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by User on 26.02.2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(MainActivity.newInstance(this));
        finish();
    }
}

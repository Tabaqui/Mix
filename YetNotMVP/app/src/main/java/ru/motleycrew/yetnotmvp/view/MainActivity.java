package ru.motleycrew.yetnotmvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.motleycrew.yetnotmvp.R;
import ru.motleycrew.yetnotmvp.data.FirstService;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "ActivityMain";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = FirstService.getIntent(this);
        startService(i);
        Log.d(LOG, "Thread " + Thread.currentThread().getName());
    }
}

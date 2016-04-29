package ru.motleycrew.presentation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.motleycrew.presentation.events.CrimeListActivity;

/**
 * Created by User on 28.04.2016.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {
    }


    public void navigateToEventList(Context context) {
        if (context == null) {
            return;
        }
        Intent i = CrimeListActivity.newIntent(context);
        context.startActivity(i);
    }
}

package ru.motleycrew;

import android.content.ContentValues;
import android.content.Context;
import android.preference.PreferenceManager;

import javax.inject.Inject;

/**
 * Created by User on 28.04.2016.
 */

public class LocalPreferences {

    private static final String PREF_TOKEN = "token";

    private Context context;

    @Inject
    public LocalPreferences(Context context) {
        this.context = context;
    }

    public String getStoredToken() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_TOKEN, null);
    }

    public void setStoredToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_TOKEN, token)
                .apply();
    }
}

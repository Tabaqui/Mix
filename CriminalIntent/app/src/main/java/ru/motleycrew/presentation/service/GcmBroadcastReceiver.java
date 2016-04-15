package ru.motleycrew.presentation.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by User on 11.04.2016.
 */
public class GcmBroadcastReceiver extends GcmListenerService {

    public static final String GCM_TOPIC = "/topics/heroku_meetup_notify";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        if (from.equals(GCM_TOPIC)) {
            String messageId = data.getString("message");

        } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
}

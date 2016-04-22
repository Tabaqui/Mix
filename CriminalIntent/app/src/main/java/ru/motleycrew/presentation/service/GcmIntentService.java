package ru.motleycrew.presentation.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import ru.motleycrew.R;
import ru.motleycrew.model.Credentials;
import ru.motleycrew.repo.Fetcher;
import ru.motleycrew.repo.FetcherImpl;

/**
 * Created by User on 11.04.2016.
 */
public class GcmIntentService extends IntentService {

    private static final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getResources().getString(R.string.default_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);
            subscribeTopics(token);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String token) {
        Fetcher fetcher = new FetcherImpl(getApplicationContext());
//        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Credentials credentials = new Credentials(token, null , "tabaqui.vn@gmail.com");
        fetcher.updateRegistration(credentials);
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        pubSub.subscribe(token, GcmBroadcastReceiver.GCM_TOPIC, null);
    }
}

package ru.motleycrew.notyreciever;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by User on 06.04.2016.
 */
public class GcmBroadcastReceiver extends GcmListenerService {

    private static final String TAG = "GcmBroadcastReceiver";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        if (from.equals("/topics/tabaqui")) {

            Uri uri = Uri.parse("https://www.upwork.com/mobile/jobs/" + data.getString("url"));
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(data.getString("header"))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentText(data.getString("date") + "\n" + data.getString("header"))
                    .setContentTitle("Dough!")
                    .setVibrate(new long[]{1000, 1000, 1000})
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setLights(Color.RED, 3000, 3000)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(0, notification);
        } else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(data.getString("message"))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("dddd")
                    .setContentText("t")
                    .setVibrate(new long[]{1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setAutoCancel(true)
                    .build();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(0, notification);
        }
    }

}

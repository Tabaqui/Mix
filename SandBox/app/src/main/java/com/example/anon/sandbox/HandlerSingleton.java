package com.example.anon.sandbox;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Anon on 17.11.2016.
 */

public class HandlerSingleton {

    private static Handler handler;
    private static Runnable logger;

    private static boolean STOP = true;


    public static void start(final Context context) {
        if (!STOP) {
            return;
        }
        STOP = false;
        handler = new Handler();
        logger = new Runnable() {
            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                for (ScanResult result : wifiManager.getScanResults()) {
                    Log.d(HandlerSingleton.class.getSimpleName(), "SSID : " + result.SSID + "RSSI dbm : " + result.level);
                }
                if (!STOP) {
                    handler.postAtTime(logger, 200);
                }
            }
        };
        handler.post(logger);
    }

    public static void stop() {
        handler = null;
        logger = null;
        STOP = true;
    }
}

package com.example.anon.sandbox;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Anon on 17.11.2016.
 */

public class WifiService extends IntentService {
    private static String TAG = WifiService.class.getSimpleName();
    private static String COMMAND = "command";
    public static String START = "start";
    public static String STOP = "stop";
    private static String WORK = "work";
    private static String CURRENT_STATE = STOP;

    private static int counter = 5;
    private static int i = 0;
    private static long[] timings = new long[10];
    private static long nanotime;


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
//            Log.d("Handlers", "Called on main thread");
//            Log.d("Handlers", Thread.currentThread().getName());
            // Repeat this the same runnable code block again another 2 seconds


            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            timings[i] = (System.nanoTime() - nanotime) / 1000;
            i++;
            for (ScanResult result : wifiManager.getScanResults()) {


//                Log.d(TAG, "SSID : " + result.SSID + "RSSI dbm : " + result.level);
//                Log.d(TAG, );
//                Log.d(TAG, "RSSI relative (5 levels) : " + wifiManager.calculateSignalLevel(result.level, 5));
//                Log.d(TAG, "--------------------------------");
            }
            if (counter-- > 0) {
                handler.postDelayed(runnable, 1);
            } else {

                System.out.println(Arrays.toString(timings));
                counter = 5;
                i = 0;
            }
        }
    };


    public static Intent newIntent(Context context, String command) {
        Intent i = new Intent(context, WifiService.class);
        i.putExtra(COMMAND, command);
        return i;
    }

    public WifiService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(COMMAND);
        Log.d(TAG, "hash code" + this.hashCode());
        if (START.equals(command) || WORK.equals(command) && CURRENT_STATE.equals(WORK)) {
            Log.d(TAG, " Thread : " + Thread.currentThread().getName());
            Log.d(TAG, " Thread : " + Thread.currentThread());
            Log.d(TAG, " ------------------------------ ");

            nanotime = System.nanoTime();
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            for (ScanResult result : wifiManager.getScanResults()) {


                Log.d(TAG, "SSID : " + result.SSID + "RSSI dbm : " + result.level);
//                Log.d(TAG, );
//                Log.d(TAG, "RSSI relative (5 levels) : " + wifiManager.calculateSignalLevel(result.level, 5));
//                Log.d(TAG, "--------------------------------");
            }
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException ex) {
                Log.d(TAG, "Thread was iterrupted");
            }
            CURRENT_STATE = WORK;
            Intent i = WifiService.newIntent(this, WORK);
            startService(i);
        } else if (STOP.equals(command)) {
            CURRENT_STATE = STOP;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }
}

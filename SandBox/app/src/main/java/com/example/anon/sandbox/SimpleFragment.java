package com.example.anon.sandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Anon on 14.11.2016.
 */

public class SimpleFragment extends Fragment {

    private static String TAG = SimpleFragment.class.getSimpleName();

    private TextView textView;
    private Button startButton;
    private Button stopButton;

    public static SimpleFragment newInstance() {
        return new SimpleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_view, container, false);
        textView = (TextView) v.findViewById(R.id.text_view);
        textView.setText("BULB");
        startButton = (Button) v.findViewById(R.id.button);
        stopButton = (Button) v.findViewById(R.id.stop_button);



//        int numberOfLevels = 5;
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//        textView.setText(String.valueOf(level));
//
//        for (WifiConfiguration configuration : wifiManager.getConfiguredNetworks()) {
//            Log.d(TAG, configuration.SSID);
//
//        }

//        Log.d(TAG, "Main thread : " + Thread.currentThread().getName());
//        Log.d(TAG, "Main thread : " + Thread.currentThread());
//        Log.d(TAG, "=======================");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = WifiService.newIntent(getContext());
//                getActivity().startService(i);
                HandlerSingleton.start(getActivity().getApplicationContext());
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandlerSingleton.stop();
                Intent i = WifiService.newIntent(getContext(), WifiService.STOP);
                getActivity().startService(i);
            }
        });


        Intent i = WifiService.newIntent(getContext(), WifiService.START);
        getActivity().startService(i);


//        PendingIntent pi = PendingIntent.getService(getContext(), 0, i ,0);

        return v;
    }


}

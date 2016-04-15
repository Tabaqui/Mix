package ru.motleycrew.notyreciever;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ActivityMain";

    private GoogleCloudMessaging gcm;

    private Button mSendButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, GcmIntentService.class);
        startService(intent);
        gcm = GoogleCloudMessaging.getInstance(this);

        mSendButton = (Button) findViewById(R.id.bt_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("ECHO");
            }
        });

        mRegisterButton = (Button) findViewById(R.id.bt_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("REGISTER");
            }
        });
    }

    private AtomicInteger ccsMsgId = new AtomicInteger();
    private AsyncTask<Void, Void, String> sendTask;

    private void sendMessage(final String message) {
        sendTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                Bundle data = new Bundle();
                data.putString("ACTION", message);
                data.putString("CLIENT_MESSAGE", "Hello GCM CCS XMPP!");
                String id = Integer.toString(ccsMsgId.incrementAndGet());

                try {

                    gcm.send(getString(R.string.gcm_defaultSenderId) + "@gcm.googleapis.com", id,
                            data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Sent message.";
            }

            @Override
            protected void onPostExecute(String result) {
                sendTask = null;
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        sendTask.execute();
    }
}

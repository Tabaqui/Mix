package ru.motleycrew.service;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by RestUser on 14.04.2016.
 */
public class GcmMessageSender {

    private static String API_KEY = "AIzaSyCuIED2SRHQc52oGeKlAc08W2U1ufkHLGo";

    public static void send(UWMessage uwMessage, String target, boolean topic) {
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", uwMessage.getMessageId());

            if (topic) {
                if (target != null) {
                    jGcmData.put("to", "/topics/" + target);
                } else {
                    jGcmData.put("to", "/topics/global");
                }
            } else {
                jGcmData.put("to", target);
            }
            jGcmData.put("data", jData);

            // Create connection to send GCM UWMessage request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

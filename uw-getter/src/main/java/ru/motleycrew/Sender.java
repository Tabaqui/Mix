package ru.motleycrew;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import ru.motleycrew.model.UWMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by User on 06.04.2016.
 */
public class Sender {

    private static String API_KEY = "AIzaSyAHaZ2YLxRNrtMyf2AnquxwhHfpJCsCjFM";

    public static void send(UWMessage uwMessage, String topic) {
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
//            jData.put("message", uwMessage.getMessage());
            jData.put("header", uwMessage.getHeader());
            jData.put("url", uwMessage.getHref());
            jData.put("date", uwMessage.getDate());

            // Where to send GCM message.
            if (topic != null) {
                jGcmData.put("to", "/topics/" + topic);
            } else {
                jGcmData.put("to", "/topics/global");
            }
            // What to send in GCM message.
            jGcmData.put("data", jData);
            jGcmData.put("priority", "high");

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
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        }
    }
}

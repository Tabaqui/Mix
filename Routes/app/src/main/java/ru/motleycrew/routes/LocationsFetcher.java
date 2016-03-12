package ru.motleycrew.routes;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vas on 29.02.16.
 */
public class LocationsFetcher {

    private static final String TAG = "LocationFetcher";
    private static final String API_KEY = "AIzaSyB52WvSLLbs_WtbYXrnCgD8QRGoFmMh1CA";
    private static final String DIRECTIONS = "directions";
    private static final String FORMAT_JSON = "json";

    private static final Uri ENDPOINT = Uri.parse("https://maps.googleapis.com/maps/api")
            .buildUpon()
            .appendQueryParameter("key", API_KEY)
            .appendQueryParameter("language", "ru")
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        String result = new String(getUrlBytes(urlSpec));
        return result;
    }

    private String buildDirectionsUrl(LatLng wayFrom, LatLng wayTo) {
        Uri.Builder builder = ENDPOINT.buildUpon()
                .appendEncodedPath(DIRECTIONS)
                .appendPath(FORMAT_JSON)
                .appendQueryParameter("origin", wayFrom.latitude + "," + wayFrom.longitude)
                .appendQueryParameter("destination", wayTo.latitude + "," + wayTo.longitude);
        return builder.build().toString();
    }

    public List<LatLng> getDirections(LatLng wayFrom, LatLng wayTo) {
        String url = buildDirectionsUrl(wayFrom, wayTo);
        return downloadDirections(url);
    }

    private List<LatLng> downloadDirections(String url) {
        List<LatLng> waypoints = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            waypoints = parseDirection(jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch place: " + ioe);
        }
        return waypoints;
    }

    private List<LatLng> parseDirection(JSONObject jsonBody) throws JSONException {
        JSONArray jsonRoutes = jsonBody.getJSONArray("routes");

        JSONObject jsonRoute = jsonRoutes.getJSONObject(0);


        JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
        JSONObject jsonLeg = jsonLegs.getJSONObject(0);
        JSONArray jsonSteps = jsonLeg.getJSONArray("steps");
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < jsonSteps.length(); i++) {
            String polyline = jsonSteps.getJSONObject(i).getJSONObject("polyline").getString("points");
            points.addAll(decodePoly(polyline));
        }
        return points;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

}

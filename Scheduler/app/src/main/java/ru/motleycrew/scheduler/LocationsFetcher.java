package ru.motleycrew.scheduler;

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
    private static final String API_KEY = "AIzaSyBvuu7tSZmen5uBTkTL-bT4DQS_JVGdjZQ";
    private static final String QUERY = "input";
    private static final String LOCATION = "location";
    private static final String TYPES = "types";
    private static final String PLACE_ID = "placeid";
    private static final String AUTOCOMPLETE_PATH = "place/autocomplete";
    private static final String PLACE_DETAILS_PATH = "place/details";
    private static final String DIRECTIONS = "directions";
    private static final String FORMAT_JSON = "json";


    private static final Uri ENDPOINT = Uri.parse("https://maps.googleapis.com/maps/api")
            .buildUpon()
            .appendQueryParameter("key", API_KEY)
            .appendQueryParameter("language", "ru")
            .build();

//    public static final Uri PREDICTION_ENDPOINT = Uri.parse("https://maps.googleapis.com/maps/api/place/autocomplete/json")
//            .buildUpon()
//            .appendQueryParameter("key", API_KEY)
//            .appendQueryParameter("types", "geocode")
//            .appendQueryParameter("language", "ru")
//            .build();
//
//    public static  final Uri PLACE_ENDPOINT = Uri.parse("https://maps.googleapis.com/maps/api/place/details/json")
//            .buildUpon()
//            .appendQueryParameter("key", API_KEY)
//            .appendQueryParameter("language", "ru")
//            .build();

    public static final Uri DIRECTIONS_ENDPOINT = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
            .buildUpon()
            .appendQueryParameter("key", API_KEY)
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

    private String buildPredictionUrl(String address, LatLng location) {
        Uri.Builder builder = ENDPOINT.buildUpon()
                .appendEncodedPath(AUTOCOMPLETE_PATH)
                .appendPath(FORMAT_JSON)
                .appendQueryParameter(QUERY, address)
                .appendQueryParameter(TYPES, "geocode")
                .appendQueryParameter(LOCATION, "" + location.latitude + "," + location.longitude);
        return builder.build().toString();
    }

    private String buildPlaceUrl(String placeId) {
        Uri.Builder builder = ENDPOINT.buildUpon()
                .appendEncodedPath(PLACE_DETAILS_PATH)
                .appendPath(FORMAT_JSON)
                .appendQueryParameter(PLACE_ID, placeId);
        return builder.build().toString();
    }

    private String buildDirectionsUrl(LatLng wayFrom, LatLng wayTo) {
        Uri.Builder builder = ENDPOINT.buildUpon()
                .appendEncodedPath(DIRECTIONS)
                .appendPath(FORMAT_JSON)
                .appendQueryParameter("origin", wayFrom.latitude + "," + wayFrom.longitude)
                .appendQueryParameter("destination", wayTo.latitude + "," + wayTo.longitude);
        return builder.build().toString();
    }

    public List<LocationItem> getPredictions(String address, LatLng location) {
        String url = buildPredictionUrl(address, location);
        return downloadPredictions(url);
    }

    private List<LocationItem> downloadPredictions(String url) {
        List<LocationItem> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items: " + ioe);
        }
        return items;
    }

    private void parseItems(List<LocationItem> locationItems, JSONObject jsonBody) throws JSONException {
        JSONArray predictions = jsonBody.getJSONArray("predictions");
        for (int i = 0; i < predictions.length(); i++) {
            JSONObject predictionSummary = predictions.getJSONObject(i);
            JSONArray predictionTerms = predictionSummary.getJSONArray("terms");
            LocationItem item = new LocationItem();
            item.setPrediction(predictionTerms.getJSONObject(0).getString("value") + " " + predictionTerms.getJSONObject(1).getString("value"));
            item.setPlaceId(predictionSummary.getString("place_id"));
            locationItems.add(item);
        }
    }

    public LatLng getPlace(String placeId) {
        String url = buildPlaceUrl(placeId);
        return downloadPlace(url);
    }

    private LatLng downloadPlace(String url) {
        LatLng place = null;
        try {
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            place = parsePlace(jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON: " + je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch place: " + ioe);
        }
        return place;
    }

    private LatLng parsePlace(JSONObject jsonBody) throws JSONException {
        JSONObject jsonLocation = jsonBody.getJSONObject("result")
                .getJSONObject("geometry")
                .getJSONObject("location");

        LatLng place = new LatLng(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng"));
        return place;
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
//            JSONObject location = jsonSteps.getJSONObject(i);
//            LatLng point = getPoint(location.getJSONObject("start_location"));
//            points.add(point);
//            point = getPoint(location.getJSONObject("end_location"));
//            points.add(point);
//        }
        return points;
    }

    private LatLng getPoint(JSONObject location) throws JSONException {
        return new LatLng(location.getDouble("lat"), location.getDouble("lng"));
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
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

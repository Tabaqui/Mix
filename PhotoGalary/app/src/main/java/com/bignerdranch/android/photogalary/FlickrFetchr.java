package com.bignerdranch.android.photogalary;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vnikolaev on 06.02.2016.
 */
public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "e39efafc3154dbd236ce54e4b15d0e34";
    private static final String FETCH_RECENT_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
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
            int bytesRead = 0;
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
        ByteBuffer wrapper = ByteBuffer.wrap(getUrlBytes(urlSpec));
        return Charset.forName("UTF-8").decode(wrapper).toString();
    }

    public String getUrlString0(String urlSpec) throws IOException {
        BufferedReader reader = new BufferedReader(getUrlReader(urlSpec));
        StringBuffer responseBuffer = new StringBuffer();
        String read;
        while ((read = reader.readLine()) != null) {
            responseBuffer.append(read);
        }
        return responseBuffer.toString();
    }

    private Reader getUrlReader(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader stream = new InputStreamReader(connection.getInputStream(), "UTF-8");
        return stream;
    }

    private String buildUrl(String method, String query) {
        Uri.Builder builder = ENDPOINT.buildUpon()
                .appendQueryParameter("method", method);
//                .appendQueryParameter("page", Integer.toString(page));
        if (method.equals(SEARCH_METHOD)) {
            builder.appendQueryParameter("text", query);
        }
        return builder.build().toString();
    }

    public List<GalleryItem> fetchRecentPhotos() {
        String url = buildUrl(FETCH_RECENT_METHOD, null);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query) {
        String url = buildUrl(SEARCH_METHOD, query);
        return downloadGalleryItems(url);
    }

    private  List<GalleryItem> downloadGalleryItems(String url) {
        List<GalleryItem> items = new ArrayList<>();
        try {
            String jsonString = getUrlString0(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems0(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

//    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException, JSONException {
//        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
//        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
//        for (int i = 0; i < photoJsonArray.length(); i++) {
//            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
//            GalleryItem item = new GalleryItem();
//            item.setId(photoJsonObject.getString("id"));
//            item.setCaption(photoJsonObject.getString("title"));
//            if (!photoJsonObject.has("url_s")) {
//                continue;
//            }
//            item.setUrl(photoJsonObject.getString("url_s"));
//            items.add(item);
//
//        }
//    }

    private void parseItems0(List<GalleryItem> items, JSONObject jsonBody) {
        try {
            Type listType = new TypeToken<List<GalleryItem>>() {
            }.getType();
            Log.i(TAG, jsonBody.getString("photos"));
            List<GalleryItem> collections = new Gson().fromJson(jsonBody.getJSONObject("photos").getString("photo"), listType);
            items.addAll(collections);
        } catch (JSONException je) {
            Log.i(TAG, "Failed to parse JSON", je);
        }
    }
}

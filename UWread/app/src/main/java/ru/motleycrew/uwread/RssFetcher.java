package ru.motleycrew.uwread;

import android.net.Uri;
import android.sax.Element;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by User on 14.03.2016.
 */
public class RssFetcher {

    private static final String TAG = "RssFetcher";

    private static final String NS = null;
    //    private static final String URL = "https://www.upwork.com/o/jobs/browse/?q=android%20-ios%20-apple%20-iphone%20-ipad&sort=create_time%2Bdesc";
    private static final Uri ENDPOINT = Uri.parse("https://www.upwork.com/jobs/rss")
            .buildUpon()
            .appendQueryParameter("q", "android -ios -apple -iphone -ipad")
            .appendQueryParameter("sort", "create_time+desc")
            .build();

    private List<RssItem> mRssItems;

    public void getRss() throws XmlPullParserException, IOException {
        mRssItems = new ArrayList<>();
        URL url = new URL(ENDPOINT.toString());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(connection.getResponseMessage() + ": with " + url);
        }
        BufferedInputStream buf = new BufferedInputStream(connection.getInputStream());
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(buf, null);

        parser.nextTag();
        readFeed(parser);
        connection.disconnect();
    }

    private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, "rss");
        int event = parser.next();
        while (!"rss".equals(parser.getName()) || event != XmlPullParser.END_TAG) {
            if ("channel".equals(parser.getName())) {
                readChannel(parser);
            }
            event = parser.next();
        }
    }

    private void readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, "channel");
        int event = parser.next();
        while (!"channel".equals(parser.getName()) || event != XmlPullParser.END_TAG) {
            if ("item".equals(parser.getName())) {
                Log.d(TAG, "item");
                readItem(parser);
            }
            event = parser.next();
        }
        parser.require(XmlPullParser.END_TAG, NS, "channel");
    }

    private void readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, "item");
        int event = parser.next();
        RssItem item = new RssItem();
        while (!"item".equals(parser.getName()) || event != XmlPullParser.END_TAG) {
            if ("title".equals(parser.getName())) {
                item.setTitle(parser.getText());
            }
            if ("link".equals(parser.getName())) {
                item.setUrl(parser.getText());
            }
            event = parser.next();
        }
    }
}

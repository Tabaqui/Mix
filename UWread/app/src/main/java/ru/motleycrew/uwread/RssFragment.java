package ru.motleycrew.uwread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by User on 14.03.2016.
 */
public class RssFragment extends Fragment {

    private static final String TAG = "RssFragment";

    private TextView mRssText;

    final public static RssFragment newInstance() {
        return new RssFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRssText = (TextView) v.findViewById(R.id.rss_xml_text);
        new RssGetter().execute();
        return v;
    }

    private class RssGetter extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new RssFetcher().getRss();
            } catch (XmlPullParserException | IOException ioe) {
                Log.e(TAG, "Failed Fetcher Service", ioe);
            }
//            mRssText.setText(")(");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRssText.setText(")(");
        }
    }
}

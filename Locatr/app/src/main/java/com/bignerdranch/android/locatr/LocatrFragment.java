package com.bignerdranch.android.locatr;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

/**
 * Created by User on 24.02.2016.
 */
public class LocatrFragment extends Fragment {

    private static final String TAG = "LocatrFramgent";

    private ImageView mImageView;
    private GoogleApiClient mClient;

    public static LocatrFragment newInstance() {
        return new LocatrFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_locatr, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);
        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                findImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findImage() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "Got a fix: " + location);
                        new SearchTask().execute(location);
                    }
                });
    }

    private class SearchTask extends AsyncTask<Location, Integer, Void> {
        private GalleryItem mGalleryItem;
        private Bitmap mBitmap;

        private ProgressDialog mProgressDialog;

        @Override
        protected Void doInBackground(Location... params) {
            FlickrFetchr fetchr = new FlickrFetchr();
            List<GalleryItem> items = fetchr.searchPhotos(params[0]);
            if (items.size() == 0) {
                return null;
            }
            publishProgress(33);
            mGalleryItem = items.get(0);
            try {
                byte[] bytes = fetchr.getUrlBytes(mGalleryItem.getUrl());
                publishProgress(33);
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                publishProgress(33);
            } catch (IOException ioe) {
                Log.i(TAG, "Unable to download bitmap", ioe);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.incrementProgressBy(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.setTitle("Loading");
            mProgressDialog.setMessage("...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mImageView.setImageBitmap(mBitmap);
            mProgressDialog.dismiss();
        }
    }
}

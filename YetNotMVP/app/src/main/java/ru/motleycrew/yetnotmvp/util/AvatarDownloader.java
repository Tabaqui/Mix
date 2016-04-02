package ru.motleycrew.yetnotmvp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by User on 22.03.2016.
 */
public class AvatarDownloader<T> {

    private static String TAG = "Downloader";

    private AccessToken mAccessToken;
    private PublishSubject<String> mAvatarSubject;
    private Map<String, T> mHolders = new HashMap<>();
    private DownloadListener<T> mListener;

    private static class Container {
        private final String mId;
        private final Bitmap mBitmap;

        public Container(Bitmap bitmap, String id) {
            mBitmap = bitmap;
            mId = id;
        }

    }

    public interface DownloadListener<T> {
        void onDownloaded(T holder, Bitmap bitmap);
    }

    public void setDownloadListener(DownloadListener<T> listener) {
        this.mListener = listener;
    }

    public void start(String url, T holder) {
        mHolders.put(url, holder);
        mAvatarSubject.onNext(url);
    }

    public void get(String id) {

    }

    public void cancel(String id) {

    }

    public AvatarDownloader() {
        mAccessToken = AccessToken.getCurrentAccessToken();
        mAvatarSubject = PublishSubject.create();
        mAvatarSubject.observeOn(Schedulers.newThread())
                .flatMap(new Func1<String, Observable<Container>>() {
                    @Override
                    public Observable<Container> call(String s) {
                        Log.d(TAG, "Flat map call" + Thread.currentThread().getName());
                        Bitmap icon1 = null;
                        try {
                            URL url = new URL(s);
                            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                            InputStream is = connection.getInputStream();
                            icon1 = BitmapFactory.decodeStream(is);
                            Log.d(TAG, "icon decode");
                        } catch (MalformedURLException ex) {
                            Log.e(TAG, "Incorrect url ", ex);
                        } catch (IOException ex) {
                            Log.e(TAG, "Could not craeate connection ", ex);
                        }
                        return Observable.just(new Container(icon1, s));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Container>() {
                    @Override
                    public void call(Container container) {
                        Log.d(TAG, "subsriber call " + Thread.currentThread().getName());
                        mListener.onDownloaded(mHolders.get(container.mId), container.mBitmap);
                    }
                });
    }

    private GraphRequest createRequest(String id) {
        Bundle parameters = new Bundle();
        parameters.putString("type", "small");
        return new GraphRequest(
                mAccessToken,
                id + "/picture",
                parameters,
                HttpMethod.GET);
    }
}

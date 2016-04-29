package ru.motleycrew.repo;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.motleycrew.database.EventLab;
import ru.motleycrew.model.Credentials;
import ru.motleycrew.model.Event;
import ru.motleycrew.network.NotyRestService;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by User on 13.04.2016.
 */
public class FetcherImpl implements Fetcher {

    private static final String TAG = "FetcherImpl";

    private Retrofit retrofit;
    private EventLab eventLab;

//    public FetcherImpl(Context context) {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .build();
//
//        eventLab = EventLab.get(context);
//
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy.MM.dd'T'HH:mm:ssZ")
//                .create();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.105:8080")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(client)
//                .build();
//    }

    public FetcherImpl(EventLab lab) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        eventLab = lab;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy.MM.dd'T'HH:mm:ssZ")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.105:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }



    @Override
    public void downloadMessage(String id) {
        NotyRestService service = retrofit.create(NotyRestService.class);
        Observable<Event> text = service.getMessage(id);

        text.subscribeOn(Schedulers.io())
                .subscribe(new Action1<Event>() {
                    @Override
                    public void call(Event event) {
                        persistToDb(event);
                    }
                });
    }

    private void persistToDb(Event event) {
        eventLab.addEvent(event);
    }

    public void pushMessage(Event refEvent) {
        NotyRestService service = retrofit.create(NotyRestService.class);
        Observable<Object> response = service.createMessage(refEvent);

        response.subscribeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    public void call(Object resp) {
                        // TODO: Don't know what is this.
                        Log.d(TAG, "message send");
                        Log.d(TAG, resp.toString());
                    }
                });
    }

    public void updateRegistration(Credentials credentials) {
        NotyRestService service = retrofit.create(NotyRestService.class);
        Observable<Object> response = service.sendToken(credentials);

        response.subscribeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    public void call(Object resp) {
                        // TODO: Save own hash
                        Log.d(TAG, "registered");
                        Log.d(TAG, resp.toString());
                    }
                });
    }

    @Override
    public Observable<Pair<String, String>> register(final Pair<String, String> credentials) {
        NotyRestService service = retrofit.create(NotyRestService.class);
        Observable<Pair<String, String>> responce = service.hash(credentials)
                .subscribeOn(Schedulers.io());
        return responce;
    }

    @Override
    public void register2(Pair<String, String> credentials) {
        NotyRestService service = retrofit.create(NotyRestService.class);

        service.hash(credentials)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        Log.d(TAG, " + |" + s);
                        Log.d(TAG, " + |" + ((Map) s).get("hash"));
                    }
                });
    }

    //    public List<String> getTokens(List<String> logins) {
//        NotyRestService service = retrofit.create(NotyRestService.class);
//        Observable<Pair<String, String>> responce = service.getTokens(logins);
//
//        responce.subscribeOn((Schedulers.io()))
//                .subscribe(new Action1<Pair<String, String>>() {
//                    @Override
//                    public void call(Pair<String, String> strings) {
//                        for (Participant p : eventLab.getParticipants(null)) {
//
//                        }
//                    }
//                })
//    }
}

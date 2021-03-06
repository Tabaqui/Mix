package ru.motleycrew.network;

import android.util.Pair;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.motleycrew.model.Credentials;
import ru.motleycrew.model.Event;
import rx.Observable;

/**
 * Created by User on 13.04.2016.
 */
public interface NotyRestService {

    @GET("messages?")
    Observable<Event> getMessage(@Query("id") String messageId);

    @POST("user")
    Observable<Object> sendToken(@Body Credentials simple);

    @POST("/messages/add")
    Observable<Object> createMessage(@Body Event message);
//
//    @POST("/friends")
//    Observable<List<String>> getEmails(@Body List<String> emails);

    @POST("/register")
    Observable<Pair<String, String>> hash(@Body Pair<String, String> cred);
}

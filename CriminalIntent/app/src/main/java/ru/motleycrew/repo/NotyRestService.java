package ru.motleycrew.repo;

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
    Observable<Event> getCurrent(@Query("text") String text);

    @POST("user")
    Observable<Object> sendToken(@Body Credentials simple);

    @POST("/messages/add")
    Observable<Object> createMessage(@Body Event message);
}

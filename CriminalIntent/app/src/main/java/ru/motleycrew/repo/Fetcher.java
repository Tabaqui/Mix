package ru.motleycrew.repo;


import android.util.Pair;

import ru.motleycrew.model.Credentials;
import ru.motleycrew.model.Event;
import rx.Observable;

/**
 * Created by User on 13.04.2016.
 */
public interface Fetcher {

    void downloadMessage(String messageId);

    void pushMessage(Event refEvent);

    void updateRegistration(Credentials credentials);

    Observable<Pair<String, String>> register(Pair<String, String> credentials);

    void register2(Pair<String, String> credentials);
}

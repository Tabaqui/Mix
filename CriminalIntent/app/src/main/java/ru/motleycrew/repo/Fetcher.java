package ru.motleycrew.repo;

import ru.motleycrew.model.Credentials;
import ru.motleycrew.model.Event;

/**
 * Created by User on 13.04.2016.
 */
public interface Fetcher {

    void downloadMessage(Event event);
    void pushMessage(Event refEvent);
    void updateRegistration(Credentials credentials);
}

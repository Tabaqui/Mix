package ru.motleycrew.repo;

import ru.motleycrew.model.Credentials;
import ru.motleycrew.model.Event;

/**
 * Created by User on 13.04.2016.
 */
public interface Fetcher {

    void downloadMessage(String messageId);
    void pushMessage(Event refEvent);
    void updateRegistration(Credentials credentials);
    void register(String cred);
}

package ru.motleycrew.service;

import ru.motleycrew.controller.json.IncomingMessage;
import ru.motleycrew.entity.Data;

/**
 * Created by User on 28.04.2016.
 */
public interface MessageService {

    public Data getData(IncomingMessage message);

    public IncomingMessage getMessage(Data data);

}

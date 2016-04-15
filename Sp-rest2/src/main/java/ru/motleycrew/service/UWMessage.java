package ru.motleycrew.service;

/**
 * Created by RestUser on 14.04.2016.
 */
public class UWMessage {


    private final String messageId;

    public UWMessage(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}

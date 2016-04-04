package ru.motleycrew.utils;

/**
 * Created by vas on 03.04.16.
 */
public class RestException extends Exception {

    public RestException() {

    }

    public RestException(String message) {
        super(message);
    }

    public RestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestException(Throwable cause) {
        super(cause);
    }

    public RestException(String message, Throwable cause, boolean enableSupersession, boolean writeStackTrace) {
        super(message, cause, enableSupersession, writeStackTrace);
    }
}

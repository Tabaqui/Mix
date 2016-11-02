package ru.motleycrew.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 11.04.2016.
 */
public class UWMessage {

//    private static final String UW_HOME = "https://upwork.com";

    private final String header;
    private final String href;
    private final String date;

    public UWMessage(String header, String date, String href) {
        this.header = header;
        this.href = "_" + href;
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public String getHref() {
        return href;
    }

    public String getDate() {
        return date;
    }
}

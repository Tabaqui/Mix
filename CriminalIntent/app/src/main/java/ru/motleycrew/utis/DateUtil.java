package ru.motleycrew.utis;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by User on 30.03.2016.
 */
public class DateUtil {

    public static String longDate(Date date) {
        return DateFormat.format("EEEE, MMM dd, yyyy", date).toString();
    }

    public static String timeAndDate(Date date) {
        return DateFormat.format("dd MMM, EEE, kk:mm", date).toString();
    }
}

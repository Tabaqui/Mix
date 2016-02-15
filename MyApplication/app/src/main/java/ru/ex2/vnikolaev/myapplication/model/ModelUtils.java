package ru.ex2.vnikolaev.myapplication.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vnikolaev on 05.02.2016.
 */
public class ModelUtils {

    public static long hours(long milliseconds) {
        return milliseconds / 1000 / 60 / 60;
    }

    public static long minToEndDate(Date date) {
        return toEndDate(date) / 1000 / 60;
    }

    public static boolean bwStartEndDay(Date date) {
        return date.after(getStartDay(date)) && date.before(getEndDay(date));
    }

    public static long toEndDay(Date date) {
        return getEndDay(date).getTime() - date.getTime();
    }

    public static long toEndDate(Date date) {
        return date.getTime() - getStartDay(date).getTime();
    }

    public static Date getEndDay(Date date) {
        return truncate(date, Calendar.HOUR, 18);
    }

    public static Date getStartDay(Date date) {
        return truncate(date, Calendar.HOUR, 10);
    }

    private static Date truncate(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(field, value);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date addWorkingHours(Date date, long hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (bwStartEndDay(date)) {
            int diff =(int) hours(toEndDate(date) - date.getTime());
            c.add(Calendar.HOUR, 16 * (diff / 8) + diff);
        } else if (date.before(getStartDay(date))) {
            date = truncate(date, Calendar.HOUR, 10);
            c.setTime(date);
            c.add(Calendar.HOUR, (int) (16 * (hours / 8) + hours));
        } else if (date.after(getEndDay(date))) {
            date = truncate(date, Calendar.HOUR, 18);
            c.setTime(date);
            c.add(Calendar.HOUR, (int) (16 * (hours / 8) + hours + 16));
        }
        return c.getTime();
    }

}

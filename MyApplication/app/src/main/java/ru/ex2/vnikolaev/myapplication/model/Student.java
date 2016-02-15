package ru.ex2.vnikolaev.myapplication.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by vnikolaev on 05.02.2016.
 */
public class Student {

    private String mId;
    private String mName;
    private String mLastName;
    private Date mStartAt;
    private int mWorkingHours;

    public Student(String name, String lastName) {
        this.mId = UUID.randomUUID().toString();
        this.mName = name;
        this.mLastName = lastName;
        this.mStartAt = new Date();
    }

    public String getName() {
        return mName;
    }

    public String getLastName() {
        return mLastName;
    }

    public Date getStartAt() {
        return mStartAt;
    }

    public int getWorkingHours() {
        return mWorkingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.mWorkingHours = workingHours;
    }

    public String getWorkingStatus() {
        if (mWorkingHours == 0) {
            return "Учеба не назначена.";
        }
        Calendar calendar = Calendar.getInstance();
//        long difference = calendar.getTimeInMillis() - getStartAt().getTime();
        int firstDayMinutes = 0;
        if (ModelUtils.bwStartEndDay(mStartAt)) {
            firstDayMinutes = (int) ModelUtils.minToEndDate(mStartAt);
        }
        int workingMinutes = mWorkingHours * 60 - firstDayMinutes;
        int days = workingMinutes / 60 / 8;
        int hours = (workingMinutes - days * 60 * 8) / 60;
        calendar.setTime(ModelUtils.getEndDay(mStartAt));
        calendar.add(Calendar.DAY_OF_MONTH, days);
        calendar.add(Calendar.HOUR, hours);
        boolean finished = calendar.getTime().after(new Date());

    }

}

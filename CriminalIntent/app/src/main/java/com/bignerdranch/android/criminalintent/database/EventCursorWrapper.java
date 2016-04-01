package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent.model.Event;
import com.bignerdranch.android.criminalintent.database.EventDBSchema.EventTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by vnikolaev on 25.01.2016.
 */
public class EventCursorWrapper extends CursorWrapper {


    public EventCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Event getEvent() {
        String uuidString = getString(getColumnIndex(EventTable.Cols.UUID));
        String title = getString(getColumnIndex(EventTable.Cols.TITLE));
        long date = getLong(getColumnIndex(EventDBSchema.EventTable.Cols.DATE));
        int isApproved = getInt(getColumnIndex(EventTable.Cols.APPROVED));
        Event event = new Event(UUID.fromString(uuidString));
        event.setTitle(title);
        event.setDate(new Date(date));
        event.setApproved(isApproved != 0);
        return event;
    }
}

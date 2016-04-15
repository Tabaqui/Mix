package ru.motleycrew.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.motleycrew.model.Event;
import ru.motleycrew.database.EventDBSchema.EventTable;

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
        String messageId = getString(getColumnIndex(EventTable.Cols.MESSAGE_ID));
        String text = getString(getColumnIndex(EventTable.Cols.TEXT));

        Event event = new Event(uuidString);
        event.setTitle(title);
        event.setDate(new Date(date));
        event.setApproved(isApproved != 0);
//        event.setMessageId(messageId);
        event.setText(text);
        return event;
    }
}

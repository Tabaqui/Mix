package ru.motleycrew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.motleycrew.model.Credentials;
import ru.motleycrew.model.Event;
import ru.motleycrew.model.Participant;

import static ru.motleycrew.database.EventDBSchema.CredentialsTable;
import static ru.motleycrew.database.EventDBSchema.EventTable;
import static ru.motleycrew.database.EventDBSchema.JoinTable;
import static ru.motleycrew.database.EventDBSchema.ParticipantTable;

/**
 * Created by User on 17.01.2016.
 */
public class EventLab {

    private static EventLab sEventLab;

    private static final boolean JOIN = true;
    private static final boolean NOT_JOIN = false;

    //    private List<Event> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDataBase;


//    public static EventLab get(Context context) {
//        if (sEventLab == null) {
//            sEventLab = new EventLab(context);
//        }
//        return sEventLab;
//    }


    private static ContentValues getContentValues(Event event) {
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.UUID, event.getId().toString());
        values.put(EventTable.Cols.TITLE, event.getTitle());
        values.put(EventTable.Cols.DATE, event.getDate().getTime());
        values.put(EventTable.Cols.APPROVED, event.isApproved() ? 1 : 0);
        values.put(EventTable.Cols.TEXT, event.getText());
//        values.put(EventTable.Cols.MESSAGE_ID, event.getMessageId());
        return values;
    }

    private static ContentValues getContentValues(Participant user) {
        ContentValues values = new ContentValues();
        values.put(ParticipantTable.Cols.UUID, user.getId().toString());
        values.put(ParticipantTable.Cols.NAME, user.getName());
        values.put(ParticipantTable.Cols.CONTACT, user.getContact());
        return values;
    }

    private static ContentValues getContentValues(UUID userId, String eventId) {
        ContentValues values = new ContentValues();
        values.put(JoinTable.Cols.PARTICIPANT_UUID, userId.toString());
        values.put(JoinTable.Cols.UUID, eventId.toString());
        return values;
    }

    private static ContentValues getCredentialsValues(Credentials credentials) {
        ContentValues values = new ContentValues();
        values.put(CredentialsTable.Cols.NAME, credentials.getEmail());
        values.put(CredentialsTable.Cols.TOKEN, credentials.getToken());
        values.put(CredentialsTable.Cols.HASH, credentials.getHash());
        return values;
    }

    public EventLab(Context context) {
        mContext = context;
        mDataBase = new EventBaseHelper(mContext).getWritableDatabase();
//        mCrimes = new ArrayList<>();
    }

    private EventCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(
                EventTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new EventCursorWrapper(cursor);
    }

    public void addEvent(Event c) {
        ContentValues values = EventLab.getContentValues(c);
        mDataBase.insert(EventTable.NAME, null, values);
    }

    public void deleteEvent(Event event) {
        String uuidString = event.getId().toString();
        mDataBase.delete(EventTable.NAME,
                EventTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void updateEvent(Event event) {
        String uuidString = event.getId().toString();
        ContentValues values = EventLab.getContentValues(event);
        mDataBase.update(EventTable.NAME,
                values,
                EventTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public List<Event> getEvent() {
        List<Event> events = new ArrayList<>();
        EventCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                events.add(cursor.getEvent());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return events;
    }

    public Event getEvent(String id) {
        EventCursorWrapper cursor = queryCrimes(EventTable.Cols.UUID + " = ?",
                new String[]{id});
        try {
            if (cursor.getCount() == 0) {
                return null;
            } else {
                cursor.moveToFirst();
                return cursor.getEvent();
            }
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Event event) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, event.getPhotoFileName());
    }

    private ParticipantCursorWrapper queryParticipants(String whereClause, String[] whereArgs, boolean joinEvent) {
        String tableName = ParticipantTable.NAME;
        String whereClauseExt = "(" + whereClause + ")";
        if (joinEvent) {
            tableName += ", " + JoinTable.NAME;
            whereClauseExt += " AND " +
                    JoinTable.Cols.PARTICIPANT_UUID + " = " + ParticipantTable.Cols.UUID;
        }
        Cursor cursor = mDataBase.query(
                tableName,
                null,
                whereClauseExt,
                whereArgs,
                null,
                null,
                null
        );
        return new ParticipantCursorWrapper(cursor);
    }

    public List<Participant> getParticipants(String eventId) {
        List<Participant> participants = new ArrayList<>();
        ParticipantCursorWrapper cursor = queryParticipants(eventId == null ? null : JoinTable.Cols.UUID + " = ?",
                eventId == null ? null : new String[]{eventId},
                eventId == null ? NOT_JOIN : JOIN);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                participants.add(cursor.getParticipant());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return participants;
    }

    public void addUser(Participant user, String eventId) {
        ContentValues userValues = getContentValues(user);
        mDataBase.beginTransaction();
        try {
            mDataBase.insert(ParticipantTable.NAME, null, userValues);
            ContentValues joinValues = getContentValues(user.getId(), eventId);
            mDataBase.insert(JoinTable.NAME, null, joinValues);
            mDataBase.setTransactionSuccessful();
        } finally {
            mDataBase.endTransaction();
        }
    }

    public void deleteUser(Participant user) {
        String uuidString = user.getId().toString();
        mDataBase.beginTransaction();
        try {
            mDataBase.delete(ParticipantTable.NAME,
                    ParticipantTable.Cols.UUID + " = ?",
                    new String[]{uuidString});
            mDataBase.delete(JoinTable.NAME,
                    JoinTable.Cols.PARTICIPANT_UUID + " = ?",
                    new String[]{uuidString});
            mDataBase.setTransactionSuccessful();
        } finally {
            mDataBase.endTransaction();
        }
    }

    private CredentialsCursorWrapper queryCredentials(String whereClause, String[] whereArgs) {
        String tableName = CredentialsTable.NAME;
        Cursor cursor = mDataBase.query(
                tableName,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CredentialsCursorWrapper(cursor);
    }

    public Credentials getCredentials() {
        Credentials credentials = new Credentials();
        CredentialsCursorWrapper cursor = queryCredentials(null, null);
        try {
            if (cursor.moveToFirst()) {
                credentials = cursor.getCredentials();
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
        return credentials;
    }

    public void addCredentials(Credentials credentials) {
        ContentValues values = getCredentialsValues(credentials);
        mDataBase.insert(CredentialsTable.NAME, null, values);
    }
}

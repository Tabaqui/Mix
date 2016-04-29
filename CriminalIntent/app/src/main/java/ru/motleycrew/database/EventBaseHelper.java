package ru.motleycrew.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static ru.motleycrew.database.EventDBSchema.CredentialsTable;
import static ru.motleycrew.database.EventDBSchema.EventTable;
import static ru.motleycrew.database.EventDBSchema.JoinTable;
import static ru.motleycrew.database.EventDBSchema.ParticipantTable;

/**
 * Created by vnikolaev on 25.01.2016.
 */
public class EventBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    private static final String DATABASE_NAME = "crimeBase.db";

    public EventBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventTable.Cols.UUID + ", " +
                EventTable.Cols.TITLE + ", " +
                EventTable.Cols.DATE + ", " +
                EventTable.Cols.TEXT + ", " +
                EventTable.Cols.MESSAGE_ID + ", " +
                EventTable.Cols.APPROVED +
                ")"
        );

        db.execSQL("create table " + ParticipantTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ParticipantTable.Cols.UUID + ", " +
                ParticipantTable.Cols.NAME + ", " +
                ParticipantTable.Cols.CONTACT +
                ")"
        );

        db.execSQL("create table " + JoinTable.NAME + "(" +
                JoinTable.Cols.PARTICIPANT_UUID + ", " +
                JoinTable.Cols.UUID +
                ")"
        );

        db.execSQL("create table " + CredentialsTable.NAME + "(" +
                CredentialsTable.Cols.NAME + ", " +
                CredentialsTable.Cols.TOKEN + ", " +
                CredentialsTable.Cols.HASH +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

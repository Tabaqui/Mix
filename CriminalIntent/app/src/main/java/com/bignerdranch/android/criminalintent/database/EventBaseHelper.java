package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("create table " + EventDBSchema.EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventDBSchema.EventTable.Cols.UUID + ", " +
                EventDBSchema.EventTable.Cols.TITLE + ", " +
                EventDBSchema.EventTable.Cols.DATE + ", " +
                EventDBSchema.EventTable.Cols.APPROVED +
                ")"
        );

        db.execSQL("create table " + EventDBSchema.ParticipantTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventDBSchema.ParticipantTable.Cols.UUID + ", " +
                EventDBSchema.ParticipantTable.Cols.NAME + ", " +
                EventDBSchema.ParticipantTable.Cols.CONTACT +
                ")"
        );

        db.execSQL("create table " + EventDBSchema.JoinTable.NAME + "(" +
                EventDBSchema.JoinTable.Cols.PARTICIPANT_UUID + ", " +
                EventDBSchema.JoinTable.Cols.UUID +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

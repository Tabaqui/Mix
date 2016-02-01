package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.database.CrimeDBSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by vnikolaev on 25.01.2016.
 */
public class CrimeCursorWrapper extends CursorWrapper {


    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.TITLE));
        String suspect = null;
        if (!isNull(getColumnIndex(CrimeTable.Cols.SUSPECT))) {
            suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        }
        String contactId = null;
        if (!isNull(getColumnIndex(CrimeTable.Cols.CONTACT))) {
            contactId = getString(getColumnIndex(CrimeTable.Cols.CONTACT));
        }

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);
        crime.setContact(contactId);
        return crime;
    }
}

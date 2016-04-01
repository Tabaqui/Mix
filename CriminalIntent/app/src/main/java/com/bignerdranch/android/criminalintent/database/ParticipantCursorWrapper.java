package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent.model.Participant;
import com.bignerdranch.android.criminalintent.database.EventDBSchema.ParticipantTable;

import java.util.UUID;

/**
 * Created by User on 29.03.2016.
 */
public class ParticipantCursorWrapper extends CursorWrapper {

    public ParticipantCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Participant getParticipant() {
        String uuidString = getString(getColumnIndex(ParticipantTable.Cols.UUID));
        String name = getString(getColumnIndex(ParticipantTable.Cols.NAME));
        String contact = getString(getColumnIndex(ParticipantTable.Cols.CONTACT));
        Participant p = new Participant(UUID.fromString(uuidString));
        p.setName(name);
        p.setContact(contact);
        return p;
    }
}

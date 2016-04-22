package ru.motleycrew.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.motleycrew.model.Participant;
import ru.motleycrew.database.EventDBSchema.ParticipantTable;

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
        Participant p = new Participant();
        p.setId(UUID.fromString(uuidString));
        p.setName(name);
        p.setContact(contact);
        return p;
    }
}

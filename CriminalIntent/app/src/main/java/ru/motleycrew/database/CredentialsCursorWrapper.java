package ru.motleycrew.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.motleycrew.model.Credentials;

import static ru.motleycrew.database.EventDBSchema.CredentialsTable;

/**
 * Created by User on 28.04.2016.
 */
public class CredentialsCursorWrapper extends CursorWrapper {
    public CredentialsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Credentials getCredentials() {
        String name = getString(getColumnIndex(CredentialsTable.Cols.NAME));
        String hash = getString(getColumnIndex(CredentialsTable.Cols.HASH));
        String token = getString(getColumnIndex(CredentialsTable.Cols.TOKEN));

        Credentials credentials = new Credentials();
        credentials.setEmail(name);
        credentials.setToken(token);
        credentials.setHash(hash);
        return credentials;
    }
}

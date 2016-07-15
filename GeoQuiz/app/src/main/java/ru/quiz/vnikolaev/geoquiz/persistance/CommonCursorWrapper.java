package ru.quiz.vnikolaev.geoquiz.persistance;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by User on 14.07.2016.
 */
public abstract class CommonCursorWrapper<T> extends CursorWrapper {

    public CommonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public abstract T get();
}

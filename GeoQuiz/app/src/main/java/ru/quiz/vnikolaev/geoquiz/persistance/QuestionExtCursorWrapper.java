package ru.quiz.vnikolaev.geoquiz.persistance;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;

/**
 * Created by User on 30.06.2016.
 */
public class QuestionExtCursorWrapper extends CursorWrapper {

    public QuestionExtCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public QuestionExt get() {
        String uuid = getString(getColumnIndex(QuizDBSchema.QuestionTable.Cols.UUID));
        String value = getString(getColumnIndex(QuizDBSchema.QuestionTable.Cols.VALUE));
        QuestionExt question = new QuestionExt();
        question.setId(uuid);
        question.setValue(value);
        return question;
    }
}

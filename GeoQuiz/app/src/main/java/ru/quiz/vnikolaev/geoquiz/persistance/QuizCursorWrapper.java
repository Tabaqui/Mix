package ru.quiz.vnikolaev.geoquiz.persistance;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.quiz.vnikolaev.geoquiz.bis.Quiz;

/**
 * Created by User on 30.06.2016.
 */
public class QuizCursorWrapper extends CursorWrapper{

    public QuizCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Quiz get() {
        String uuid = getString(getColumnIndex(QuizDBSchema.QuizTable.Cols.UUID));
        String title = getString(getColumnIndex(QuizDBSchema.QuizTable.Cols.TITLE));
        int passed = getInt(getColumnIndex(QuizDBSchema.QuizTable.Cols.PASSED));
        Quiz quiz = new Quiz();
        quiz.setId(uuid);
        quiz.setHeader(title);
        quiz.setPassed(passed == 1 ? true : false);
        return quiz;
    }
}

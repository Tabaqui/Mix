package ru.quiz.vnikolaev.geoquiz.persistance;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.quiz.vnikolaev.geoquiz.bis.Answer;

/**
 * Created by User on 30.06.2016.
 */
public class AnswerCursorWrapper extends CommonCursorWrapper<Answer> {

    public AnswerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Answer get() {
        String uuid = getString(getColumnIndex(QuizDBSchema.AnswerTable.Cols.UUID));
        String value = getString(getColumnIndex(QuizDBSchema.AnswerTable.Cols.VALUE));
        Answer answer = new Answer();
        answer.setId(uuid);
        answer.setValue(value);
        return answer;
    }
}

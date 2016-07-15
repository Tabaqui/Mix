package ru.quiz.vnikolaev.geoquiz.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.quiz.vnikolaev.geoquiz.bis.Answer;
import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;
import ru.quiz.vnikolaev.geoquiz.bis.Quiz;

/**
 * Created by User on 30.06.2016.
 */
public class QuizDataSource {

    private static QuizDataSource sDataSource;

    private static final boolean JOIN = true;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static ContentValues getContentValues(Quiz quiz) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.QuizTable.Cols.UUID, quiz.getId());
        values.put(QuizDBSchema.QuizTable.Cols.TITLE, quiz.getHeader());
        values.put(QuizDBSchema.QuizTable.Cols.PASSED, quiz.isPassed());
        return values;
    }

    private static ContentValues getContentValues(QuestionExt question) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.QuestionTable.Cols.UUID, question.getId());
        values.put(QuizDBSchema.QuestionTable.Cols.VALUE, question.getValue());
        return values;
    }

    private static List<ContentValues> getContentValuesList(List<QuestionExt> questionExts) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (QuestionExt question : questionExts) {
            ContentValues values = getContentValues(question);
            contentValues.add(values);
        }
        return contentValues;
    }

    private static ContentValues getContentValues(Answer answer) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.AnswerTable.Cols.UUID, answer.getId());
        values.put(QuizDBSchema.AnswerTable.Cols.VALUE, answer.getValue());
        return values;
    }

    private QuizCursorWrapper queryQuizes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuizDBSchema.QuizTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new QuizCursorWrapper(cursor);
    }

    public void addQuiz(Quiz quiz) {
        ContentValues quizValues = QuizDataSource.getContentValues(quiz);
        List<ContentValues> questionValuesList = getContentValuesList(quiz.getQuestions());
        mDatabase.beginTransaction();
        try {
            mDatabase.insert(QuizDBSchema.QuizTable.NAME, null, quizValues);
            for (ContentValues questionValues : questionValuesList) {
                mDatabase.insert(QuizDBSchema.QuestionTable.NAME, null, questionValues);
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    public void deleteQuiz(Quiz quiz) {
        String id = quiz.getId();
        mDatabase.delete(QuizDBSchema.QuizTable.NAME,
                QuizDBSchema.QuizTable.Cols.UUID + " = ? ",
                new String[]{id});
    }

    private QuestionExtCursorWrapper queryQuestions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuizDBSchema.QuestionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new QuestionExtCursorWrapper(cursor);
    }

    public List<QuestionExt> getQuestions(String quizId) {
        List<QuestionExt> questions = new ArrayList<>();
        QuestionExtCursorWrapper cursor = queryQuestions(
                QuizDBSchema.QuestionTable.Cols.QUIZ_UUID + " = ? ",
                new String[]{quizId}
        );
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    questions.add(cursor.get());
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return questions;
    }

    private AnswerCursorWrapper queryAnswers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuizDBSchema.AnswerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new AnswerCursorWrapper(cursor);
    }

    public void addAnswer(Answer answer) {
        ContentValues values = QuizDataSource.getContentValues(answer);
        mDatabase.insert(QuizDBSchema.AnswerTable.NAME, null, values);
    }

    public <T> List<T> get(String joinId) {
        return null;
    }

    public List<Answer> getAnswers(String questionId) {
        get
        List<Answer> answers = new ArrayList<>();
        AnswerCursorWrapper cursor = queryAnswers(
                QuizDBSchema.AnswerTable.Cols.QUESTION_UUID + " = ? ",
                new String[]{questionId}
        );
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    answers.add(cursor.get());
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return answers;
    }

//    public Answer getAnswer(String id) {
//        AnswerCursorWrapper cursor = queryAnswers(
//                QuizDBSchema.AnswerTable.Cols.UUID + " = ? ",
//                new String[]{id}
//        );
//        try {
//            if (cursor.getCount() == 0) {
//                return null;
//            } else {
//                cursor.moveToFirst();
//                return cursor.get();
//            }
//        } finally {
//            cursor.close();
//        }
//    }
}

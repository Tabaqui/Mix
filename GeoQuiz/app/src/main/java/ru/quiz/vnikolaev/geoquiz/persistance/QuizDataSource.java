package ru.quiz.vnikolaev.geoquiz.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.quiz.vnikolaev.geoquiz.bis.Answer;
import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;
import ru.quiz.vnikolaev.geoquiz.bis.Quiz;
import ru.quiz.vnikolaev.geoquiz.bis.UserAnswer;

/**
 * Created by User on 30.06.2016.
 */
public class QuizDataSource {

    private static QuizDataSource sDataSource;

    private SQLiteDatabase mDatabase;

    public static QuizDataSource get(Context context) {
        if (sDataSource == null) {
            sDataSource = new QuizDataSource(context);
        }
        return sDataSource;
    }

    private QuizDataSource(Context context) {
        mDatabase = new QuizBaseHelper(context).getWritableDatabase();
    }

    private QuizCursorWrapper queryQuizzes(String whereClause, String[] whereArgs) {
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

    private UserAnswerWrapper queryUserAnswers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuizDBSchema.UserAnswerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new UserAnswerWrapper(cursor);
    }

    public void addQuiz(Quiz quiz) {
        ContentValues quizValues = DataSourceUtil.getContentValues(quiz);
        mDatabase.beginTransaction();
        try {
            long quizId = mDatabase.insert(QuizDBSchema.QuizTable.NAME, null, quizValues);
            Map<QuestionExt, ContentValues> questionsValues = DataSourceUtil
                    .getQuestionListContentValues(quiz.getQuestions(), quizId);
            for (Map.Entry<QuestionExt, ContentValues> questionValues : questionsValues.entrySet()) {
                long questionId = mDatabase.insert(QuizDBSchema.QuestionTable.NAME, null, questionValues.getValue());
                Map<Answer, ContentValues> answersValues = DataSourceUtil
                        .getAnswerListContentValues(questionValues.getKey().getAnswers(), questionId);
                for (Map.Entry<Answer, ContentValues> answerValues : answersValues.entrySet()) {
                    mDatabase.insert(QuizDBSchema.AnswerTable.NAME, null, answerValues.getValue());
                }
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    public void deleteQuiz(Quiz quiz) {
        long id = quiz.getId();
        mDatabase.delete(QuizDBSchema.QuizTable.NAME,
                QuizDBSchema.QuizTable.Cols.UUID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public List<Quiz> getQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        QuizCursorWrapper cursor = queryQuizzes(null, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Quiz quiz = cursor.get();
                    quizzes.add(quiz);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return quizzes;
    }

    public Quiz getQuiz(long id) {
        QuizCursorWrapper wrapper = queryQuizzes(
                QuizDBSchema.QuizTable.Cols.UUID + " = ? ",
                new String[]{String.valueOf(id)}
        );
        Quiz quiz = null;
        try {
            if (wrapper.getCount() > 0) {
                wrapper.moveToFirst();
                quiz = wrapper.get();
            }
        } finally {
            wrapper.close();
        }
        return quiz;
    }


    public List<QuestionExt> getQuestions(long quizId, int number, boolean shuffle) {
        List<QuestionExt> questions = getOrderedQuestions(quizId);
        if (!questions.isEmpty()) {
            return questions;
        }
        QuestionExtCursorWrapper cursor = queryQuestions(
                QuizDBSchema.QuestionTable.Cols.QUIZ_UUID + " = ? ",
                new String[]{String.valueOf(quizId)}
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
        if (shuffle) {
            Collections.shuffle(questions);
        }
        for (int i = 0; i < questions.size(); i++) {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setQuizId(quizId);
            userAnswer.setQuestionId(questions.get(i).getId());
            userAnswer.setNumber(i);
            questions.get(i).setUserAnswer(userAnswer);
            addUserAnswer(userAnswer);
        }
        return number == 0 ? questions : questions.subList(0, number);

    }

    public void addQuestion(QuestionExt question, long quizId) {
        ContentValues values = DataSourceUtil.getContentValues(question, quizId);
        mDatabase.insert(QuizDBSchema.QuestionTable.NAME, null, values);
    }

    public void addAnswer(Answer answer, long questionId) {
        ContentValues values = DataSourceUtil.getContentValues(answer, questionId);
        mDatabase.insert(QuizDBSchema.AnswerTable.NAME, null, values);
    }

    public List<Answer> getAnswers(long questionId) {
        List<Answer> answers = new ArrayList<>();
        AnswerCursorWrapper cursor = queryAnswers(
                QuizDBSchema.AnswerTable.Cols.QUESTION_UUID + " = ? ",
                new String[]{String.valueOf(questionId)}
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

    public UserAnswer getUserAnswer(long quizId, long questionId) {
        UserAnswerWrapper wrapper = queryUserAnswers(
                QuizDBSchema.UserAnswerTable.Cols.QUIZ_ID + " = ? " +
                        " and " +
                        QuizDBSchema.UserAnswerTable.Cols.QUESTION_ID + " = ? ",
                new String[]{String.valueOf(quizId), String.valueOf(questionId)}
        );
        UserAnswer userAnswer = null;
        try {
            if (wrapper.getCount() > 0) {
                wrapper.moveToFirst();
                userAnswer = wrapper.get();
            }
        } finally {
            wrapper.close();
        }
        return userAnswer;
    }

    public void addUserAnswer(UserAnswer userAnswer) {
        ContentValues values = DataSourceUtil.getContentValues(userAnswer);
        mDatabase.insert(QuizDBSchema.UserAnswerTable.NAME, null, values);
    }

    public void updateUserAnswer(UserAnswer userAnswer) {
        ContentValues values = DataSourceUtil.getForAnswerContentValues(userAnswer);
        mDatabase.update(QuizDBSchema.UserAnswerTable.NAME,
                values,
                QuizDBSchema.UserAnswerTable.Cols.QUIZ_ID + " = ? " +
                        "AND " +
                        QuizDBSchema.UserAnswerTable.Cols.QUESTION_ID + " = ? ",
                new String[]{String.valueOf(userAnswer.getQuizId()),
                        String.valueOf(userAnswer.getQuestionId())});
    }

    private List<QuestionExt> getOrderedQuestions(long quizId) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(QuizDBSchema.QuestionTable.NAME +
                " join " +
                QuizDBSchema.UserAnswerTable.NAME +
                " on " +
                QuizDBSchema.QuestionTable.Cols.UUID +
                " = " +
                QuizDBSchema.UserAnswerTable.Cols.QUESTION_ID);
        Cursor cursor = builder.query(mDatabase,
                null,
                QuizDBSchema.UserAnswerTable.Cols.QUIZ_ID + " = ?",
                new String[]{String.valueOf(quizId)},
                null,
                null,
                "number asc");
        QuestionExtCursorWrapper wrapper = new QuestionExtCursorWrapper(cursor);
        List<QuestionExt> userAnswers = new ArrayList<>();
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    userAnswers.add(wrapper.withUserAnswer().get());
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return userAnswers;
    }

}

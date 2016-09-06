package ru.quiz.vnikolaev.geoquiz.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 19.07.2016.
 */
public class QuizBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    private static final String DATABASE_NAME = "quizBase.db";

    public QuizBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + QuizDBSchema.QuizTable.NAME + "(" +
                QuizDBSchema.QuizTable.Cols.UUID + " integer primary key autoincrement, " +
                QuizDBSchema.QuizTable.Cols.TITLE + ", " +
                QuizDBSchema.QuizTable.Cols.PASSED + " int)");

        sqLiteDatabase.execSQL("create table " + QuizDBSchema.QuestionTable.NAME + "(" +
                QuizDBSchema.QuestionTable.Cols.UUID + " integer primary key autoincrement, " +
                QuizDBSchema.QuestionTable.Cols.VALUE + ", " +
                QuizDBSchema.QuestionTable.Cols.QUIZ_UUID + " int)");

        sqLiteDatabase.execSQL("create table " + QuizDBSchema.AnswerTable.NAME + "(" +
                QuizDBSchema.AnswerTable.Cols.UUID + " integer primary key autoincrement, " +
                QuizDBSchema.AnswerTable.Cols.VALUE + ", " +
                QuizDBSchema.AnswerTable.Cols.QUESTION_UUID + " int)");

        sqLiteDatabase.execSQL("create table " + QuizDBSchema.UserAnswerTable.NAME + "(" +
                QuizDBSchema.UserAnswerTable.Cols.QUIZ_ID + " int," +
                QuizDBSchema.UserAnswerTable.Cols.QUESTION_ID + " int, " +
                QuizDBSchema.UserAnswerTable.Cols.ANSWER_ID + " int, " +
                QuizDBSchema.UserAnswerTable.Cols.NUMBER + " int, " +
                "primary key (quiz_id, question_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

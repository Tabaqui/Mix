package ru.quiz.vnikolaev.geoquiz.persistance;

import android.content.ContentValues;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.quiz.vnikolaev.geoquiz.bis.Answer;
import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;
import ru.quiz.vnikolaev.geoquiz.bis.Quiz;
import ru.quiz.vnikolaev.geoquiz.bis.UserAnswer;

/**
 * Created by User on 27.08.2016.
 */
public class DataSourceUtil {

    public static ContentValues getContentValues(Quiz quiz) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.QuizTable.Cols.TITLE, quiz.getHeader());
        values.put(QuizDBSchema.QuizTable.Cols.PASSED, quiz.isPassed());
        return values;
    }

    public static ContentValues getContentValues(QuestionExt question, long quizId) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.QuestionTable.Cols.VALUE, question.getValue());
        values.put(QuizDBSchema.QuestionTable.Cols.QUIZ_UUID, quizId);
        return values;
    }

    public static Map<QuestionExt, ContentValues> getQuestionListContentValues(Collection<QuestionExt> questionExts, long quizId) {
        Map<QuestionExt, ContentValues> contentValues = new HashMap<>();
        for (QuestionExt question : questionExts) {
            ContentValues values = getContentValues(question, quizId);
            contentValues.put(question, values);
        }
        return contentValues;
    }

    public static Map<Answer, ContentValues> getAnswerListContentValues(Collection<Answer> answers, long questionId) {
        Map<Answer, ContentValues> contentValues = new HashMap<>();
        for (Answer answer : answers) {
            ContentValues values = getContentValues(answer, questionId);
            contentValues.put(answer, values);
        }
        return contentValues;
    }

    public static ContentValues getContentValues(UserAnswer userAnswer) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.UserAnswerTable.Cols.QUIZ_ID, userAnswer.getQuizId());
        values.put(QuizDBSchema.UserAnswerTable.Cols.QUESTION_ID, userAnswer.getQuestionId());
        values.put(QuizDBSchema.UserAnswerTable.Cols.ANSWER_ID, userAnswer.getAnswerId());
        values.put(QuizDBSchema.UserAnswerTable.Cols.NUMBER, userAnswer.getNumber());
        return values;
    }

    public static ContentValues getForAnswerContentValues(UserAnswer userAnswer) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.UserAnswerTable.Cols.ANSWER_ID, userAnswer.getAnswerId());
        return values;
    }

    public static ContentValues getContentValues(Answer answer, long questionId) {
        ContentValues values = new ContentValues();
        values.put(QuizDBSchema.AnswerTable.Cols.VALUE, answer.getValue());
        values.put(QuizDBSchema.AnswerTable.Cols.QUESTION_UUID, questionId);
        return values;
    }
}

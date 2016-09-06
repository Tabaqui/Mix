package ru.quiz.vnikolaev.geoquiz.persistance;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.quiz.vnikolaev.geoquiz.bis.UserAnswer;

/**
 * Created by User on 27.08.2016.
 */
public class UserAnswerWrapper extends CursorWrapper{

    public UserAnswerWrapper(Cursor cursor) {
        super(cursor);
    }

    public UserAnswer get() {
        long quizId = getLong(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.QUIZ_ID));
        long questionId = getLong(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.QUESTION_ID));
        Long answerId = null;
        if (!isNull(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.ANSWER_ID))) {
            answerId = getLong(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.ANSWER_ID));
        }
        long order = getLong(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.NUMBER));
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuizId(quizId);
        userAnswer.setQuestionId(questionId);
        userAnswer.setAnswerId(answerId);
        userAnswer.setNumber(order);
        return userAnswer;
    }


}

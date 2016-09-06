package ru.quiz.vnikolaev.geoquiz.persistance;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;
import ru.quiz.vnikolaev.geoquiz.bis.UserAnswer;

/**
 * Created by User on 30.06.2016.
 */
public class QuestionExtCursorWrapper extends CursorWrapper {

    private boolean userAnswer;

    public QuestionExtCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public QuestionExt get() {
        long uuid = getLong(getColumnIndex(QuizDBSchema.QuestionTable.Cols.UUID));
        String value = getString(getColumnIndex(QuizDBSchema.QuestionTable.Cols.VALUE));
        QuestionExt question = new QuestionExt();
        question.setId(uuid);
        question.setValue(value);
        if (userAnswer) {
            long quizId = getLong(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.QUIZ_ID));
            long questionId = getLong(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.QUESTION_ID));
            Long answerId = null;
            if (!isNull(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.ANSWER_ID))) {
                answerId = getLong(getColumnIndex(QuizDBSchema.UserAnswerTable.Cols.ANSWER_ID));
            }
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setQuizId(quizId);
            userAnswer.setQuestionId(questionId);
            userAnswer.setAnswerId(answerId);
            question.setUserAnswer(userAnswer);
        }
        return question;
    }

    public QuestionExtCursorWrapper withUserAnswer() {
        userAnswer = true;
        return this;
    }
}

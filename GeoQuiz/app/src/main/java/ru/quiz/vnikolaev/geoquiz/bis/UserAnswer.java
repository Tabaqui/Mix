package ru.quiz.vnikolaev.geoquiz.bis;

/**
 * Created by User on 18.07.2016.
 */
public class UserAnswer {

    private long mQuizId;
    private long mQuestionId;
    private Long mAnswerId;
    private long mNumber;

    public long getQuizId() {
        return mQuizId;
    }

    public void setQuizId(long quizId) {
        mQuizId = quizId;
    }

    public long getQuestionId() {
        return mQuestionId;
    }

    public void setQuestionId(long questionId) {
        mQuestionId = questionId;
    }

    public Long getAnswerId() {
        return mAnswerId;
    }

    public void setAnswerId(Long answerId) {
        mAnswerId = answerId;
    }

    public long getNumber() {
        return mNumber;
    }

    public void setNumber(long number) {
        mNumber = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAnswer that = (UserAnswer) o;

        if (mQuizId != that.mQuizId) return false;
        if (mQuestionId != that.mQuestionId) return false;
        return mAnswerId == that.mAnswerId;

    }

    @Override
    public int hashCode() {
        int result = (int) (mQuizId ^ (mQuizId >>> 32));
        result = 31 * result + (int) (mQuestionId ^ (mQuestionId >>> 32));
        result = 31 * result + (int) (mAnswerId ^ (mAnswerId >>> 32));
        return result;
    }
}

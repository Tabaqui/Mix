package ru.quiz.vnikolaev.geoquiz.bis;

import java.util.List;

/**
 * Created by User on 22.06.2016.
 */
public class Quiz {

    private String mId;
    private String mHeader;
    private List<QuestionExt> mQuestions;
    private boolean mPassed;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String header) {
        mHeader = header;
    }

    public List<QuestionExt> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(List<QuestionExt> questions) {
        mQuestions = questions;
    }

    public boolean isPassed() {
        return mPassed;
    }

    public void setPassed(boolean passed) {
        mPassed = passed;
    }
}

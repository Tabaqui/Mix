package ru.quiz.vnikolaev.geoquiz.bis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 22.06.2016.
 */
public class Quiz {

    private long mId;
    private String mHeader;
    private List<QuestionExt> mQuestions;
    private boolean mPassed;
    private long time;
    private int questionsNumber;
    private String url;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getHeader() {
        return mHeader;
    }

    public void setHeader(String header) {
        mHeader = header;
    }

    public List<QuestionExt> getQuestions() {
        if (mQuestions == null) {
            mQuestions = new ArrayList<>();
        }
        return mQuestions;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getQuestionsNumber() {
        return questionsNumber;
    }

    public void setQuestionsNumber(int questionsNumber) {
        this.questionsNumber = questionsNumber;
    }

    public void setQuestions(List<QuestionExt> questions) {
        mQuestions = questions;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPassed() {
        return mPassed;
    }

    public void setPassed(boolean passed) {
        mPassed = passed;
    }
}

package ru.quiz.vnikolaev.geoquiz.bis;

import java.util.List;

/**
 * Created by User on 22.06.2016.
 */
public class Quiz {

    private String mHeader;
    private List<QuestionExt> mQuestions;

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
}

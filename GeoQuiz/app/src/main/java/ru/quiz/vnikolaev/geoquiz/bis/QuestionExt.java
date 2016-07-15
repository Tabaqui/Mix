package ru.quiz.vnikolaev.geoquiz.bis;

import java.util.List;

/**
 * Created by User on 19.06.2016.
 */
public class QuestionExt {

    private String mId;
    private String mValue;
    private List<String> mAnswers;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public List<String> getAnswers() {
        return mAnswers;
    }

    public void setAnswers(List<String> answers) {
        this.mAnswers = answers;
    }
}

package ru.quiz.vnikolaev.geoquiz.bis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 19.06.2016.
 */
public class QuestionExt {

    private long mId;
    private String mValue;
    private List<Answer> mAnswers;
    private UserAnswer mUserAnswer;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public List<Answer> getAnswers() {
        if (mAnswers == null) {
            mAnswers = new ArrayList<>();
        }
        return mAnswers;
    }

    public void setAnswers(List<Answer> answers) {
        this.mAnswers = answers;
    }

    public UserAnswer getUserAnswer() {
        return mUserAnswer;
    }

    public void setUserAnswer(UserAnswer userAnswer) {
        mUserAnswer = userAnswer;
    }
}

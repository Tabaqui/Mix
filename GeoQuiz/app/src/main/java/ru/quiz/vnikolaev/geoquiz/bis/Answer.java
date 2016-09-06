package ru.quiz.vnikolaev.geoquiz.bis;

/**
 * Created by User on 30.06.2016.
 */
public class Answer {

    private long mId;
    private String value;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

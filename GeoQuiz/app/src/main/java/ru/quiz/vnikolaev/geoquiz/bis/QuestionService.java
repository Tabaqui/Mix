package ru.quiz.vnikolaev.geoquiz.bis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 19.06.2016.
 */
public class QuestionService {

    private static final List<QuestionExt> PREPARED_QUESTIONS;

    static {
        PREPARED_QUESTIONS = new ArrayList<>();
        QuestionExt qe = new QuestionExt();
        qe.setValue("Where is Atlantida?");
        List<String> anss = new ArrayList<>();
        anss.add("Atlantic ocean");
        anss.add("Greenland");
        anss.add("Estonia");
        anss.add("Valinor");
        qe.setAnswers(anss);

        PREPARED_QUESTIONS.add(qe);

        qe = new QuestionExt();
        qe.setValue("What kind of color orange is?");
        anss = new ArrayList<>();
        anss.add("Invisible");
        anss.add("Transparent");
        anss.add("Orange");
//        anss.add("Hello");
        qe.setAnswers(anss);

        PREPARED_QUESTIONS.add(qe);
    }

    public static List<QuestionExt> get() {
        return new ArrayList<>(PREPARED_QUESTIONS);
    }
}

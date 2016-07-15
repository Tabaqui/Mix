package ru.quiz.vnikolaev.geoquiz.persistance;

/**
 * Created by User on 28.06.2016.
 */
public class QuizDBSchema {

    public static final class QuizTable {
        public static final String NAME = "quiz";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String PASSED = "passed";
        }
    }

    public static final class QuestionTable {
        public static final String NAME = "question";

        public static final class Cols {
            public static final String UUID = "id";
            public static final String VALUE = "value";
            public static final String QUIZ_UUID = "quiz_uuid";
        }
    }

    public static final class AnswerTable {
        public static final String NAME = "answer";

        public static final class Cols {
            public static final String UUID = "id";
            public static final String VALUE = "value";
            public static final String QUESTION_UUID = "question_id";
        }
    }

}

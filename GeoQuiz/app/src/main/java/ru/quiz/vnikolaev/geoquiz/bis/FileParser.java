package ru.quiz.vnikolaev.geoquiz.bis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.quiz.vnikolaev.geoquiz.Question;

/**
 * Created by User on 20.06.2016.
 */
public class FileParser {

    private static final String QUESTION_MARK = "?";

    private final String fileName;

    public FileParser(String fileName) {
        this.fileName = fileName;
    }

    public List<QuestionExt> extractQuestions() throws IOException {
        List<QuestionExt> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        QuestionExt question = null;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            line = line.trim();
            if (line.length() > 50) {
                break;
            }
            if (line.endsWith(QUESTION_MARK)) {
                if (question != null && question.getAnswers().size() > 1 && question.getAnswers().size() < 5) {
                    result.add(question);
                }
                question = new QuestionExt();
                question.setValue(line);
            } else {
                question.getAnswers().add(line);
            }
            if (result.size() >= 45) {
                break;
            }
        }
        reader.close();
        return result;
    }
}

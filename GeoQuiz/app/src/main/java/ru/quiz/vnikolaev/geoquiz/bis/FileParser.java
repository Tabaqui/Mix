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

    public Quiz extractQuestions() throws IOException {
        Quiz quiz = new Quiz();
        List<QuestionExt> questions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        QuestionExt question = null;
        try {
            String header = reader.readLine();
            quiz.setHeader(header);
        } catch (IOException ex) {
            throw new IOException("Error read header from file " + fileName,  ex);
        }
        try {
            String number = reader.readLine();
            int qNumber = Integer.valueOf(number);
        } catch (Exception ex) {
            throw new IOException("Error read questions number from file " + fileName, ex);
        }

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            line = line.trim();
            if (line.length() > 50) {
                break;
            }
            if (line.endsWith(QUESTION_MARK)) {
                if (question != null && question.getAnswers().size() > 1 && question.getAnswers().size() < 5) {
                    questions.add(question);
                }
                question = new QuestionExt();
                question.setValue(line);
            } else {
                question.getAnswers().add(line);
            }
            if (questions.size() >= 45) {
                break;
            }
        }
        reader.close();
        quiz.setQuestions(questions);
        return quiz;
    }
}

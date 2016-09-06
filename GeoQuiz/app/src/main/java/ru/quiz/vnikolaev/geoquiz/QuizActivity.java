package ru.quiz.vnikolaev.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.quiz.vnikolaev.geoquiz.bis.Answer;
import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;
import ru.quiz.vnikolaev.geoquiz.bis.Quiz;
import ru.quiz.vnikolaev.geoquiz.bis.UserAnswer;
import ru.quiz.vnikolaev.geoquiz.network.QuizService;
import ru.quiz.vnikolaev.geoquiz.persistance.QuizDataSource;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String QUIZ_INDEX = "quizIndex";

    private Button mLTButton;
    private Button mLBButton;
    private Button mRTButton;
    private Button mRBButton;

    private ImageButton mNextButton;
    private ImageButton mPrevButton;


    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;

    private Quiz mQuiz;
    private QuizDataSource mDS;
    private UserAnswer mUserAnswer;

    public class AnswerOnClickListener implements View.OnClickListener {

        private final long mAnswerId;
        private final UserAnswer mUserAnswer;
        private final Refresher mButtonRefresher;

        public AnswerOnClickListener(long answerId, UserAnswer userAnswer, Refresher buttonRefresher) {
            mAnswerId = answerId;
            mUserAnswer = userAnswer;
            mButtonRefresher = buttonRefresher;
        }

        @Override
        public void onClick(View v) {
            mUserAnswer.setAnswerId(mAnswerId);
            mButtonRefresher.clear();
            ((Button) v).setTextColor(Color.GREEN);
        }
    }

    public interface Refresher {
        void clear();
    }

    private static class ButtonRefresher implements Refresher {
        List<Button> mButtons ;
        ButtonRefresher(List<Button> buttons) {
            mButtons = buttons;
        }

        @Override
        public void clear() {
            for (Button button : mButtons) {
                button.setTextColor(Color.WHITE);
            }
        }
    }

    public static Intent getIntent(Context packageContext, long quizId) {
        Intent intent = new Intent(packageContext, QuizActivity.class);
        intent.putExtra(QUIZ_INDEX, quizId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        long id = getIntent().getLongExtra(QUIZ_INDEX, 0L);
        mDS = QuizDataSource.get(getApplicationContext());
        mQuiz = mDS.getQuiz(id);
        int counter = mQuiz.getQuestionsNumber();
        List<QuestionExt> questions = new ArrayList<>();
        if (mQuiz.getUrl() != null) {
            QuizService service = QuizService.getInstance();
            try {
                questions = service.downloadQuiestions(mQuiz.getId(), counter);
            } catch (IOException ex) {
                Toast.makeText(this, "Error connecting with server.", Toast.LENGTH_LONG);
            }
        } else {
            questions = mDS.getQuestions(mQuiz.getId(), counter, true);
        }
        mQuiz.setQuestions(questions);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mLTButton = (Button) findViewById(R.id.lt_button);
        mLBButton = (Button) findViewById(R.id.lb_button);
        mRBButton = (Button) findViewById(R.id.rb_button);
        mRTButton = (Button) findViewById(R.id.rt_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        if (mCurrentIndex == 0) {
            for (QuestionExt question : questions) {
                if (question.getUserAnswer().getAnswerId() == null) {
                    break;
                }
                mCurrentIndex++;
            }
            // TODO: 04.09.2016 This is stub if all questions answered 
            if (mCurrentIndex >= questions.size()) {
                mCurrentIndex = 0;
            }
        }
        updateQuestion();
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswer();
                mCurrentIndex = (mQuiz.getQuestions().size() + mCurrentIndex - 1) % mQuiz.getQuestions().size();
                updateQuestion();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAnswer();
                mCurrentIndex = (mCurrentIndex + 1) % mQuiz.getQuestions().size();
                updateQuestion();
            }
        });

    }


    private void updateQuestion() {
        QuestionExt question = mQuiz.getQuestions().get(mCurrentIndex);
        mQuestionTextView.setText(question.getValue());
        List<Answer> answers = mDS.getAnswers(question.getId());
        question.setAnswers(answers);
        mUserAnswer = mDS.getUserAnswer(mQuiz.getId(), question.getId());
        updateUI(question);
    }

    private void updateUI(QuestionExt question) {
        List<Button> buttons = new ArrayList<>();
        buttons.add(mLTButton);
        buttons.add(mLBButton);
        buttons.add(mRTButton);
        buttons.add(mRBButton);
        Refresher refresher = new ButtonRefresher(buttons);
        for (Button button : buttons) {
            button.setTextColor(Color.WHITE);
            button.setVisibility(View.GONE);
        }
        buttons = buttons.subList(0, question.getAnswers().size());
        List<Answer> answers = question.getAnswers();
        Long quaaId = mUserAnswer.getAnswerId();
        for (int i = 0; i < answers.size(); i++) {
            buttons.get(i).setText(answers.get(i).getValue());
            buttons.get(i).setVisibility(View.VISIBLE);
            buttons.get(i).setOnClickListener(
                    new AnswerOnClickListener(answers.get(i).getId(), mUserAnswer, refresher));
            Long aiId = answers.get(i).getId();
            if (quaaId != null && quaaId.longValue() == aiId) {
                buttons.get(i).setTextColor(Color.GREEN);
            } else {
                buttons.get(i).setTextColor(buttons.get(i).getTextColors().getDefaultColor());

            }
        }
    }

    private void saveAnswer() {
        QuizDataSource ds = QuizDataSource.get(getApplicationContext());
        ds.updateUserAnswer(mUserAnswer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
    }

}

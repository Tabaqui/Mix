package ru.quiz.vnikolaev.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import ru.quiz.vnikolaev.geoquiz.bis.FileChooser;
import ru.quiz.vnikolaev.geoquiz.bis.FileParser;
import ru.quiz.vnikolaev.geoquiz.bis.QuestionExt;
import ru.quiz.vnikolaev.geoquiz.bis.QuestionService;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEAT = "cheat";
    private static final int REQUEST_CODE_CHEAT = 0;


    private Button mLTButton;
    private Button mLBButton;
    private Button mRTButton;
    private Button mRBButton;

    private ImageButton mNextButton;
    private ImageButton mPrevButton;

    private Toolbar toolbar;

    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;

    private List<QuestionExt> mQuestions;
    private String[] mResults;

    private View.OnClickListener answerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String answer = ((Button) view).getText().toString();
            saveAnswer(answer);
            mCurrentIndex = (mCurrentIndex + 1) % mQuestions.size();
            updateQuestion();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        mQuestions = QuestionService.get();
        mResults = new String[mQuestions.size()];
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
        updateQuestion();
        mLTButton.setOnClickListener(answerListener);
        mLBButton.setOnClickListener(answerListener);
        mRTButton.setOnClickListener(answerListener);
        mRBButton.setOnClickListener(answerListener);

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestions.size() - 1) % mQuestions.size();
                updateQuestion();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.size();
                updateQuestion();
            }
        });

//        toolbar = (Toolbar) findViewById(R.id.load_questions);
//        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem item = menu.findItem(R.id.load_questions);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.load_questions:
                FileChooser filechooser = new FileChooser(QuizActivity.this);
                filechooser.setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(final File file) {
                        // ....do something with the file
                        String filename = file.getAbsolutePath();
                        Log.d("File", filename);

                        FileParser fp = new FileParser(filename);
                        try {
                            mQuestions = fp.extractQuestions();
                        } catch (IOException ex) {
                            Toast.makeText(QuizActivity.this, "Error extracting questions from file " + filename, Toast.LENGTH_LONG);
                            ex.printStackTrace();
                        }
                        // then actually do something in another module

                    }
                });
//                filechooser.setExtension("pdf");
                filechooser.showDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateQuestion() {
        QuestionExt question = mQuestions.get(mCurrentIndex);
        mQuestionTextView.setText(question.getValue());
        mLTButton.setText(question.getAnswers().get(0));
        mLBButton.setText(question.getAnswers().get(1));
        if (question.getAnswers().size() < 4) {
            mRBButton.setVisibility(View.GONE);
            if (question.getAnswers().size() < 3) {
                mRTButton.setVisibility(View.GONE);
            } else {
                mRTButton.setVisibility(View.VISIBLE);
                mRTButton.setText(question.getAnswers().get(2));
            }
        } else {
            mRBButton.setVisibility(View.VISIBLE);
            mRTButton.setVisibility(View.VISIBLE);
            mRTButton.setText(question.getAnswers().get(2));
            mRBButton.setText(question.getAnswers().get(3));
        }

    }

    private void saveAnswer(String text) {
        mResults[mCurrentIndex] = text;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

}

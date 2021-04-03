package ru.quiz.vnikolaev.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "ru.quiz.vnikolaev.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "ru.quiz.vnikolaev.geoquiz.answer_shown";
    private static final String KEY_ANSWER_SHOWN = "answer_shown";
    private static final String KEY_ANSWER_IS_TRUE = "answer_true";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private TextView mApiTextView;
    private boolean mAnswerShown;

    public static Intent newIntent(Context packageContext, boolean isAnswerTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mApiTextView = (TextView) findViewById(R.id.api_version_text_view);
        mApiTextView.setText("API version " + Build.VERSION.SDK_INT);
        if (savedInstanceState != null) {
            mAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            if (mAnswerShown) {
                showCheat();
            }
        }

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheat();
            }
        });
    }

    private void showCheat() {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
        mAnswerShown = true;
        setAnswerShownResult(true);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mAnswerShown);
    }

    private void atomTest() {
        System.out.println("atomTest")
        System.out.println("atomFix")
    }


}

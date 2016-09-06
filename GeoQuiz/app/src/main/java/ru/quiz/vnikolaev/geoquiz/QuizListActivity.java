package ru.quiz.vnikolaev.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.quiz.vnikolaev.geoquiz.bis.FileChooser;
import ru.quiz.vnikolaev.geoquiz.bis.FileParser;
import ru.quiz.vnikolaev.geoquiz.bis.Quiz;
import ru.quiz.vnikolaev.geoquiz.network.QuizService;
import ru.quiz.vnikolaev.geoquiz.persistance.QuizDataSource;

/**
 * Created by User on 23.06.2016.
 */
public class QuizListActivity extends AppCompatActivity implements UrlDialog.DialogListener {

    private RecyclerView mRecyclerView;
    private QuizDataSource mDataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataSource = QuizDataSource.get(getApplicationContext());
        setContentView(R.layout.activity_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new QuizAdapter(mDataSource.getQuizzes()));
    }

    private class QuizHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Quiz mQuiz;
        private TextView mFirstLine;

        @Override
        public void onClick(View view) {
            Intent i = QuizActivity.getIntent(QuizListActivity.this, mQuiz.getId());
            startActivity(i);
        }

        public QuizHolder(View itemView) {
            super(itemView);
            mFirstLine = (TextView) itemView.findViewById(R.id.firstLine);
            itemView.setOnClickListener(this);
        }

        public void bindQuiz(Quiz quiz) {
            mQuiz = quiz;
            mFirstLine.setText(mQuiz.getHeader());
        }
    }

    private class QuizAdapter extends RecyclerView.Adapter<QuizHolder> {
        private List<Quiz> mQuizList;

        public QuizAdapter(List<Quiz> quizzes) {
            mQuizList = quizzes;
        }

        @Override
        public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(QuizListActivity.this);
            View view = inflater.inflate(R.layout.list_item_quiz, parent, false);
            return new QuizHolder(view);
        }

        @Override
        public void onBindViewHolder(QuizHolder holder, int position) {
            Quiz quiz = mQuizList.get(position);
            holder.bindQuiz(quiz);
        }

        @Override
        public int getItemCount() {
            return mQuizList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.card_questions:
                FileChooser filechooser = new FileChooser(QuizListActivity.this);
                filechooser.setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(final File file) {
                        // ....do something with the file
                        String filename = file.getAbsolutePath();
                        Log.d("File", filename);

                        FileParser fp = new FileParser(filename);
                        try {
                            Quiz quiz = fp.extractQuestions();
                            mDataSource.addQuiz(quiz);
                            updateUI();
                        } catch (IOException ex) {
                            if (ex.getMessage() == null) {
                                Toast.makeText(QuizListActivity.this, "Error extracting questions from file " + filename + ".", Toast.LENGTH_LONG);
                            } else {
                                Toast.makeText(QuizListActivity.this, ex.getMessage(), Toast.LENGTH_LONG);
                            }
                            ex.printStackTrace();
                        }

                    }
                });
                filechooser.showDialog();
                return true;
            case R.id.url_questions:
                FragmentManager manager = getSupportFragmentManager();
                UrlDialog dialog = UrlDialog.newInstance(this);
                dialog.show(manager, "DIALOG_TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        mRecyclerView.setAdapter(new QuizAdapter(mDataSource.getQuizzes()));
    }

    @Override
    public void onDialogResult(int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        final String url = intent.getStringExtra(UrlDialog.EXTRA_URL);
        Toast.makeText(QuizListActivity.this, url, Toast.LENGTH_LONG);

        new AsyncTask<Void, Void, List<Quiz>>() {
            @Override
            protected List<Quiz> doInBackground(Void... params) {
                QuizService quizService = QuizService.getInstance();
                quizService.setUrl(url);
                List<Quiz> result = null;
                try {
                    result = quizService.downloadQuizzes();
                } catch (IOException ex) {
                    //TODO: Use some other approach to get data from network than async task
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Quiz> quizzes) {
                List<Quiz> newQuizzes = new ArrayList<>(quizzes);
                QuizAdapter adapter = (QuizAdapter) mRecyclerView.getAdapter();
                newQuizzes.addAll(adapter.mQuizList);
                adapter.mQuizList = newQuizzes;
                adapter.notifyDataSetChanged();
                QuizDataSource ds = QuizDataSource.get(getApplicationContext());
                for (Quiz quiz : quizzes) {
                    ds.addQuiz(quiz);
                }
            }
        };
    }

}

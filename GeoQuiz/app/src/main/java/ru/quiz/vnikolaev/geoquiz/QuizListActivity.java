package ru.quiz.vnikolaev.geoquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.quiz.vnikolaev.geoquiz.bis.Quiz;

/**
 * Created by User on 23.06.2016.
 */
public class QuizListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new QuizAdapter(new ArrayList<Quiz>()));
    }

    private class QuizHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Quiz mQuiz;
        private TextView mFirstLine;

        @Override
        public void onClick(View view) {

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

        public QuizAdapter(List<Quiz> quizs) {
            mQuizList = quizs;
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
}

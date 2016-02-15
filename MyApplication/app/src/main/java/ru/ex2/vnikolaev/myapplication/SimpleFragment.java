package ru.ex2.vnikolaev.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.ex2.vnikolaev.myapplication.model.Student;

/**
 * Created by vnikolaev on 05.02.2016.
 */
public class SimpleFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_student_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    private class StudentHolder extends RecyclerView.ViewHolder {

        private Student mStudent;
        private TextView mCredentials;
        private TextView mWorkingStatus;

        public StudentHolder(View itemView) {
            super(itemView);
            mCredentials = (TextView) itemView.findViewById(R.id.student_cred);
            mWorkingStatus = (TextView) itemView.findViewById(R.id.student_working_status);
        }

        public void bindStudent(Student student) {
            this.mStudent = student;
            mCredentials.setText(mStudent.getName() + " " + mStudent.getLastName());
            mWorkingStatus.setText(mStudent.getWorkingStatus());
        }
    }
}

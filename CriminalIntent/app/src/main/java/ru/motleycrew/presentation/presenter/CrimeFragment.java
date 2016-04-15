package ru.motleycrew.presentation.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;



import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.motleycrew.R;
import ru.motleycrew.database.EventLab;
import ru.motleycrew.model.Event;
import ru.motleycrew.model.Participant;
import ru.motleycrew.presentation.service.MessageFetcher;
import ru.motleycrew.repo.Fetcher;
import ru.motleycrew.repo.FetcherImpl;
import ru.motleycrew.utis.DateUtil;

/**
 * Created by User on 16.01.2016.
 */
public class CrimeFragment extends Fragment {

    public static final String TAG = "CrimeFragment";

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE_TIME = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Event mEvent;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mDetailsField;
    private Button mChooseUsers;
    private CheckBox mSolvedCheckBox;
    private Callbacks mCallbacks;
    private Button mReceivedButton;
    private Button mSendButton;


    public static String time(Date date) {
        return DateFormat.format("kk:mm", date).toString();
    }

    public static CrimeFragment newInstance(String crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface Callbacks {
        void onCrimeUpdated(Event event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String crimeId = (String) this.getArguments().get(ARG_CRIME_ID);
        mEvent = EventLab.get(getActivity()).getEvent(crimeId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEvent.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleField.setText(mEvent.getTitle());
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mEvent.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE_TIME);
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mEvent.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_DATE_TIME);
            }
        });
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mEvent.isApproved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEvent.setApproved(isChecked);
                updateCrime();
            }
        });
        mTitleField = (EditText) v.findViewById(R.id.event_details_text);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEvent.setText(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mChooseUsers = (Button) v.findViewById(R.id.bt_choose_users);
        mChooseUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventLab.get(getActivity()).updateEvent(mEvent);
                Intent i = UserListActivity.newIntent(getActivity(), mEvent.getId());
                startActivity(i);
            }
        });

        mReceivedButton = (Button) v.findViewById(R.id.bt_service);
        mReceivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = MessageFetcher.newInstance(getContext(), "test_message_id_1");
                getActivity().startService(i);
            }
        });
        mSendButton = (Button) v.findViewById(R.id.bt_send_message);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Participant> eventParticipants = EventLab.get(getActivity()).getParticipants(mEvent.getId());
                for (Participant participant : eventParticipants) {
                    mEvent.addParticipant(participant);
                }
                Fetcher fetcher = new FetcherImpl(getContext());
                fetcher.pushMessage(mEvent);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void updateDate() {
        mDateButton.setText(DateUtil.longDate(mEvent.getDate()));
        mTimeButton.setText(CrimeFragment.time(mEvent.getDate()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_delete_crime) {
            EventLab.get(getActivity()).deleteEvent(mEvent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventLab.get(getActivity()).updateEvent(mEvent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTime(date);
            Calendar oldCalendar = Calendar.getInstance();
            oldCalendar.setTime(mEvent.getDate());
            newCalendar.set(Calendar.HOUR, oldCalendar.get(Calendar.HOUR));
            newCalendar.set(Calendar.MINUTE, oldCalendar.get(Calendar.MINUTE));
            mEvent.setDate(newCalendar.getTime());
            updateCrime();
            updateDate();
        }
        if (requestCode == REQUEST_TIME) {
            long dateMillis = data.getLongExtra(TimePickerFragment.EXTRA_TIME, 0);
            Date date = new Date(dateMillis);
            Log.d(TAG, DateUtil.timeAndDate(date));
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTime(date);
            Calendar oldCalendar = Calendar.getInstance();
            oldCalendar.setTime(mEvent.getDate());
            newCalendar.set(Calendar.YEAR, oldCalendar.get(Calendar.YEAR));
            newCalendar.set(Calendar.MONTH, oldCalendar.get(Calendar.MONTH));
            newCalendar.set(Calendar.DAY_OF_MONTH, oldCalendar.get(Calendar.DAY_OF_MONTH));
            mEvent.setDate(newCalendar.getTime());
            updateCrime();
            updateDate();
        }

    }

    private void updateCrime() {
        EventLab.get(getActivity()).updateEvent(mEvent);
        mCallbacks.onCrimeUpdated(mEvent);
    }

}

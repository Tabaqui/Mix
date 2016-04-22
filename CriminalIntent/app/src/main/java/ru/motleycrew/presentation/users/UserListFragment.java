package ru.motleycrew.presentation.users;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import ru.motleycrew.R;
import ru.motleycrew.database.EventLab;
import ru.motleycrew.model.Event;
import ru.motleycrew.model.Participant;
import ru.motleycrew.utis.DateUtil;

import java.util.List;

/**
 * Created by User on 29.03.2016.
 */
public class UserListFragment extends Fragment {

    private static final String TAG = "UserListFragment";

    private static final String ARG_ID = "event_id";
    private static final int REQUEST_CONTACT = 2;

    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private Event mEvent;
//    private Button mUserList;

    private RecyclerView mRecyclerView;

    public static final Fragment newInstance(String eventId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, eventId);
        Fragment f = new UserListFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mRecyclerView != null) {
            Log.d(TAG, String.valueOf(mRecyclerView.getAdapter() == null));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String eventId = (String) getArguments().get(ARG_ID);
        mEvent = EventLab.get(getActivity()).getEvent(eventId);
        List<Participant> users = EventLab.get(getActivity()).getParticipants(eventId);
        mEvent.getParticipants().clear();
        for (Participant p : users) {
            mEvent.addParticipant(p);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_user, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_user:
                final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                PackageManager packageManager = getActivity().getPackageManager();
                if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
                    Toast.makeText(getActivity(), R.string.error_contact_manager, Toast.LENGTH_SHORT).show();
                    return true;
                }
                startActivityForResult(pickContact, REQUEST_CONTACT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.user_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        setupItemTouchHelper();
        setupAnimationDecoratorHelper();

//        mReportButton = (Button) v.findViewById(R.id.crime_report);
//        mReportButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = ShareCompat.IntentBuilder.from(getActivity())
//                        .setType("text/plain")
//                        .getIntent()
//                        .setAction(Intent.ACTION_SEND)
//                        .putExtra(Intent.EXTRA_TEXT, getCrimeReport())
//                        .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i, getString(R.string.send_report));
//                startActivity(i);
//            }
//        });

//        mCallButton = (Button) v.findViewById(R.id.call_crime_suspect);
//        mCallButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//                smsIntent.setData(Uri.parse("smsto:"));
//                smsIntent.setType("vnd.android-dir/mms-sms");
//                StringBuilder address = new StringBuilder();
//                for (Participant p : mEvent.getParticipants()) {
//                    address.append(p.getContact());
//                }
//                smsIntent.putExtra("address", address.toString());
//                smsIntent.putExtra("sms_body", "Test ");
//                startActivity(smsIntent);
//            }
//        });
        if (!mEvent.getParticipants().isEmpty()) {
//            mCallButton.setText(getString(R.string.send_notify));
        } else {
//            mCallButton.setEnabled(false);
        }
//        mUserList = (Button) v.findViewById(R.id.bt_event_users);
//        mUserList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = UserListActivity.newIntent(getActivity(), mEvent.getId());
//                startActivity(i);
//            }
//        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID
            };
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            String name = "";
            String contact = "";
            try {
                Log.d(TAG, Integer.toString(c.getCount()));
                if (c.getColumnCount() == 0) {
                    return;
                }
                c.moveToFirst();
                name = c.getString(0);
                contact = c.getString(1);

            } finally {
                c.close();
            }
            if (!mEvent.isIn(name)) {
                Participant p = new Participant(name, contact);
                Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                if (p.getContact() != null) {
                    queryFields = new String[]{
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    };
                    c = getActivity().getContentResolver()
                            .query(phoneUri,
                                    queryFields,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{String.valueOf(p.getContact())}
                                    , null);
                    try {
                        if (c.getCount() == 0) {
                            c.close();
                            return;
                        }
                        c.moveToFirst();
                        String phoneNum = c.getString(0);
                        p.setContact(phoneNum);
//                        mEvent.addParticipant(p);
                        UserAdapter adapter = (UserAdapter) mRecyclerView.getAdapter();
                        adapter.add(p, mEvent.getParticipants().size());
                    } finally {
                        c.close();
                    }
                }
            }
        }
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mEvent.isApproved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateString = DateUtil.longDate(mEvent.getDate());
//        String suspect = mEvent.getSuspect();
//        if (suspect == null) {
//            suspect = getString(R.string.crime_report_no_suspect);
//        } else {
//            suspect = getString(R.string.crime_report_suspect, suspect);
//        }
        String report = getString(R.string.crime_report, mEvent.getTitle(), dateString, solvedString, null);
        return report;
    }


    private void setupAdapter() {
        UserAdapter adapter = new UserAdapter(mEvent.getParticipants());
        mRecyclerView.setAdapter(adapter);
    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {

        private List<Participant> mParticipants;

        public UserAdapter(List<Participant> participants) {
            if (participants == null) {
                throw new NullPointerException();
            }
            mParticipants = participants;
        }

        @Override
        public int getItemCount() {
            return mParticipants.size();
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.list_item_user, parent, false);
            return new UserHolder(v);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            Participant user = mParticipants.get(position);
            holder.bind(user);
        }


        public void add(Participant user, int position) {
            if (!mParticipants.contains(user)) {
                mParticipants.add(user);
                notifyItemInserted(position);
                EventLab.get(UserListFragment.this.getActivity()).addUser(user, mEvent.getId());
            } else {
//                notifyDataSetChanged();
            }
        }

        public void remove(int position) {
            Participant user = mParticipants.get(position);
            EventLab.get(UserListFragment.this.getActivity()).deleteUser(user);
            mParticipants.remove(position);
            notifyItemRemoved(position);
        }
    }

    private class UserHolder extends RecyclerView.ViewHolder {

        Participant mUser;

        private TextView mUserTextView;

        public UserHolder(View itemView) {
            super(itemView);
            mUserTextView = (TextView) itemView.findViewById(R.id.tv_user_name);
        }

        public void bind(Participant user) {
            mUser = user;
            mUserTextView.setText(mUser.getName());
        }
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    Drawable background;
                    Drawable xMark;
                    int xMarkMargin;
                    boolean initiated;

                    private void init() {
                        background = new ColorDrawable(Color.RED);
                        xMark = ContextCompat.getDrawable(UserListFragment.this.getActivity(), R.drawable.ic_clear_24dp);
                        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        xMarkMargin = (int) getResources().getDimension(R.dimen.ic_clear_margin);
                        initiated = true;
                    }

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int swipedPosition = viewHolder.getAdapterPosition();
                        UserAdapter adapter = (UserAdapter) mRecyclerView.getAdapter();
                        adapter.remove(swipedPosition);
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        View itemView = viewHolder.itemView;
                        if (viewHolder.getAdapterPosition() == -1) {
                            return;
                        }
                        if (!initiated) {
                            init();
                        }
                        background.setBounds(itemView.getRight() + (int) dX,
                                itemView.getTop(),
                                itemView.getRight(),
                                itemView.getBottom());
                        background.draw(c);
                        // it will be going with min sign;
                        int itemHeight = itemView.getBottom() - itemView.getTop();
                        int intrinsicWidth = xMark.getIntrinsicWidth();
                        int intrinsicHeight = xMark.getIntrinsicWidth();
                        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                        int xMarkRight = itemView.getRight() - xMarkMargin;
                        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int xMarkBottom = xMarkTop + intrinsicHeight;
                        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                        xMark.draw(c);

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setupAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            Drawable background;
            boolean initiated;

            private void init() {
                if (!initiated) {
                    background = new ColorDrawable(Color.RED);
                    initiated = true;
                }
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                init();

                if (parent.getItemAnimator().isRunning()) {
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;
                    int left = 0;
                    int right = parent.getWidth();
                    int top = 0;
                    int bottom = 0;
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }
                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        top = lastViewComingDown.getBottom() +
                                (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() +
                                (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        top = lastViewComingDown.getBottom() +
                                (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() +
                                (int) firstViewComingUp.getTranslationY();
                    }
                    background.setBounds(left, top, right, bottom);
                    background.draw(c);
                }
                super.onDraw(c, parent, state);
            }
        });
    }

}

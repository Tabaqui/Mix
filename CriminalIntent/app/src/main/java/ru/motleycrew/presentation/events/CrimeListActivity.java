package ru.motleycrew.presentation.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import ru.motleycrew.R;
import ru.motleycrew.model.Event;
import ru.motleycrew.presentation.event.CrimeFragment;
import ru.motleycrew.presentation.event.CrimePagerActivity;
import ru.motleycrew.presentation.SingleFragmentActivity;

/**
 * Created by User on 17.01.2016.
 */
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, CrimeListActivity.class);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        List<Participant> participants = EventLab.get(getApplicationContext()).getParticipants(null);
//        List<String> logins = new ArrayList<>();
//        for (Participant p : participants) {
//            logins.add(p.getEmail());
//        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Event event) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, event.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(event.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Event event) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}

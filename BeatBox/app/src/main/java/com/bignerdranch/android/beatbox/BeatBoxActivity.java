package com.bignerdranch.android.beatbox;

import android.support.v4.app.Fragment;

/**
 * Created by User on 02.02.2016.
 */
public class BeatBoxActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return BeatBoxFragment.newInstance();
    }
}

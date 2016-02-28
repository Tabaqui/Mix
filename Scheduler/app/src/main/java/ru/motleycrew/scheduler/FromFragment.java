package ru.motleycrew.scheduler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * Created by User on 26.02.2016.
 */
public class FromFragment extends Fragment {

    private Fragment mMapFragment;

    public static Fragment newInstance() {
        return new FromFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_from, container, false);
        FragmentManager fm = getChildFragmentManager();
        Fragment f = fm.findFragmentById(R.id.map_container);
        if (f == null) {
            fm.beginTransaction()
                    .replace(R.id.map_container, PlaceFragment.newInstance())
                    .commit();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
        adapter.addAll(strings);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete);
        textView.setAdapter(adapter);
        return view;
    }

    private final String[] strings = {"Byte", "KiloByte", "MegaByte", "GigaByte"};
}

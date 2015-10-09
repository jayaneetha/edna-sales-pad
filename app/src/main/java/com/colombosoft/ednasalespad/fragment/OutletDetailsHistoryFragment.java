package com.colombosoft.ednasalespad.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colombosoft.ednasalespad.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OutletDetailsHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OutletDetailsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutletDetailsHistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outlet_details_history, container, false);
        return rootView;
    }

}

package com.colombosoft.ednasalespad.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colombosoft.ednasalespad.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OutletDetailsCompetitorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OutletDetailsCompetitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutletDetailsCompetitorFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outlet_details_competitor, container, false);
        return rootView;
    }
}

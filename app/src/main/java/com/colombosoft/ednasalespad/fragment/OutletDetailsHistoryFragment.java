package com.colombosoft.ednasalespad.fragment;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.adapter.OutletHistoryAdapter;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.Outlet;

public class OutletDetailsHistoryFragment extends Fragment {

    ListView outletHistoryListView;
    Outlet outlet;
    DatabaseHandler dbHandler;
    SharedPref pref;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outlet_details_history, container, false);

        dbHandler = DatabaseHandler.getDbHandler(getActivity());
        pref = SharedPref.getInstance(getActivity());
        outlet = dbHandler.getOutletOfId(pref.getSelectedOutletId());

        outletHistoryListView = (ListView) rootView.findViewById(R.id.outlet_details_history_list_view);
        outletHistoryListView.setAdapter(new OutletHistoryAdapter(getActivity().getApplicationContext(), outlet));

        return rootView;
    }

}

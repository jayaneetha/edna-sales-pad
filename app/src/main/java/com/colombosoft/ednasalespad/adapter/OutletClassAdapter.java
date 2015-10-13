package com.colombosoft.ednasalespad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.model.OutletClass;

import java.util.List;

/**
 * Created by Admin on 10/13/15.
 */
public class OutletClassAdapter extends BaseAdapter {

    private static LayoutInflater layoutInflater = null;
    List<OutletClass> OutletClassList;

    public OutletClassAdapter(Context context, List<OutletClass> OutletClass) {
        this.OutletClassList = OutletClass;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return OutletClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return OutletClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return OutletClassList.get(position).getClassId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        View rowView = layoutInflater.inflate(R.layout.spinner_item, null);
        holder.outlet_class = (TextView) rowView.findViewById(R.id.textViewSpinner);
        holder.outlet_class.setText(OutletClassList.get(position).getClassName());
        return rowView;
    }

    public class Holder {
        TextView outlet_class;
    }
}

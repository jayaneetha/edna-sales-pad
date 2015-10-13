package com.colombosoft.ednasalespad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.model.OutletType;

import java.util.List;

/**
 * Created by Admin on 10/13/15.
 */
public class OutletTypeAdapter extends BaseAdapter {

    private static LayoutInflater layoutInflater = null;
    List<OutletType> outletTypeList;

    public OutletTypeAdapter(Context context, List<OutletType> outletTypes) {
        this.outletTypeList = outletTypes;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return outletTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return outletTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return outletTypeList.get(position).getTypeId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        View rowView = layoutInflater.inflate(R.layout.spinner_item, null);
        holder.type = (TextView) rowView.findViewById(R.id.textViewSpinner);
        holder.type.setText(outletTypeList.get(position).getTypeName());
        return rowView;
    }

    public class Holder {
        TextView type;
    }
}

package com.colombosoft.ednasalespad.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.model.HistoryDetail;
import com.colombosoft.ednasalespad.model.Invoice;
import com.colombosoft.ednasalespad.model.Outlet;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 10/11/15.
 */
public class OutletHistoryAdapter extends BaseAdapter {

    private static LayoutInflater layoutInflater = null;
    Outlet outlet;
    List<HistoryDetail> historyDetailList;
    String[] list;
    Context context;

    public OutletHistoryAdapter(Context context, Outlet outlet) {

        this.outlet = outlet;
        this.context = context;
        this.historyDetailList = outlet.getOutletHistory();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return historyDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = layoutInflater.inflate(R.layout.fragment_outlet_details_history_item, null);

        holder.date = (TextView) rowView.findViewById(R.id.tv_outlet_history_item_date);
        holder.type = (TextView) rowView.findViewById(R.id.tv_outlet_history_item_type);
        holder.amount = (TextView) rowView.findViewById(R.id.tv_outlet_history_item_amount);
        holder.balance = (TextView) rowView.findViewById(R.id.tv_outlet_history_item_balance);

        holder.date.setText(getDate(historyDetailList.get(position).getDate()));
        holder.type.setText(getType(historyDetailList.get(position).getHistoryType()));
        holder.amount.setText(String.valueOf(historyDetailList.get(position).getInvoice().getTotalAmount()));
        holder.balance.setText(String.valueOf(getBalance(historyDetailList.get(position).getInvoice())));

        return rowView;
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    private String getType(int historyType) {
        switch (historyType) {
            case 0:
                return "Other";
            case 1:
                return "Invoice";
            default:
                return "";
        }
    }

    private double getBalance(Invoice invoice){
        return (invoice.getTotalAmount() - invoice.getTotalDiscount()) - invoice.getTotalPaidAmount();
    }

    public class Holder {
        TextView date, type, amount, balance;
    }
}

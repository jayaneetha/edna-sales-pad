package com.colombosoft.ednasalespad.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.libs.widget.AnimatedExpandableListView;
import com.colombosoft.ednasalespad.model.Outlet;
import com.colombosoft.ednasalespad.model.VisitDetail;

import java.util.List;

/**
 * Created by thahzan on 1/12/15.
 */
public class OutletExpandableAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private Context context;
    private List<Outlet> outlets;
    private List<VisitDetail> visitDetails;
    private LayoutInflater inflater;
    private Resources resources;

    public OutletExpandableAdapter(Context context, List<Outlet> outlets, List<VisitDetail> visitDetails) {
        this.context = context;
        this.outlets = outlets;
        this.visitDetails = visitDetails;
        inflater = LayoutInflater.from(context);
        this.resources = context.getResources();
    }

    private static class GroupHolder {
        TextView outletName;
        TextView outletAddress;
        RelativeLayout indicatorContainer;
        TextView indicator;
    }

    private static class ChildHolder {
        TextView ownerName;
        TextView landNumber;
        TextView mobileNumber;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder childHolder;
        Outlet outlet = getGroup(groupPosition);

        if(convertView == null) {
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.item_outlet_child, parent, false);
            childHolder.ownerName = (TextView)convertView.findViewById(R.id.item_outlet_child_owner_name);
            childHolder.landNumber = (TextView)convertView.findViewById(R.id.item_outlet_child_land_tv);
            childHolder.mobileNumber = (TextView)convertView.findViewById(R.id.item_outlet_child_mobile_tv);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder)convertView.getTag();
        }

        childHolder.ownerName.setText(outlet.getOwnerName());
        childHolder.landNumber.setText(outlet.getContactLand());
//        childHolder.mobileNumber.setText(outlet.getContactMobile());

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public int getGroupCount() {
        if(outlets != null) {
            return outlets.size();
        }
        return 0;
    }

    @Override
    public Outlet getGroup(int groupPosition) {
        if(outlets != null){
            return outlets.get(groupPosition);
        }
        return null;
    }

    @Override
    public Outlet getChild(int groupPosition, int childPosition) {
        if(outlets != null){
            return outlets.get(groupPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        if(outlets != null){
            return groupPosition;
        }
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if(outlets != null) {
            return groupPosition;
        }
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupHolder groupHolder;
        Outlet outlet = getGroup(groupPosition);

        if(convertView == null) {
            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.item_outlet_parent, parent, false);
            groupHolder.outletName = (TextView)convertView.findViewById(R.id.item_outlet_tv_outlet_name);
            groupHolder.outletAddress = (TextView)convertView.findViewById(R.id.item_outlet_tv_address);
            groupHolder.indicatorContainer = (RelativeLayout)convertView.findViewById(R.id.item_outlet_rl_indicator);
            groupHolder.indicator = (TextView)convertView.findViewById(R.id.item_outlet_tv_indicator);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder)convertView.getTag();
        }

        groupHolder.outletName.setText(outlet.getOutletName());
        groupHolder.outletAddress.setText(outlet.getAddress());

        boolean unpFound = false;
        boolean invFound = false;

        for(VisitDetail visitDetail : visitDetails) {
            if(visitDetail.getOutletId() == outlet.getOutletId()) {
                switch (visitDetail.getVisitStatus()){
                    case VisitDetail.STATUS_INVOICED:
                        invFound = true;
                        break;
                    case VisitDetail.STATUS_UNPRODUCTIVE:
                        unpFound = true;
                        break;
                    default:
                        break;
                }
            }
        }

        if(invFound){
            groupHolder.indicatorContainer.setBackgroundColor(resources.getColor(R.color.light_500));
            groupHolder.indicator.setText("P");
        } else if(unpFound) {
            groupHolder.indicatorContainer.setBackgroundColor(resources.getColor(R.color.light_700));
            groupHolder.indicator.setText("U");
        } else {
            groupHolder.indicatorContainer.setBackgroundColor(resources.getColor(R.color.light_900));
            groupHolder.indicator.setText("N");
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

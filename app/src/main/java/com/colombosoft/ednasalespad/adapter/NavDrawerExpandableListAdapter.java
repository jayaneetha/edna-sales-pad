package com.colombosoft.ednasalespad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colombosoft.ednasalespad.R;
import com.colombosoft.ednasalespad.libs.widget.AnimatedExpandableListView;
import com.colombosoft.ednasalespad.model.ChildItem;
import com.colombosoft.ednasalespad.model.GroupItem;

import java.util.List;

public class NavDrawerExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private Context context;
    private List<GroupItem> list;
    private LayoutInflater inflater;

    private static class GroupHolder{
        ImageView icon;
        TextView title;
        ImageView indicator;
    }

    private static class ChildHolder{
        TextView title;
        TextView hint;
    }

    public NavDrawerExpandableListAdapter(Context context, List<GroupItem> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder childHolder;
        ChildItem child = getChild(groupPosition, childPosition);

        if(convertView == null){
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.item_child, parent, false);
            childHolder.title = (TextView)convertView.findViewById(R.id.child_item_title_tv);
            childHolder.hint = (TextView)convertView.findViewById(R.id.child_item_hint_tv);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder)convertView.getTag();
        }

        childHolder.title.setText(child.getChildTitle());
        childHolder.hint.setText(child.getHint());

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if(list.get(groupPosition).isExpandable()){
            return list.get(groupPosition).getChildren().size();
        }

        return 0;
    }

    @Override
    public int getGroupCount() {
        return list.size() ;
    }

    @Override
    public GroupItem getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public ChildItem getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Integer.parseInt(String.valueOf(groupPosition) + String.valueOf(childPosition));
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupHolder groupHolder;
        GroupItem groupItem = getGroup(groupPosition);

        if(convertView == null){
            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.item_group, parent, false);
            groupHolder.title = (TextView)convertView.findViewById(R.id.group_item_textview);
            groupHolder.icon = (ImageView)convertView.findViewById(R.id.group_item_icon);
            groupHolder.indicator = (ImageView)convertView.findViewById(R.id.group_item_indicator);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder)convertView.getTag();
        }

        if(groupItem.getIcon() != null) {
            if(groupHolder.icon.getVisibility() == View.GONE){
                groupHolder.icon.setVisibility(View.VISIBLE);
            }
            groupHolder.icon.setImageDrawable(groupItem.getIcon());
        } else {
            groupHolder.icon.setVisibility(View.GONE);
        }
        groupHolder.title.setText(groupItem.getParentTitle());
        if(groupItem.isExpandable()) {
            if(groupHolder.indicator.getVisibility() == View.VISIBLE){
                groupHolder.indicator.setVisibility(View.INVISIBLE);
            }
            if(groupItem.isExpanded()){
                groupHolder.indicator.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_up));
            } else {
                groupHolder.indicator.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_down));
            }
            groupHolder.indicator.setVisibility(View.VISIBLE);
        } else {
            if(groupHolder.indicator.getVisibility() == View.VISIBLE){
                groupHolder.indicator.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

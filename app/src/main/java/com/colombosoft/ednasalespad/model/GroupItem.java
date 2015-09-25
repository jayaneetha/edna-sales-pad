package com.colombosoft.ednasalespad.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thahzan on 1/2/15.
 */
public class GroupItem {

    private int index;
    private Drawable icon;
    private String parentTitle;
    private List<ChildItem> children = new ArrayList<ChildItem>();
    private boolean expanded = false;
    private boolean isExpandable = false;

    public GroupItem(int index, Drawable icon, String parentTitle) {
        this.index = index;
        this.icon = icon;
        this.parentTitle = parentTitle;
        this.isExpandable = false;
        this.expanded = false;
    }

    public GroupItem(int index, Drawable icon, String parentTitle, List<ChildItem> children) {
        this.index = index;
        this.icon = icon;
        this.parentTitle = parentTitle;
        this.children = children;
        this.isExpandable = true;
        this.expanded = false;
    }

    public GroupItem() {}

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public List<ChildItem> getChildren() {
        return children;
    }

    public void setChildren(List<ChildItem> children) {
        this.children = children;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean isExpandable) {
        this.isExpandable = isExpandable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

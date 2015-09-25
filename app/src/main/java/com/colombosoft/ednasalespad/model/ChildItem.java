package com.colombosoft.ednasalespad.model;

/**
 * Created by thahzan on 1/2/15.
 */
public class ChildItem {

    private int index;
    private String childTitle, hint;

    public ChildItem(int index, String childTitle, String hint) {
        this.index = index;
        this.childTitle = childTitle;
        this.hint = hint;
    }

    public ChildItem() {}

    public String getChildTitle() {
        return childTitle;
    }

    public void setChildTitle(String childTitle) {
        this.childTitle = childTitle;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TaZ on 4/10/15.
 */
public class OutletClass {

    private int classId;
    private String className;

    public OutletClass(int classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public static OutletClass parseOutletType(JSONObject instance) throws JSONException {

        if(instance != null) {
            return new OutletClass(instance.getInt(""), instance.getString(""));
        }

        return null;
    }

}

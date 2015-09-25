package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TaZ on 4/10/15.
 */
public class OutletType {

    private int typeId;
    private String typeName;

    public OutletType(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static OutletType parseOutletType(JSONObject instance) throws JSONException {

        if(instance != null) {
            return new OutletType(instance.getInt(""), instance.getString(""));
        }

        return null;
    }

}

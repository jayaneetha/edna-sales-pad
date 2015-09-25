package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thahzan on 2/16/15.
 */
public class ProductType {

    private int typeId;
    private String typeName;

    public ProductType(int typeId, String typeName) {
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

    public static ProductType parseType(JSONObject instance) throws JSONException {

        if(instance != null) {
            ProductType type = new ProductType(instance.getInt("product_type_id"), instance.getString("product_type"));
            return type;
        }

        return null;
    }

}

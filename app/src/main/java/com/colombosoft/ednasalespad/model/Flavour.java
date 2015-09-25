package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by thahzan on 1/5/15.
 */
public class Flavour implements Serializable {

    private int flavourId;
    private String flavourName, colorCode;
    private int selectedQty;

    private int itemId;

    public Flavour() {}

    /**
     * Use this constructor to create just the flavours for storing purposes.
     * @param flavourId
     * @param flavourName
     * @param colorCode
     */
    public Flavour(int flavourId, String flavourName, String colorCode) {
        this.flavourId = flavourId;
        this.flavourName = flavourName;
        this.colorCode = colorCode;
    }

    /**
     * Use this constructor to create flavours with quantities
     * @param flavourId
     * @param flavourName
     * @param colorCode
     * @param selectedQty
     */
    public Flavour(int flavourId, String flavourName, String colorCode, int selectedQty) {
        this.flavourId = flavourId;
        this.flavourName = flavourName;
        this.colorCode = colorCode;
        this.selectedQty = selectedQty;
    }

    public int getFlavourId() {
        return flavourId;
    }

    public void setFlavourId(int flavourId) {
        this.flavourId = flavourId;
    }

    public String getFlavourName() {
        return flavourName;
    }

    public void setFlavourName(String flavourName) {
        this.flavourName = flavourName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public int getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(int selectedQty) {
        this.selectedQty = selectedQty;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public static Flavour parseFlavour(JSONObject instance) throws JSONException {

        if(instance != null) {
            Flavour flavour = new Flavour(instance.getInt("product_flavour_id"), instance.getString("product_flavour"), "#000000");

            if(flavour.getFlavourName().equalsIgnoreCase("milk")) {
                flavour.setColorCode("#FF3299C8");
            } else if(flavour.getFlavourName().equalsIgnoreCase("coffee")){
                flavour.setColorCode("#FF450505");
            } else if(flavour.getFlavourName().equalsIgnoreCase("R&R")){
                flavour.setColorCode("#FFFF040C");
            } else if(flavour.getFlavourName().equalsIgnoreCase("crispy")) {
                flavour.setColorCode("#FFF1DF02");
            } else if(flavour.getFlavourName().equalsIgnoreCase("orange")) {
                flavour.setColorCode("#FFFF8700");
            } else if(flavour.getFlavourName().equalsIgnoreCase("vanila")) {
                flavour.setColorCode("#FFFFBB33");
            } else if(flavour.getFlavourName().equalsIgnoreCase("s.berry")) {
                flavour.setColorCode("#FFFF4444");
            } else if(flavour.getFlavourName().equalsIgnoreCase("fruit")) {
                flavour.setColorCode("#FF9933CC");
            } else if(flavour.getFlavourName().equalsIgnoreCase("mango")) {
                flavour.setColorCode("#FF669900");
            } else if(flavour.getFlavourName().equalsIgnoreCase("banana")) {
                flavour.setColorCode("#FFFBFF00");
            } else if(flavour.getFlavourName().equalsIgnoreCase("faluda")) {
                flavour.setColorCode("#FFFF4444");
            } else if(flavour.getFlavourName().equalsIgnoreCase("cho.mol")) {
                flavour.setColorCode("#FFA22F05");
            } else {
                flavour.setFlavourName("");
                flavour.setColorCode("#00000000");
            }

            return flavour;
        }

        return null;
    }

}

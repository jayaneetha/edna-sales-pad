package com.colombosoft.ednasalespad.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by thahzan on 1/22/15.
 */
public class ReturnDetail implements Serializable {

    private long orderId;
    private Item item;
    private int flavourId, qty;
    private double returnPrice;

    public ReturnDetail() {}

    public ReturnDetail(long orderId, Item item, int flavourId, int qty, double returnPrice) {
        this.orderId = orderId;
        this.item = item;
        this.flavourId = flavourId;
        this.qty = qty;
        this.returnPrice = returnPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getFlavourId() {
        return flavourId;
    }

    public void setFlavourId(int flavourId) {
        this.flavourId = flavourId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }

    public JSONObject getReturnDetailAsJSON() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("id_item", item.getItemNo());
        params.put("price", item.getWholesalePrice()/item.getUnitQty());
        params.put("return", qty);
        params.put("qty", 0);
        params.put("replace", 0);
        params.put("sample", 0);
        params.put("free", 0);
        params.put("salableReturns", 0);

        JSONObject json = new JSONObject(params);

        return json;
    }

}

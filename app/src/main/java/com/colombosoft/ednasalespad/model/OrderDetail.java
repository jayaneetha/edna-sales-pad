package com.colombosoft.ednasalespad.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by thahzan on 1/21/15.
 */
public class OrderDetail implements Serializable {

    private long orderId;
    private Item item;
    private int freeQty, flavourId;

    public OrderDetail() {
    }

    public OrderDetail(long orderId, Item item, int freeQty) {
        this.orderId = orderId;
        this.item = item;
        this.freeQty = freeQty;
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

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    public int getFlavourId() {
        return flavourId;
    }

    public void setFlavourId(int flavourId) {
        this.flavourId = flavourId;
    }

    public JSONObject getOrderDetailAsJSON(Order order) {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("id_item", item.getItemNo());
        params.put("price", item.getWholesalePrice());

        if (item.getCategoryId() == 12 || item.getCategoryId() == 14 || (item.getCategoryId() == 13 && item.getTypeId() != 38)) {
            params.put("net_after_discount", (order.getDiscountPercentage() * item.getWholesalePrice()) / 100);
        } else {
            params.put("net_after_discount", 0);
        }

        params.put("return", item.getReturnQty());
        params.put("qty", item.getSelectedQty());
        params.put("replace", 0);
        params.put("sample", 0);
        params.put("free", freeQty);
        params.put("salableReturns", 0);

        return new JSONObject(params);
    }

}

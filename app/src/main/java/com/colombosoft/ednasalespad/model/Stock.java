package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thahzan on 2/19/15.
 */
public class Stock {

    private int stockItemId;
    private double stockItemQty;

    public Stock() {
    }

    public Stock(int stockItemId, double stockItemQty) {
        this.stockItemId = stockItemId;
        this.stockItemQty = stockItemQty;
    }

    public int getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(int stockItemId) {
        this.stockItemId = stockItemId;
    }

    public double getStockItemQty() {
        return stockItemQty;
    }

    public void setStockItemQty(double stockItemQty) {
        this.stockItemQty = stockItemQty;
    }

    public static Stock parseStock(JSONObject instance) throws JSONException {

        if(instance != null) {
            double qty = instance.getDouble("qty");
            if(qty > 0) {
                return new Stock(instance.getInt("product_id"), instance.getDouble("qty"));
            }
        }

        return null;
    }

}

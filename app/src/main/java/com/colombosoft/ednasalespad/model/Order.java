package com.colombosoft.ednasalespad.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by thahzan on 1/21/15.
 */
public class Order implements Serializable {

    private final String LOG_TAG = Order.class.getSimpleName();

    private long orderId;
    private long orderTime;
    private int outletId, routeId;
    private double grossAmount;
    private double returnAmount;
    private double eligibleAmount;
    private double discount;
    private double discountPercentage;
    private double marginAmount;
    private double netAmount;

    private double latitude;
    private double longitude;
    private int batteryLevel;
    private List<OrderDetail> orderDetails;
    private List<ReturnDetail> returnDetails;
    private Payment payment = new Payment();
    private boolean isSynced = false;

    private boolean discountPercent = false;

    public Order() {
        isSynced = false;
    }

    public Order(long orderId, long orderTime, int outletId, double discount, double latitude, double longitude, List<OrderDetail> orderDetails, List<ReturnDetail> returnDetails, Payment payment) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.outletId = outletId;
        this.discount = discount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderDetails = orderDetails;
        this.payment = payment;
        this.returnDetails = returnDetails;
        this.isSynced = false;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public double getEligibleAmount() {
        return eligibleAmount;
    }

    public void setEligibleAmount(double eligibleAmount) {
        this.eligibleAmount = eligibleAmount;
    }

    public double getMarginAmount() {
        return marginAmount;
    }

    public void setMarginAmount(double marginAmount) {
        this.marginAmount = marginAmount;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<ReturnDetail> getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(List<ReturnDetail> returnDetails) {
        this.returnDetails = returnDetails;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public boolean isDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(boolean discountPercent) {
        this.discountPercent = discountPercent;
    }

    public JSONObject getOrderAsJSON() throws JSONException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        HashMap<String, Object> finalJSONParams = new HashMap<String, Object>();

        HashMap<String, Object> invoiceParams = new HashMap<String, Object>();
        invoiceParams.put("orderId", orderId);
        invoiceParams.put("invtype", 0);
        invoiceParams.put("outletid", outletId);
        invoiceParams.put("routeid", routeId);
        invoiceParams.put("lat", latitude);
        invoiceParams.put("lon", longitude);
        invoiceParams.put("bat", batteryLevel);
        invoiceParams.put("discount", discount);
        invoiceParams.put("grossamount", grossAmount);
        invoiceParams.put("returnamount", returnAmount);
        invoiceParams.put("eligibleamount", eligibleAmount);
        invoiceParams.put("marginamount", marginAmount);
        invoiceParams.put("netamount", netAmount);
        invoiceParams.put("discountpercentage", discountPercentage);

        Date date = new Date(orderTime);

        String combinedDate = sdf.format(date);

        String[] broken = combinedDate.split(" ");

        invoiceParams.put("invDate", broken[0]);
        invoiceParams.put("invtime", broken[1]);

        JSONObject invoiceJSON = new JSONObject(invoiceParams);

        finalJSONParams.put("Invoice", invoiceJSON);

        finalJSONParams.put("posm", new JSONArray());

        JSONArray itemsArray = new JSONArray();

        if (orderDetails != null) {
            for (int i = 0; i < orderDetails.size(); i++) {
                JSONObject tmpItemJSON = orderDetails.get(i).getOrderDetailAsJSON(this);
                if (tmpItemJSON != null) {
                    itemsArray.put(tmpItemJSON);
                }
            }
        }

        if (returnDetails != null) {
            for (int i = 0; i < returnDetails.size(); i++) {
                JSONObject tmpItemJSON = returnDetails.get(i).getReturnDetailAsJSON();
                if (tmpItemJSON != null) {
                    itemsArray.put(tmpItemJSON);
                }
            }
        }

        finalJSONParams.put("invitems", itemsArray);

        JSONArray paymentArray = new JSONArray();
        if (payment != null) {
//            JSONObject tmpPaymentObject = payment.getPaymentAsJSON();
//            if(tmpPaymentObject != null) {
//                if(payment.getCashPayment() != null){
//                    if(payment.getCheque() != null) {
//                        tmpPaymentObject.put("type", "Both");
//                    } else {
//                        tmpPaymentObject.put("type", "Cash");
//                    }
//                } else if (payment.getCheque() != null) {
//                    tmpPaymentObject.put("type", "Cheque");
//                }
//                paymentArray.put(tmpPaymentObject);
//            }
        }

        finalJSONParams.put("Payment", paymentArray);

        JSONObject finalObject = new JSONObject(finalJSONParams);

        Log.wtf(LOG_TAG, "Final JSONString\n" + finalObject.toString());

        return new JSONObject(finalJSONParams);
    }

    public double calculateTotalReturns() {

        if(returnDetails != null) {
            double total = 0;
            for(ReturnDetail returnDetail : returnDetails) {
                total += returnDetail.getReturnPrice() * returnDetail.getQty();
            }
            return total;
        }

        return 0;
    }

}

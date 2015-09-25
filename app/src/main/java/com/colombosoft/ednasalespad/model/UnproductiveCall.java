package com.colombosoft.ednasalespad.model;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by thahzan on 1/21/15.
 */
public class UnproductiveCall {

    private long time;
    private String reason;
    private int outletId;
    private double latitude, longitude;
    private int batteryLevel;
    private boolean isSynced;
    private String remark;

    public UnproductiveCall() {}

    public UnproductiveCall(long time, String reason, String remark, int outletId, double latitude, double longitude, boolean isSynced) {
        this.time = time;
        this.reason = reason;
        this.remark = remark;
        this.outletId = outletId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSynced = isSynced;
    }

    public UnproductiveCall(long time, String reason, String remark ,int outletId, double latitude, double longitude, int batteryLevel) {
        this.time = time;
        this.reason = reason;
        this.remark = remark;
        this.outletId = outletId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.batteryLevel = batteryLevel;
        isSynced = false;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
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

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public JSONObject getUnproductiveCallAsJSON() {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("unp_time", this.time);
        map.put("unp_reason", this.reason);
        map.put("outlet_id", this.outletId);
        map.put("latitude", this.latitude);
        map.put("longitude", this.longitude);
        map.put("battery_level", this.batteryLevel);
        map.put("remark", this.remark == null ? "N/A" : this.remark);

        return new JSONObject(map);
    }

}

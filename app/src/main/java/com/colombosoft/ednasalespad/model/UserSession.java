package com.colombosoft.ednasalespad.model;

/**
 * Created by thahzan on 2/17/15.
 */
public class UserSession {

    private int sessionId;
    private long startTime, endTime;
    private double latitude, longitude;

    public UserSession() {}

    public UserSession(int sessionId, long startTime, long endTime, double latitude, double longitude) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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
}

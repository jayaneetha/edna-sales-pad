package com.colombosoft.ednasalespad.model;

/**
 * Created by thahzan on 2/6/15.
 */
public class Attendance {

    public static final int ATTENDANCE_STATUS_START = 0;
    public static final int ATTENDANCE_STATUS_END = 1;

    private long logTime;
    private int logStatus;
    private boolean isSynced = false;
    private double latitude, longitude;
    private int localSession;
    private String location;

    public Attendance() {}

    public Attendance(long logTime, int logStatus) {
        this.logTime = logTime;
        this.logStatus = logStatus;
    }

    public Attendance(long logTime, int logStatus, boolean isSynced, int localSession, String loc) {
        this.logTime = logTime;
        this.logStatus = logStatus;
        this.isSynced = isSynced;
        this.localSession = localSession;
        this.location = loc;
    }
//    public Attendance(long logTime, int logStatus, boolean isSynced, int localSession) {
//        this.logTime = logTime;
//        this.logStatus = logStatus;
//        this.isSynced = isSynced;
//        this.localSession = localSession;
//    }

    public String getLoc() {
        return location;
    }

    public void setLoc(String loc) {
        this.location = loc;
    }

    public long getLogTime() {
        return logTime;
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    public int getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(int logStatus) {
        this.logStatus = logStatus;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
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

    public int getLocalSession() {
        return localSession;
    }

    public void setLocalSession(int localSession) {
        this.localSession = localSession;
    }
}

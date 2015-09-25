package com.colombosoft.ednasalespad.model;

/**
 * Created by thahzan on 2/5/15.
 */
public class VisitDetail {

    private int outletId, visitStatus, sessionId;

    public static final int STATUS_NOT_VISITED = 0;
    public static final int STATUS_INVOICED = 1;
    public static final int STATUS_UNPRODUCTIVE = 2;

    public VisitDetail() {}

    /**
     * Create a VisitDetail
     * @param outletId ID of the outlet
     * @param visitStatus The visit Status. 0 -> Not Visited, 1 -> Invoiced, 2 -> Unproductive
     */
    public VisitDetail(int outletId, int visitStatus, int sessionId) {
        this.outletId = outletId;
        this.visitStatus = visitStatus;
        this.sessionId = sessionId;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public int getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(int visitStatus) {
        this.visitStatus = visitStatus;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}

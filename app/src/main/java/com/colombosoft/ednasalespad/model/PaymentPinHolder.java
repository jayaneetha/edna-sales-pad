package com.colombosoft.ednasalespad.model;

/**
 * Created by TaZ on 4/8/15.
 */
public class PaymentPinHolder {

    public static final int TYPE_DAY = 0;
    public static final int TYPE_OTHER = 1;

    private int type;
    private HistoryDetail historyDetail;

//    public PaymentPinHolder(String pinLabel) {
//        this.type = TYPE_PINNED;
//        this.pinLabel = pinLabel;
//    }
//
//    public PaymentPinHolder(HistoryDetail historyDetail) {
//        this.type = TYPE_OTHER;
//        this.historyDetail = historyDetail;
//    }


    public PaymentPinHolder(int type, HistoryDetail historyDetail) {
        this.type = type;
        this.historyDetail = historyDetail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HistoryDetail getHistoryDetail() {
        return historyDetail;
    }

    public void setHistoryDetail(HistoryDetail historyDetail) {
        this.historyDetail = historyDetail;
    }
}

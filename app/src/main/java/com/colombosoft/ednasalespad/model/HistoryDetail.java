package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class HistoryDetail implements Serializable {

    public static final int TYPE_OTHER = 0;
    public static final int TYPE_INVOICE = 1;

    /**
     * historyType = 0 : No Invoice
     * historyType = 1 : Invoice
     */
    private int historyType;
    private int outletId;
    private long date;
    private String remark;
    private Invoice invoice;

    private boolean selectedOnList;

    public HistoryDetail() {
    }

    /**
     * This is to create a history without an invoice. Basically an unproductive call.
     * Call this constructor and it will automatically set historyType to 0.
     *
     * @param remark remark of the history item (Eg : Shop Closed, Owner Dead, etc.)
     */
    public HistoryDetail(int outletId, String remark, long date) {
        this.historyType = TYPE_OTHER;
        this.outletId = outletId;
        this.remark = remark;
        this.date = date;
    }

    /**
     * This is to create a history with an invoice. Use remark to state if there's any cheque payments.
     * Call this constructor and it will automatically set historyType to 1.
     *
     * @param invoice the relevant invoice
     * @param remark  remark of the invoice (Eg : Cheque Date : 2015....)
     */
    public HistoryDetail(int outletId, Invoice invoice, String remark, long date) {
        this.historyType = TYPE_INVOICE;
        this.outletId = outletId;
        this.invoice = invoice;
        this.remark = remark;
        this.date = date;
    }

    public static HistoryDetail parseHistoryDetail(JSONObject instance) throws JSONException {

        // For now, we only consider invoices as Outlet history details.
        // Have to figure out some way to add return cheques, unproductive calls to it.
        if (instance != null) {
            Invoice inv = Invoice.parseInvoice(instance);
            if (inv != null) {
                HistoryDetail detail = new HistoryDetail();
                detail.setHistoryType(1);
                detail.setInvoice(inv);
                detail.setDate(detail.getInvoice().getInvoiceTime());

                return detail;
            }
        }

        return null;
    }

    public int getHistoryType() {
        return historyType;
    }

    public void setHistoryType(int historyType) {
        this.historyType = historyType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public boolean isSelectedOnList() {
        return selectedOnList;
    }

    public void setSelectedOnList(boolean selectedOnList) {
        this.selectedOnList = selectedOnList;
    }

    public boolean isOutstandingHistory() {
        return this.historyType == TYPE_INVOICE && invoice.isOutstandingInvoice();
    }

}

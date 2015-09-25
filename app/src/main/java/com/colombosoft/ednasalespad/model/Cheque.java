package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Cheque implements Serializable {

    private String chequeNo;
    private long chequeDate;
    private double amount;
    private int bankId, branchid;
    private boolean isSynced;

    private long invoiceId;
    private int outletId;

    public Cheque() {
    }

    public Cheque(String chequeNo, long chequeDate, double amount, int bankId, int branchId) {
        this.chequeNo = chequeNo;
        this.chequeDate = chequeDate;
        this.amount = amount;
        this.bankId = bankId;
        this.branchid = branchId;
    }

    public static Cheque parseCheque(JSONObject instance) throws JSONException {

        if (instance != null) {
            Cheque cheq = new Cheque();
            cheq.setChequeNo(instance.getString("cheq_no"));

            try {
                // Parse date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date convertedDate = sdf.parse(instance.getString("cheq_date") + " 12:00:00");
                cheq.setChequeDate(convertedDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cheq.setAmount(instance.getDouble("cheq_amount"));
            cheq.setBankId(instance.getInt("cheq_bank_id"));
            cheq.setBranchid(instance.getInt("cheq_branch_id"));

            return cheq;
        }

        return null;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public long getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(long chequeDate) {
        this.chequeDate = chequeDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getBranchid() {
        return branchid;
    }

    public void setBranchid(int branchid) {
        this.branchid = branchid;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public JSONObject getChequePaymentAsJSON() {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("order_id", invoiceId);
        map.put("check_amount", amount);
        map.put("check_datetime", chequeDate);
        map.put("cash_amount", "null");
        map.put("check_bank_id", bankId);
        map.put("check_branch_id", branchid);
        map.put("check_no", chequeNo);
        map.put("cash_datetime", "null");

        return new JSONObject(map);
    }

}

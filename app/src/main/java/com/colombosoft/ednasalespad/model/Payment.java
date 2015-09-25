package com.colombosoft.ednasalespad.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thahzan on 1/21/15.
 */
public class Payment implements Serializable {

    private long orderId;
    private CashPayment cashPayment = new CashPayment();
    private Cheque cheque = new Cheque();

    public Payment() {
    }

    public Payment(CashPayment cashPayment, long orderId) {
        this.cashPayment = cashPayment;
        this.orderId = orderId;
    }

    public Payment(long orderId, Cheque cheque) {
        this.orderId = orderId;
        this.cheque = cheque;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public CashPayment getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(CashPayment cashPayment) {
        this.cashPayment = cashPayment;
    }

    public Cheque getCheque() {
        return cheque;
    }

    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }

    public JSONObject getPaymentAsJSON() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("order_id", orderId);

        if(cashPayment != null) {
            map.put("cash_amount", String.valueOf(cashPayment.getPaymentAmount()));
            map.put("cash_datetime", String.valueOf(cashPayment.getPaymentTime()));
        } else {
            map.put("cash_amount", "0.00");
            map.put("cash_datetime", "0");
        }

        if(cheque != null) {
            map.put("check_amount", String.valueOf(cheque.getAmount()));
            map.put("check_datetime", String.valueOf(cheque.getChequeDate()));
            map.put("check_no", String.valueOf(cheque.getChequeNo()));
            map.put("check_bank_id", String.valueOf(cheque.getBankId()));
            map.put("check_branch_id", String.valueOf(cheque.getBranchid()));
        } else {
            map.put("check_amount", "0.00");
            map.put("check_datetime", "0");
            map.put("check_no", "0");
            map.put("check_bank_id", "0");
            map.put("check_branch_id", "0");
        }

        return new JSONObject(map);
    }

}

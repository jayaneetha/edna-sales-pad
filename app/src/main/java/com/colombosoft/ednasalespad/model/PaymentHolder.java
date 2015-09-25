package com.colombosoft.ednasalespad.model;

/**
 * Created by TaZ on 4/5/15.
 */
public class PaymentHolder {

    public static final int TYPE_CASH = 0;
    public static final int TYPE_CHEQUE = 1;

    private long time;
    private CashPayment cashPayment;
    private Cheque cheque;
    private int type;

    /**
     * Create a cash payment instance
     * @param time The time of the payment
     * @param cashPayment The cash payment object to hold
     */
    public PaymentHolder(long time, CashPayment cashPayment) {
        this.time = time;
        this.cashPayment = cashPayment;
        this.type = TYPE_CASH;
    }

    /**
     * Create a cheque payment instance
     * @param time The time of the payment
     * @param cheque The cheque payment object to hold
     */
    public PaymentHolder(long time, Cheque cheque) {
        this.time = time;
        this.cheque = cheque;
        this.type = TYPE_CHEQUE;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}

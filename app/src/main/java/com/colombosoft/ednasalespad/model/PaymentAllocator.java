package com.colombosoft.ednasalespad.model;

/**
 * Created by TaZ on 4/6/15.
 */
public class PaymentAllocator {

    private long invoiceId;
    private CashPayment cashPayment;
    private Cheque cheque;

    private boolean isSynced;

    public PaymentAllocator(long invoiceId, CashPayment cashPayment) {
        this.invoiceId = invoiceId;
        this.cashPayment = cashPayment;
    }

    public PaymentAllocator(long invoiceId, Cheque cheque) {
        this.invoiceId = invoiceId;
        this.cheque = cheque;
    }

    public PaymentAllocator(long invoiceId, CashPayment cashPayment, Cheque cheque) {
        this.invoiceId = invoiceId;
        this.cashPayment = cashPayment;
        this.cheque = cheque;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
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

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean isSynced) {
        this.isSynced = isSynced;
    }

    public double getTotalAllocation() {

        double total = 0;
        if (cashPayment != null) {
            total += cashPayment.getPaymentAmount();
        }

        if (cheque != null) {
            total += cheque.getAmount();
        }

        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(invoiceId).append(" (");
        boolean cashAvailable = false;
        if(cashPayment != null) {
            sb.append("Cash : ").append(cashPayment.getPaymentAmount());
            cashAvailable = true;
        }
        if(cheque != null) {
            if(cashAvailable) sb.append(", ");
            sb.append("Cheque : ").append(cheque.getAmount());
        }
        sb.append(")");
        return sb.toString();
    }
}

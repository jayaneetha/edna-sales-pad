package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 */
public class Invoice implements Serializable {

    private long invoiceId, invoiceTime;
    private double totalAmount;
    private double totalDiscount;
    private double returnAmount;
    private List<Cheque> chequePayments;
    private List<CashPayment> cashPayments;

    public Invoice() {
    }

    /**
     * Constructor
     * @param invoiceId
     * @param invoiceTime
     * @param totalAmount
     * @param totalDiscount
     * @param returnAmount
     */
    public Invoice(long invoiceId, long invoiceTime, double totalAmount, double totalDiscount, double returnAmount) {
        this.invoiceId = invoiceId;
        this.invoiceTime = invoiceTime;
        this.totalAmount = totalAmount;
        this.totalDiscount = totalDiscount;
        this.returnAmount = returnAmount;
    }

    public Invoice(long invoiceId, long invoiceTime, double totalAmount, double totalDiscount, double returnAmount, List<CashPayment> cashPayments, List<Cheque> chequePayments) {
        this.invoiceId = invoiceId;
        this.invoiceTime = invoiceTime;
        this.totalAmount = totalAmount;
        this.cashPayments = cashPayments;
        this.chequePayments = chequePayments;
        this.totalDiscount = totalDiscount;
        this.returnAmount = returnAmount;
    }

    public static Invoice parseInvoice(JSONObject instance) throws JSONException {

        if (instance != null) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceId(instance.getLong("id_invoice"));

            try {
                // Parse date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date convertedDate = sdf.parse(instance.getString("added_date"));
                invoice.setInvoiceTime(convertedDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                invoice.setInvoiceTime(System.currentTimeMillis());
            }

            invoice.setTotalAmount(instance.getDouble("gross_amount"));
            invoice.setTotalDiscount(instance.getDouble("discount"));

            invoice.setReturnAmount(instance.getDouble("sales_return") + instance.getDouble("market_return"));

            CashPayment cashPayment = new CashPayment(invoice.getInvoiceTime(), instance.getDouble("total_cash"));
            Cheque cheque = new Cheque("123123", invoice.getInvoiceTime(), instance.getDouble("total_cheque"), 1, 1);

            invoice.addCashPayment(cashPayment);
            invoice.addChequePayment(cheque);

            return invoice;
        }

        return null;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public long getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(long invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public List<CashPayment> getCashPayments() {
        return cashPayments;
    }

    public void setCashPayments(List<CashPayment> cashPayments) {
        this.cashPayments = cashPayments;
    }

    public List<Cheque> getChequePayments() {
        return chequePayments;
    }

    public void setChequePayments(List<Cheque> chequePayments) {
        this.chequePayments = chequePayments;
    }

    public void addChequePayment(Cheque cheque) {
        if (chequePayments == null) {
            chequePayments = new ArrayList<Cheque>();
        }
        chequePayments.add(cheque);
    }

    public void addCashPayment(CashPayment cashPayment) {
        if (cashPayments == null) {
            cashPayments = new ArrayList<CashPayment>();
        }
        cashPayments.add(cashPayment);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Invoice) {
            if (object.hashCode() == this.hashCode()) {
                return true;
            } else {
                if (((Invoice) object).getInvoiceId() == this.getInvoiceId()) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public boolean isOutstandingInvoice() {

        double paid = getTotalPaidAmount();
        return paid < totalAmount - totalDiscount;

    }

    public double getTotalPaidAmount() {
        double paid = 0;

        if (cashPayments != null) {
            for (CashPayment cashPayment : cashPayments) {
                paid += cashPayment.getPaymentAmount();
            }
        }

        if (chequePayments != null) {
            for (Cheque cheque : chequePayments) {
                paid += cheque.getAmount();
            }
        }

        return paid;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Invoice No : ").append(invoiceId).append("\n");
        sb.append("Invoice Time : ").append(invoiceTime).append("\n");
        sb.append("Invoice Total : ").append(totalAmount).append("\n");
        sb.append("Invoice Discount : ").append(totalDiscount).append("\n");

        sb.append("Cash Payment(s)").append("\n");

        if (cashPayments != null) {
            for (CashPayment cashPayment : cashPayments) {
                sb.append(cashPayment.getPaymentTime()).append("-").append(cashPayment.getPaymentAmount()).append("\n");
            }
        }

        if (chequePayments != null) {
            for (Cheque cheque : chequePayments) {
                sb.append(cheque.getChequeNo()).append("-").append(cheque.getAmount()).append("\n");
            }
        }

        return sb.toString();
    }
}

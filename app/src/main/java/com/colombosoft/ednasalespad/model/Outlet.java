package com.colombosoft.ednasalespad.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Outlet implements Serializable {

    private int outletId, routeId, outletType, outletClass;
    private String outletName, address, ownerName, contactLand, outletCode, frontImageURI, showcaseImageUri;
    private List<HistoryDetail> outletHistory;

    public Outlet() {
    }

    /**
     * Call this method to create an outlet with id and name assigned
     *
     * @param outletId   id of the outlet
     * @param outletName name of the outlet
     */
    public Outlet(int outletId, String outletName) {
        this.outletId = outletId;
        this.outletName = outletName;
    }

    /**
     * Call this method to assign all params at the same time
     *
     * @param outletId         id of the outlet
     * @param routeId          relevant route id
     * @param outletType       outlet type id of outlet
     * @param outletClass      class of the outlet
     * @param outletName       name of the outlet
     * @param outletCode       code of the outlet
     * @param address          address of the outlet
     * @param ownerName        name of the owner
     * @param contactLand      land phone number
     * @param frontImageURI    shop front image URI
     * @param showcaseImageUri shop showcase image URI
     * @param outletHistory    history of the outlet
     */
    public Outlet(int outletId, int routeId, int outletType, int outletClass, String outletName,
                  String outletCode, String address, String ownerName, String contactLand,
                  String frontImageURI, String showcaseImageUri, List<HistoryDetail> outletHistory) {
        this.outletId = outletId;
        this.routeId = routeId;
        this.outletType = outletType;
        this.outletClass = outletClass;
        this.outletName = outletName;
        this.outletCode = outletCode;
        this.address = address;
        this.ownerName = ownerName;
        this.contactLand = contactLand;
        this.frontImageURI = frontImageURI;
        this.showcaseImageUri = showcaseImageUri;
        this.outletHistory = outletHistory;
    }

    /*
    Call this constructor to create an outlet without an assistant.
     */
    public Outlet(int outletId, int routeId, String outletName, String address, String ownerName, String contactLand, String contactMobile, String frontImageURI, String showcaseImageUri, List<HistoryDetail> outletHistory) {
        this.outletId = outletId;
        this.routeId = routeId;
        this.outletName = outletName;
        this.address = address;
        this.ownerName = ownerName;
        this.contactLand = contactLand;
        this.frontImageURI = frontImageURI;
        this.showcaseImageUri = showcaseImageUri;
        this.outletHistory = outletHistory;
    }

    /**
     * Phase a JSONObject to Outlet Object
     *
     * @param instance
     * @return
     * @throws JSONException
     */
    public static Outlet parseOutlet(JSONObject instance) throws JSONException {

        if (instance != null) {
            Outlet outlet = new Outlet();
            outlet.setOutletId(instance.getInt("id_outlet"));
            outlet.setOutletName(instance.getString("retailer_name"));
            //outlet.setRouteId(instance.getInt("route_code")); --Thejan
            outlet.setAddress("N/A");
            outlet.setOwnerName(instance.getString("owner_name"));
            outlet.setContactLand("N/A");
            outlet.setOutletType(instance.getInt("id_outlet_type"));
            outlet.setOutletClass(instance.getInt("id_outlet_class"));
            outlet.setOutletCode(instance.getString("retailer_code"));

            JSONArray invoicesArray = instance.getJSONArray("invoices");

            List<HistoryDetail> historyDetails = new ArrayList<HistoryDetail>();

            for (int i = 0; i < invoicesArray.length(); i++) {
                Invoice invoice = Invoice.parseInvoice(invoicesArray.getJSONObject(i));
                HistoryDetail historyDetail = new HistoryDetail(outlet.getOutletId(), invoice, "", invoice.getInvoiceTime());
                historyDetails.add(historyDetail);
            }

            outlet.setOutletHistory(historyDetails);

            return outlet;
        }

        return null;
    }

    //Getters
    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactLand() {
        return contactLand;
    }

    public void setContactLand(String contactLand) {
        this.contactLand = contactLand;
    }

    public String getFrontImageURI() {
        return frontImageURI;
    }

    public void setFrontImageURI(String frontImageURI) {
        this.frontImageURI = frontImageURI;
    }

    public String getShowcaseImageUri() {
        return showcaseImageUri;
    }

    public void setShowcaseImageUri(String showcaseImageUri) {
        this.showcaseImageUri = showcaseImageUri;
    }

    public List<HistoryDetail> getOutletHistory() {
        return outletHistory;
    }

    public void setOutletHistory(List<HistoryDetail> outletHistory) {
        this.outletHistory = outletHistory;
    }

    public void setSolitaryOutletHistory(HistoryDetail history) {
        if (outletHistory == null) {
            outletHistory = new ArrayList<HistoryDetail>();
        }
        outletHistory.add(history);
    }

    public int getOutletType() {
        return outletType;
    }

    public void setOutletType(int outletType) {
        this.outletType = outletType;
    }

    public int getOutletClass() {
        return outletClass;
    }

    public void setOutletClass(int outletClass) {
        this.outletClass = outletClass;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    @Override
    public String toString() {
        return outletName;
    }

}

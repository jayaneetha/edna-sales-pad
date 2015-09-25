package com.colombosoft.ednasalespad.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Route implements Serializable {

    private int routeId;
    private String routeCode;
    private String routeName;
    private List<Outlet> outlets;
    private double fixedTarget, selectedTarget;

    /**
     * Deault Constructor
     */
    public Route() {
    }

    /**
     * Constructor
     *
     * @param routeId
     * @param routeCode
     * @param routeName
     * @param outlets
     */
    public Route(int routeId, String routeCode, String routeName, List<Outlet> outlets) {
        this.routeId = routeId;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.outlets = outlets;
    }

    /**
     * Constructor
     *
     * @param routeId
     * @param routeCode
     * @param routeName
     * @param fixedTarget
     * @param selectedTarget
     */
    public Route(int routeId, String routeCode, String routeName, double fixedTarget, double selectedTarget) {
        this.routeId = routeId;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.fixedTarget = fixedTarget;
        this.selectedTarget = selectedTarget;
    }

    /**
     * Constructor
     *
     * @param routeId
     * @param routeCode
     * @param routeName
     * @param outlets
     * @param fixedTarget
     * @param selectedTarget
     */
    public Route(int routeId, String routeCode, String routeName, List<Outlet> outlets, double fixedTarget, double selectedTarget) {
        this.routeId = routeId;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.outlets = outlets;
        this.fixedTarget = fixedTarget;
        this.selectedTarget = selectedTarget;
    }

    /**
     * Phase the JSONObject Route to a Route Object
     *
     * @param instance
     * @return
     * @throws JSONException
     */
    public static Route parseRoute(JSONObject instance) throws JSONException {

        List<Outlet> outlets = new ArrayList<Outlet>();
        Route route = new Route();
        route.setRouteId(instance.getInt("territory_id"));
        route.setRouteName(instance.getString("territory_name"));
        route.setRouteCode(instance.getString("territory_code").toUpperCase(Locale.getDefault()));

        JSONArray outletsArray = instance.getJSONArray("outlets");
        for (int i = 0; i < outletsArray.length(); i++) {
            Outlet outlet = Outlet.parseOutlet(outletsArray.getJSONObject(i));
            if (outlet != null) {
                outlets.add(outlet);
            }
        }

        route.setOutlets(outlets);

        return route;
    }

    //Getters
    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    public double getFixedTarget() {
        return fixedTarget;
    }

    public void setFixedTarget(double fixedTarget) {
        this.fixedTarget = fixedTarget;
    }

    public double getSelectedTarget() {
        return selectedTarget;
    }

    public void setSelectedTarget(double selectedTarget) {
        this.selectedTarget = selectedTarget;
    }

    @Override
    public String toString() {
        return routeName;
    }

}

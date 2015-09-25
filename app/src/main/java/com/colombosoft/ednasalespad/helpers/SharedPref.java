package com.colombosoft.ednasalespad.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.colombosoft.ednasalespad.model.Route;
import com.colombosoft.ednasalespad.model.User;

/**
 * Created by Admin on 9/20/15.
 */
public class SharedPref {

    private static SharedPref pref;
    private Context context;
    private SharedPreferences sharedPref;

    private SharedPref(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance(Context context) {
        if (pref == null) {
            pref = new SharedPref(context);
        }

        return pref;
    }

    /**
     * Return the Server URL
     *
     * @return
     */
    public String getServer() {
        String DEFAULT_URL = "http://gateway.ceylonlinux.com/edna_sfa2/android_service/";
        return sharedPref.getString("server_url", DEFAULT_URL);
    }

    /**
     * Store the Server URL
     *
     * @param url
     */
    public void setServer(String url) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("server_url", url);
    }

    /**
     * Set the logged in status
     *
     * @param status
     */
    public void setLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("login_status", status).apply();
    }

    /**
     * Checks the logged in status of the user.
     *
     * @return Boolean
     */
    public boolean isLoggedIn() {
        return sharedPref.getBoolean("login_status", false);
    }

    /**
     * Store the user object
     *
     * @param user
     */
    public void storeLoginUser(User user) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("user_id", user.getId());
        editor.putString("user_name", user.getName());
        editor.putString("user_username", user.getUsername());
        editor.putString("user_position", user.getPosition());
        editor.putString("user_territory", user.getTerritory());
        editor.putString("user_contact", user.getContact());
        editor.putString("user_image_uri", user.getImageURI());
        editor.putInt("user_type", user.getType());
        editor.putInt("user_location_id", user.getLocationId());
        editor.putInt("sales_type", user.getSalesType());
        editor.apply();
    }

    /**
     * Return the user object of the logged in user
     *
     * @return
     */
    public User getLoginUser() {

        User user = new User();
        user.setId(sharedPref.getInt("user_id", 0));
        user.setName(sharedPref.getString("user_name", null));
        user.setUsername(sharedPref.getString("user_username", null));
        user.setPosition(sharedPref.getString("user_position", null));
        user.setTerritory(sharedPref.getString("user_territory", null));
        user.setContact(sharedPref.getString("user_contact", null));
        user.setImageURI(sharedPref.getString("user_image_uri", null));
        user.setType(sharedPref.getInt("user_type", 0));
        user.setLocationId(sharedPref.getInt("user_location_id", 0));
        user.setSalesType(sharedPref.getInt("sales_type", 0));

        if (user.getId() == 0) {
            return null;
        } else {
            return user;
        }
    }


    public long generateOrderId(long time) {
        Log.wtf("ID", String.valueOf(sharedPref.getInt("user_id", 0)) + String.valueOf(time));
        long order_id = Long.parseLong(String.valueOf(sharedPref.getInt("user_id", 0)) + String.valueOf(time));
        return (order_id < 0 ? -order_id : order_id);
    }

    public void storeSelectedRoute(Route route) {
        if (route != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("selected_route_id", route.getRouteId());
            editor.putString("selected_route_code", route.getRouteCode());
            editor.putString("selected_route_name", route.getRouteName());
            editor.putFloat("selected_route_fixed_target", (float) route.getFixedTarget());
            editor.putFloat("selected_route_selected_target", (float) route.getSelectedTarget());
            editor.apply();
        }
    }

    public Route getSelectedRoute() {

        Route route = new Route(sharedPref.getInt("selected_route_id", 0),
                sharedPref.getString("selected_route_code", null),
                sharedPref.getString("selected_route_name", null),
                sharedPref.getFloat("selected_route_fixed_target", 0),
                sharedPref.getFloat("selected_route_selected_target", 0));
        if (route.getRouteId() != 0) {
            return route;
        }

        return null;
    }

    public void storePreviousRoute(Route route) {
        if (route != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("previous_route_id", route.getRouteId());
            editor.putString("previous_route_name", route.getRouteName());
            editor.putFloat("previous_route_fixed_target", (float) route.getFixedTarget());
            editor.putFloat("previous_route_selected_target", (float) route.getSelectedTarget());
            editor.apply();
        }
    }

//    public Route getPreviousRoute() {
//
//        Route route = new Route(sharedPref.getInt("previous_route_id", 0), sharedPref.getString("previous_route_name", null),
//                sharedPref.getFloat("previous_route_fixed_target", 0), sharedPref.getFloat("previous_route_selected_target", 0));
//        if (route.getRouteId() != 0) {
//            return route;
//        }
//
//        return null;
//    }

    public void clearPref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("login_status", false);
        storePreviousRoute(getSelectedRoute());
        editor.putInt("selected_route_id", 0);
        editor.putString("selected_route_name", "");
        editor.putFloat("selected_route_fixed_target", 0);
        editor.putFloat("selected_route_selected_target", 0);
        editor.apply();
    }

    public int getSelectedOutletId() {
        return sharedPref.getInt("selected_out_id", 0);
    }

    public void setSelectedOutletId(int outletId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("selected_out_id", outletId);
        editor.apply();
    }

    public int startDay() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("day_status", true);

        int session = sharedPref.getInt("local_session", 0) + 1;
        editor.putInt("local_session", session);

        editor.apply();

        return session;
    }

    public void endDay() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("day_status", false);
        editor.apply();
    }

    public int getLocalSessionId() {
        return sharedPref.getInt("local_session", 0);
    }

//    public void setDayStatus(boolean isDayStarted) {
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("day_status", isDayStarted);
//        editor.apply();
//    }

    public boolean isDayStarted() {
        return sharedPref.getBoolean("day_status", false);
    }

    public void setTransferToDealerList(boolean flag) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("transfer_to_dlist", flag);
        editor.apply();
    }

    public boolean getTransferToDealerList(boolean inverse) {

        boolean result = sharedPref.getBoolean("transfer_to_dlist", false);

        if (inverse) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("transfer_to_dlist", false);
            editor.apply();
        }

        return result;
    }

}

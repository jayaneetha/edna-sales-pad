package com.colombosoft.ednasalespad.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import com.colombosoft.ednasalespad.MyApplication;
import com.colombosoft.ednasalespad.model.CashPayment;
import com.colombosoft.ednasalespad.model.Cheque;
import com.colombosoft.ednasalespad.model.Order;
import com.colombosoft.ednasalespad.model.UnproductiveCall;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thahzan on 1/6/15.
 */
public class NetworkFunctions {

    private final String LOG_TAG = NetworkFunctions.class.getSimpleName();
    public String baseURL; //= "http://gateway.ceylonlinux.com/edna_sfa2/android_service/"; // Live server

    /**
     * The base URL to POST the parameters to. The function names will be appended to this
     */
//    private final String baseURL = "http://gateway.ceylonlinux.com/edna_sfa/android_service/"; // Test server

    public NetworkFunctions() {
        SharedPref pref = SharedPref.getInstance(MyApplication.getContext());
        baseURL = pref.getServer();
    }


//    Local servers
//    private final String baseURL = "http://192.168.1.117/edna_sfa2/android_service/";
//    private final String baseURL = "http://192.168.1.90/edna_sfa2/"
//    private final String baseURL = "http://192.168.1.90/edna_sfa2/";
//    private final String baseURL = "http://192.168.1.200/edna_sfa2/";

    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * This function will POST username and password and will return a the response JSON
     * from the server.
     *
     * @param username The string of the entered username
     * @param password The string of the entered password
     * @return The response as a String
     * @throws IOException Throws if unable to reach the server
     */
    public String authenticate(String username, String password) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

//        return postToServer(baseURL + "login", params); // Test Server
        Log.i(LOG_TAG, baseURL);
        return postToServer(baseURL + "user_login", params);

    }

    public String getRoutesAndOutlets(int locationId, int territoryId) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));
        params.add(new BasicNameValuePair("Id_territory", String.valueOf(territoryId)));

        Log.i("TERRITORY", String.valueOf(territoryId));

        return postToServer(baseURL + "getOutlet", params);
    }

    public String getItemCategories(int locationId) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));

        return postToServer(baseURL + "getItemDetails", params);
    }

    public String getStockDetails(int locationId, int repType) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_location_id", String.valueOf(locationId)));
        params.add(new BasicNameValuePair("user_type_id", String.valueOf(repType)));

        return postToServer(baseURL + "stock", params);
    }


    public String getBanks(int locationId) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_location_id", String.valueOf(locationId)));

        return getFromServer(baseURL + "getBank", params);
    }

    public String requestLoading(int locationId, JSONArray request, long requestTime) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));
        params.add(new BasicNameValuePair("jsonString", request.toString()));
        params.add(new BasicNameValuePair("time", String.valueOf(requestTime)));

        return postToServer(baseURL + "mobileRequested", params);
    }

    public String getProductTypes(int locationId) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));

        return postToServer(baseURL + "productType", params);
    }

    public String getFlavours(int locationId) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));

        return postToServer(baseURL + "productFlaver", params);
    }

    public String syncUnproductive(UnproductiveCall unproductiveCall, int locationId) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));
        params.add(new BasicNameValuePair("data", unproductiveCall.getUnproductiveCallAsJSON().toString()));

        return postToServer(baseURL + "unproductive", params);
    }

    public String getAddressOfLocation(Location location) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("latlng", String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude())));
        params.add(new BasicNameValuePair("sensor", "true"));

        return getFromServer("http://maps.googleapis.com/maps/api/geocode/json", params);
    }

    /**
     * @param location
     * @param task     either start attendance or end attendance "begin" or "end"
     * @return
     */
    public String setAttendance(Location location, String task) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
        params.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));
        params.add(new BasicNameValuePair("task", task));

        return postToServer(baseURL + "attendance", params);
    }

    /**
     * This function POSTs params to server and gets the response.
     *
     * @param url    The URL to POST to
     * @param params The List<NameValuePair></> of params to POST
     * @return The response from the server as a String
     * @throws IOException Throws if unable to connect to the server
     */
    private String postToServer(String url, List<NameValuePair> params) throws IOException {

        String response = "";

        URL postURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) postURL.openConnection();
        con.setConnectTimeout(10 * 1000);
        con.setReadTimeout(30 * 1000);
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        con.connect();

        int status = con.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                response = sb.toString();
                Log.i(LOG_TAG, "Server Response : \n" + response);
        }

        return response;
    }

    /**
     * This function GETs params to server and returns the response.
     *
     * @param url    The URL to GET from
     * @param params The List<NameValuePair></> of params to POST
     * @return The response string from the server
     * @throws IOException Throws if unable to connect to the server
     */
    private String getFromServer(String url, List<NameValuePair> params) throws IOException {

        String response = "";

        URL postURL = new URL(url + generateGETParams(params));
        Log.d(LOG_TAG, postURL.toString());
        HttpURLConnection con = (HttpURLConnection) postURL.openConnection();
        con.setConnectTimeout(10 * 1000);
        con.setReadTimeout(30 * 1000);
        con.setRequestMethod("GET");
        con.setDoInput(true);
        con.setDoOutput(true);

//        OutputStream os = con.getOutputStream();
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//        writer.write(getQuery(params));
//        writer.flush();
//        writer.close();
//        os.close();

        con.connect();

        int status = con.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                response = sb.toString();
                Log.d(LOG_TAG, "Server Response : \n" + response);
        }

        return response;
    }

    /**
     * This function will return the params as a queried String to POST to the server
     *
     * @param params The parameters to be POSTed
     * @return The formatted String
     * @throws UnsupportedEncodingException
     */
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (pair != null) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
        }
        Log.i("SERVER REQUEST", result.toString());
        return result.toString();
    }

    private String generateGETParams(List<NameValuePair> params) {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (pair != null) {
                if (first) {
                    first = false;
                    result.append("?");
                } else
                    result.append("&");

                result.append(pair.getName());
                result.append("=");
                result.append(pair.getValue());
            }
        }
        return result.toString();

    }

    public String syncOrder(Order order, int repId, int locationId, int salesType) throws IOException, JSONException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));
        params.add(new BasicNameValuePair("user_id", String.valueOf(repId)));
        params.add(new BasicNameValuePair("jsonString", order.getOrderAsJSON().toString()));
        params.add(new BasicNameValuePair("session_id", "0"));
        params.add(new BasicNameValuePair("user_status", "1"));
        params.add(new BasicNameValuePair("sales_type", String.valueOf(salesType)));

        return postToServer(baseURL + "insert_order", params);
    }

    public String syncPayments(List<CashPayment> cashPayments, List<Cheque> cheques, int repId, int locationId) throws IOException, JSONException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("position_id", String.valueOf(locationId)));
        params.add(new BasicNameValuePair("user_id", String.valueOf(repId)));
        params.add(new BasicNameValuePair("session_id", "0"));
        params.add(new BasicNameValuePair("user_status", "1"));

        JSONArray cashCheckPaymentJasonArray = new JSONArray();

        if (cashPayments != null) {
            for (CashPayment cashPayment : cashPayments) {
                cashCheckPaymentJasonArray.put(cashPayment.getCashPaymentAsJSON());
            }
        }

        if (cheques != null) {
            for (Cheque cheque : cheques) {
                cashCheckPaymentJasonArray.put(cheque.getChequePaymentAsJSON());
            }
        }

        params.add(new BasicNameValuePair("cash_check_payment", cashCheckPaymentJasonArray.toString()));

        String jsonSring = cashCheckPaymentJasonArray.toString();
        Log.i("PAYMENT SYNC", jsonSring);

        return postToServer(baseURL + "save_payments", params);
    }

    public Bitmap downloadImage(String fileName) {
        String image_url = (new StringBuilder()).append("http://124.43.26.21/edna_sfa2/uploads/product_images/").append(fileName).toString();
        Bitmap bitmap = null;
        Log.i(LOG_TAG, "Fetching: " + image_url);
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(image_url).openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());

            bitmap = BitmapFactory.decodeStream((InputStream) bufferedInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return bitmap;
        }
    }

}

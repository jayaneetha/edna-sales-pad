package com.colombosoft.ednasalespad;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.adapter.NavDrawerExpandableListAdapter;
import com.colombosoft.ednasalespad.dialog.CustomProgressDialog;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.NetworkFunctions;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.libs.widget.AnimatedExpandableListView;
import com.colombosoft.ednasalespad.libs.widget.CircularImageView;
import com.colombosoft.ednasalespad.model.CashPayment;
import com.colombosoft.ednasalespad.model.Cheque;
import com.colombosoft.ednasalespad.model.ChildItem;
import com.colombosoft.ednasalespad.model.GroupItem;
import com.colombosoft.ednasalespad.model.Order;
import com.colombosoft.ednasalespad.model.Stock;
import com.colombosoft.ednasalespad.model.User;
import com.colombosoft.ednasalespad.utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends ActionBarActivity {

    private static final String LOG_TAG = DashboardActivity.class.getSimpleName();

    private User user;

    private SharedPref pref;
    private DatabaseHandler dbHandler;
    private NetworkFunctions networkFunctions;

    private AnimatedExpandableListView drawerList;
    private NavDrawerExpandableListAdapter navDrawerExpandableListAdapter;
    private List<GroupItem> navDrawerItemList;

    private DrawerLayout drawerLayout;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        // Establishing DB and SharedPreferences connection
        dbHandler = DatabaseHandler.getDbHandler(DashboardActivity.this);
        pref = SharedPref.getInstance(DashboardActivity.this);
        networkFunctions = new NetworkFunctions();

        user = dbHandler.getUser();

        // Defining the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.dashboard_nav_drawer_layout);
        // Setting the color the status bar should appear (behind the sliding menu)
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.bg_color));

        // Defining the animated expandable ListView in the sliding menu
        drawerList = (AnimatedExpandableListView) findViewById(R.id.dashboard_left_drawer);

//        Drawable icon = getResources().getDrawable(R.drawable.plus);

        // Creating items and sub items to add to the sliding menu ListView
        navDrawerItemList = new ArrayList<GroupItem>();

        Resources resources = getResources();

        navDrawerItemList.add(new GroupItem(1, resources.getDrawable(R.drawable.alarm_clock), "Mark Attendance"));
        navDrawerItemList.add(new GroupItem(2, resources.getDrawable(R.drawable.positive_dynamic), "Dashboard"));
        navDrawerItemList.add(new GroupItem(3, resources.getDrawable(R.drawable.truck), "Pick Route"));

        // Should only show this option for mobile stock users. Comment out the if condition when necessary.
//        if(user.getType() == User.TYPE_MOBILE) {
        navDrawerItemList.add(new GroupItem(4, resources.getDrawable(R.drawable.luggage_trolley), "Request Loading"));
//        }

        navDrawerItemList.add(new GroupItem(5, resources.getDrawable(R.drawable.apartment), "Dealer List"));
        navDrawerItemList.add(new GroupItem(6, resources.getDrawable(R.drawable.google_alerts), "Messages"));
        navDrawerItemList.add(new GroupItem(7, resources.getDrawable(R.drawable.sinchronize), "Synchronize"));

        List<ChildItem> childList1 = new ArrayList<ChildItem>();
        childList1.add(new ChildItem(1, "Route target by SKU", "Plan target by SKU"));
        childList1.add(new ChildItem(2, "Day Planner", "Plan day wise"));
        childList1.add(new ChildItem(3, "Day-Route-SKU", "Plan by Day-Route-SKU"));
        navDrawerItemList.add(new GroupItem(8, resources.getDrawable(R.drawable.calendar), "Target Planning", childList1));

        List<ChildItem> childList4 = new ArrayList<ChildItem>();
        childList4.add(new ChildItem(1, "Stock Balance", "View balance stock"));
        navDrawerItemList.add(new GroupItem(9, resources.getDrawable(R.drawable.area_chart), "Reports", childList4));

        navDrawerItemList.add(new GroupItem(10, resources.getDrawable(R.drawable.expensive_2), "Expenses"));
        navDrawerItemList.add(new GroupItem(11, resources.getDrawable(R.drawable.cash_receiving), "Incentives/Earnings"));
        navDrawerItemList.add(new GroupItem(12, resources.getDrawable(R.drawable.magazine), "Product Catalogues"));

        List<ChildItem> childList6 = new ArrayList<ChildItem>();
        childList6.add(new ChildItem(1, "Database", "View Database"));
        navDrawerItemList.add(new GroupItem(13, null, "Others", childList6));

        navDrawerItemList.add(new GroupItem(14, resources.getDrawable(R.drawable.logout), "Sign Out"));

        navDrawerItemList.add(new GroupItem(15, resources.getDrawable(R.drawable.logout), "Update Profile"));

        // Define the navDrawerExpandableListAdapter for the animated expandable ListView
        navDrawerExpandableListAdapter = new NavDrawerExpandableListAdapter(DashboardActivity.this, navDrawerItemList);
        drawerList.setAdapter(navDrawerExpandableListAdapter);

        // Expand/Collapse the clicked group in the expandable ListView
        drawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (navDrawerItemList.get(groupPosition).isExpandable()) {
                    if (drawerList.isGroupExpanded(groupPosition)) {
                        drawerList.collapseGroupWithAnimation(groupPosition);
                        navDrawerItemList.get(groupPosition).setExpanded(false);
                    } else {
                        drawerList.expandGroupWithAnimation(groupPosition);
                        navDrawerItemList.get(groupPosition).setExpanded(true);
                    }
                    navDrawerExpandableListAdapter.notifyDataSetChanged();
                } else {
                    drawerSelectionHandler(navDrawerItemList.get(groupPosition).getIndex());
                }

                return true;
            }
        });

        // Pass the index to the appropriate function to handel the click event
        drawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                drawerSelectionHandler(Integer.parseInt(String.valueOf(navDrawerItemList.get(groupPosition).getIndex())
                        + String.valueOf(navDrawerItemList.get(groupPosition).getChildren().get(childPosition).getIndex())));
//                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Attaching the toolbar as the support action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("Edna Salespad");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ednatheme_ic_navigation_drawer));

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(DashboardActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        TextView profileName = (TextView) findViewById(R.id.dashboard_profile_name);
        CircularImageView profileImage = (CircularImageView) findViewById(R.id.dashboard_profile_circular_imageview);

        profileName.setText(user.getName().toUpperCase(Locale.getDefault()));


    }

    private void drawerSelectionHandler(int index) {

        Log.d(LOG_TAG, String.valueOf(index));

        if (index == 14) {
//            AlertDialog dialog = new AlertDialog.Builder(DashboardActivity.this)
//                    .setMessage("")
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            pref.clearPref();
//                            Intent intent = new Intent(DashboardActivity.this, UnlockActivity.class);
//                            intent.putExtra("user", pref.getLoginUser());
//                            startActivity(intent);
//                            finish();
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .create();
//            dialog.show();
        } else {
            if (!pref.isDayStarted()) {
                if (index != 1) {
                    Toast.makeText(DashboardActivity.this, "Please mark attendance to start the day", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(DashboardActivity.this, MarkAttendanceActivity.class));
            } else {
                switch (index) {
                    case 1:
                        // Navigation menu : Mark Attendance
                        startActivity(new Intent(DashboardActivity.this, MarkAttendanceActivity.class));
                        finish();
                        break;
                    case 2:
                        // Navigation menu : Dashboard
                        break;
                    case 3:
                        // Navigation menu : Pick Route
                        startActivity(new Intent(DashboardActivity.this, ViewRoutesActivity.class));
                        break;
                    case 4:
                        // Navigation menu : Request Loading
                        if (NetworkUtil.isNetworkAvailable(DashboardActivity.this)) {
                            //startActivity(new Intent(DashboardActivity.this, RequestLoadingActivity.class));
                        } else {
                            Toast.makeText(DashboardActivity.this, "Please turn on Mobile Data or Wi-Fi", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 5:
                        // Navigation menu : Dealer List
                        startActivity(new Intent(DashboardActivity.this, SelectDealerActivity.class));
                        //finish();
                        break;
                    case 6:
                        // Navigation menu : Messages
                        break;
                    case 7:
                        // Navigation menu : Synchronize
                        if (dbHandler.getUnsyncedOrderCount() == 0 && dbHandler.getUnsyncedCashPayments().size() == 0 && dbHandler.getUnsyncedChequePayments().size() == 0) {
                            Toast.makeText(DashboardActivity.this, "No data to sync with server", Toast.LENGTH_SHORT).show();
                        } else {
                            if (NetworkUtil.isNetworkAvailable(DashboardActivity.this)) {
                                new Sync().execute();
                            } else {
                                Toast.makeText(DashboardActivity.this, "Please turn on Mobile Data or Wi-Fi", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case 81:
                        // Navigation menu : Target Planner : Route Target by SKU
                        break;
                    case 82:
                        // Navigation menu : Target Planner : Day Planner
                        break;
                    case 83:
                        // Navigation menu : Target Planner : Day - Route - SKU
                        break;
                    case 91:
                        // Navigation menu : Reports : Stock Balance
                        break;
                    case 10:
                        // Navigation menu : Expenses
                        break;
                    case 11:
                        // Navigation menu : Incentives/Earnings
                        break;
                    case 12:
                        // Navigation menu : Product Catalogue
                        break;
                    case 131:
                        // Navigation menu : Others : Database
                        //startActivity(new Intent(DashboardActivity.this, ViewDatabaseActivity.class));
                        break;
                    case 15:
                        startActivity(new Intent(DashboardActivity.this, UserProfileActivity.class));
                        finish();
                        break;
                    default:
                        // Invalid argument. Most likely an un-tracked entry to the animated expandable ListView.
                        Toast.makeText(DashboardActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        }, 700);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            // Left drawer is open. Close it
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - backPressedTime > 2000) {
                // More than 2 seconds since last back press. So user must press back again to exit
                Toast.makeText(DashboardActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                backPressedTime = currentTime;
            } else {
                // Less than 2 seconds since last back press meaning a double back press.
                // In which case app should close.
                System.exit(0);
            }
        }
    }

    private class Sync extends AsyncTask<Void, Void, Boolean> {

        private CustomProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new CustomProgressDialog(DashboardActivity.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setMessage("Synchronizing...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean finalResult = true;

            List<Order> orders = dbHandler.getUnsyncedOrders();
            String responseString = null;
            if(orders != null) {
                for (Order order : orders) {
                    try {
                        responseString = networkFunctions.syncOrder(order, user.getId(), user.getLocationId(), user.getSalesType());
                        JSONObject responseJSON = new JSONObject(responseString);
                        if (responseJSON.getBoolean("result")) {
                            dbHandler.setOrderAsSynced(order.getOrderId());
                        }

                        String distStock = networkFunctions.getStockDetails(user.getLocationId(), user.getType());
                        JSONArray distStockJSONArray = new JSONArray(distStock);
                        List<Stock> distStocks = new ArrayList<Stock>();
                        for (int i = 0; i < distStockJSONArray.length(); i++) {
                            Stock stock = Stock.parseStock(distStockJSONArray.getJSONObject(i));
                            if (stock != null) {
                                distStocks.add(stock);
                            }
                        }
                        dbHandler.updateStockDetails(distStocks);

                    } catch (IOException e) {
                        e.printStackTrace();
                        finalResult = false;
                    } catch (JSONException e) {
                        if (responseString != null && responseString.contains("{\"result\":true}")) {
                            dbHandler.setOrderAsSynced(order.getOrderId());
                        } else {
                            finalResult = false;
                        }
                        e.printStackTrace();
                    }
                }
            }

            List<CashPayment> cashPayments = dbHandler.getUnsyncedCashPayments();
            List<Cheque> cheques = dbHandler.getUnsyncedChequePayments();

            String response = "";
            try {
                response = networkFunctions.syncPayments(cashPayments, cheques, pref.getLoginUser().getId(), pref.getLoginUser().getLocationId());
                JSONObject paymentResponseJSON = new JSONObject(response);

                if(paymentResponseJSON.getBoolean("result")){
                    if(cashPayments != null) dbHandler.setCashPaymentAsSynced(cashPayments);
                    if(cheques != null) dbHandler.setChequePaymentAsSynced(cheques);
                }

            } catch (IOException e) {
                e.printStackTrace();
                finalResult = false;
            } catch (JSONException e) {
                if (responseString != null && responseString.contains("{\"result\":true}")) {
                    if(cashPayments != null) dbHandler.setCashPaymentAsSynced(cashPayments);
                    if(cheques != null) dbHandler.setChequePaymentAsSynced(cheques);
                } else {
                    finalResult = false;
                }
                e.printStackTrace();
            }

//            return dbHandler.getUnsyncedOrderCount() == 0 && dbHandler.getUnsyncedCashPayments().size() == 0 && dbHandler.getUnsyncedChequePayments().size() == 0;
            return finalResult;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (aBoolean) {
                Toast.makeText(DashboardActivity.this, "Successfully synced with the server", Toast.LENGTH_SHORT).show();
            } else {
                if(dbHandler.getUnsyncedOrderCount() > 0) {
                    Toast.makeText(DashboardActivity.this, "Some orders weren't synced", Toast.LENGTH_SHORT).show();
                } else if (dbHandler.getUnsyncedChequePayments().size() > 0 || dbHandler.getUnsyncedCashPayments().size() > 0) {
                    Toast.makeText(DashboardActivity.this, "Some payments weren't synced", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Uncaught exception", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}

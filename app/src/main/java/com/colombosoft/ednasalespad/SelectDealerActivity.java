package com.colombosoft.ednasalespad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.adapter.OutletExpandableAdapter;
import com.colombosoft.ednasalespad.dialog.CustomKeypadDialog;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.libs.widget.AnimatedExpandableListView;
import com.colombosoft.ednasalespad.model.Outlet;
import com.colombosoft.ednasalespad.model.Route;
import com.colombosoft.ednasalespad.model.VisitDetail;
import com.colombosoft.ednasalespad.utils.RequestCodes;
import com.melnykov.fab.FloatingActionButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectDealerActivity extends ActionBarActivity {

    private DatabaseHandler dbHandler;
    private SharedPref pref;

    private AnimatedExpandableListView listView;
    private OutletExpandableAdapter adapter;
    private TextView tvRoute;
    private TextView tvFixedTarget;
    private TextView tvSelectedTarget;
    private FloatingActionButton fabAddOutlet;

    private Route route;
    private List<Outlet> outletList;
    private List<VisitDetail> visitDetails;

    private NumberFormat numberFormat;

    private boolean canLoadOutlets = false;
    private boolean startingSequence;

    private double selectedTarget = 0;
    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dealer);

        dbHandler = DatabaseHandler.getDbHandler(SelectDealerActivity.this);
        pref = SharedPref.getInstance(SelectDealerActivity.this);

        visitDetails = dbHandler.getVisitsOfCurrentSession(pref.getLocalSessionId());

        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        startingSequence = getIntent().getBooleanExtra(RequestCodes.KEY_STARTING_SEQUENCE, false);

        TextView tvDate = (TextView) findViewById(R.id.view_outlets_tv_date_view);
        tvRoute = (TextView) findViewById(R.id.view_outlets_tv_route_view);
        tvFixedTarget = (TextView) findViewById(R.id.view_outlets_tv_fixed_target_view);
        tvSelectedTarget = (TextView) findViewById(R.id.view_outlets_tv_selected_target_view);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final String formattedDate = sdf.format(calendar.getTime());

        tvDate.setText(formattedDate);

        int temporaryRouteId = getIntent().getIntExtra(RequestCodes.KEY_TEMPORARY_ROUTE, 0);

        if (temporaryRouteId == 0) {
            // Continuing in the default route
            route = pref.getSelectedRoute();
            if (route == null) {
                Toast.makeText(SelectDealerActivity.this, "Please select a route", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectDealerActivity.this, ViewRoutesActivity.class);
                intent.putExtra("request_code", RequestCodes.VIEW_OUTLETS);
                startActivity(intent);
                finish();
            } else {
                // Route already selected. So loading today's route
                tvRoute.setText(route.getRouteName());
                tvFixedTarget.setText(numberFormat.format(route.getFixedTarget()));
                tvSelectedTarget.setText(numberFormat.format(route.getSelectedTarget()));
                canLoadOutlets = true;
            }
        } else {
            // Continuing in the selected temporary route
            route = dbHandler.getRouteOfId(temporaryRouteId);
            tvRoute.setText(route.getRouteName());
            tvFixedTarget.setText(numberFormat.format(route.getFixedTarget()));
            tvSelectedTarget.setText(numberFormat.format(route.getSelectedTarget()));
            canLoadOutlets = true;
        }

        if (route == null) {
            Toast.makeText(SelectDealerActivity.this, "System encountered an error", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_outlets_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("SELECT DEALER");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = (AnimatedExpandableListView) findViewById(R.id.view_outlets_listview);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }

                return true;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Outlet selectedOutlet = outletList.get(groupPosition);
                Intent intent = new Intent(SelectDealerActivity.this, DealerDetailsActivity.class);
                intent.putExtra("outlet", selectedOutlet);
                pref.setSelectedOutletId(selectedOutlet.getOutletId());
                startActivity(intent);
                finish();
//                Toast.makeText(SelectDealerActivity.this, selectedOutlet.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        if (canLoadOutlets) {
            new loadOutlets().execute(route);
        }

        tvRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectDealerActivity.this, "Long press to change route", Toast.LENGTH_SHORT).show();
            }
        });

        tvRoute.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(SelectDealerActivity.this, ViewRoutesActivity.class);
                intent.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
                startActivity(intent);
                finish();
                return false;
            }
        });

        tvSelectedTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectDealerActivity.this, "Long press to select new target", Toast.LENGTH_SHORT).show();
            }
        });

        tvSelectedTarget.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                CustomKeypadDialog keypad = new CustomKeypadDialog(SelectDealerActivity.this, false, new CustomKeypadDialog.IOnOkClickListener() {
                    @Override
                    public void okClicked(double value) {
                        selectedTarget = value;
                        tvSelectedTarget.setText(numberFormat.format(selectedTarget));
                        route.setSelectedTarget(selectedTarget);
                        dbHandler.updateRoute(route);
                    }
                });

                keypad.show();
                keypad.setHeader("Select New Target");
                keypad.loadValue(selectedTarget);
                return true;
            }
        });

        fabAddOutlet = (FloatingActionButton) findViewById(R.id.view_outlets_fab_add_dealer);
        fabAddOutlet.attachToListView(listView);

        fabAddOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectDealerActivity.this, "Add New Dealer", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        if (currentTime - backPressedTime > 2000) {
            // More than 2 seconds since last back press. So user must press back again to exit
            Toast.makeText(SelectDealerActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressedTime = currentTime;
        } else {
            // Less than 2 seconds since last back press meaning a double back press.
            // In which case app should close.
            finish();
        }

    }

    private class loadOutlets extends AsyncTask<Route, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listView.setAdapter(adapter);

        }

        @Override
        protected Void doInBackground(Route... params) {
            Route mRoute = params[0];

            if (mRoute != null) {
                selectedTarget = mRoute.getSelectedTarget();
                outletList = dbHandler.getOutletsOfRoute(mRoute.getRouteId());
                if (outletList != null) {
                    if (adapter == null) {
                        adapter = new OutletExpandableAdapter(SelectDealerActivity.this, outletList, visitDetails);
//                        listView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(SelectDealerActivity.this, "No outlets available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SelectDealerActivity.this, "Invalid route. Cannot load outlets", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }


}

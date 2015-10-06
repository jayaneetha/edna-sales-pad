package com.colombosoft.ednasalespad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.dialog.CustomProgressDialog;
import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.Route;
import com.colombosoft.ednasalespad.utils.RequestCodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewRoutesActivity extends ActionBarActivity {

    private static final String LOG_TAG = ViewRoutesActivity.class.getSimpleName();
    final Context context = this;
    ListView routes;
    private SharedPref pref;
    private int requestCode = 0;
    private boolean startingSequence = false;
    private List<Route> routeList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routes);

        Intent entry = getIntent();
        requestCode = entry.getIntExtra("request_code", 0);

        startingSequence = entry.getBooleanExtra(RequestCodes.KEY_STARTING_SEQUENCE, false);

        DatabaseHandler dbHandler = DatabaseHandler.getDbHandler(ViewRoutesActivity.this);
        pref = SharedPref.getInstance(ViewRoutesActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_routes_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText("ROUTES");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        routes = (ListView) findViewById(R.id.view_routes_listview);

        new GetRoutes().execute();

        // Set the on click event.
        routes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Route selectedRoute = routeList.get(position);

                if (startingSequence) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_confirm);
                    dialog.setTitle("Confirm");

                    TextView message_text = (TextView) dialog.findViewById(R.id.message_text);
                    message_text.setText("Do you really want to select the route " + selectedRoute.getRouteName() + " ?");
                    Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pref.storeSelectedRoute(selectedRoute);
                            dialog.dismiss();
                            startActivity(new Intent(ViewRoutesActivity.this, SelectDealerActivity.class).putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true));
                            Toast.makeText(ViewRoutesActivity.this, "Select Dealer Activity", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } else {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_confirm);
                    dialog.setTitle("Confirm");

                    TextView message_text = (TextView) dialog.findViewById(R.id.message_text);
                    message_text.setText("Do you really want to switch to the route " + selectedRoute.getRouteName() + " ?");
                    Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pref.storeSelectedRoute(selectedRoute);
                            dialog.dismiss();
                            startActivity(new Intent(ViewRoutesActivity.this, SelectDealerActivity.class).putExtra(RequestCodes.KEY_TEMPORARY_ROUTE, selectedRoute.getRouteId()));
                            Toast.makeText(ViewRoutesActivity.this, "Select Dealer Activity", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

//                        AlertDialog alert = new AlertDialog.Builder(ViewRoutesActivity.this)
//                                .setMessage("Are you sure you want to switch to " + selectedRoute.getRouteName() + " temporarily?")
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
////                                        startActivity(new Intent(ViewRoutesActivity.this, SelectDealerActivity.class).putExtra(RequestCodes.KEY_TEMPORARY_ROUTE, selectedRoute.getRouteId()));
//                                        Toast.makeText(ViewRoutesActivity.this, "Select Dealer Activity", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//                                })
//                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .create();
//                        alert.show();
                }
            }
        });

    }

    private class GetRoutes extends AsyncTask<Void, Void, List<Route>> {

        private CustomProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(ViewRoutesActivity.this, "Getting Data Please wait", Toast.LENGTH_SHORT).show();
            pDialog = new CustomProgressDialog(ViewRoutesActivity.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setMessage("Loading Routes...");
            pDialog.show();
        }

        @Override
        protected List doInBackground(Void... params) {
            DatabaseHandler databaseHandler = DatabaseHandler.getDbHandler(ViewRoutesActivity.this);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pDialog.setMessage("Loading Routes...");
                }
            });

            List<Route> list = databaseHandler.getRoutes();

            Collections.sort(list, new Comparator<Route>() {
                @Override
                public int compare(Route lhs, Route rhs) {
                    return lhs.getRouteName().compareToIgnoreCase(rhs.getRouteName());
                }
            });

            return list;
        }

        @Override
        protected void onPostExecute(List<Route> list) {
            super.onPostExecute(list);

            String[] Stringlist = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Stringlist[i] = list.get(i).getRouteName();
            }

            // Define the array adapter to feed the route names to the listview
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewRoutesActivity.this, android.R.layout.simple_list_item_1, Stringlist);
            routes.setAdapter(adapter);

            routeList = list;

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

        }
    }
}


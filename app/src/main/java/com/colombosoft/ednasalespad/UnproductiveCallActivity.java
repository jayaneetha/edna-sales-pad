package com.colombosoft.ednasalespad;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.libs.progress.ProgressWheel;
import com.colombosoft.ednasalespad.model.UnproductiveCall;

public class UnproductiveCallActivity extends ActionBarActivity {

    private final String LOG_TAG = UnproductiveCallActivity.class.getSimpleName();
    Spinner reason;
    EditText remarks;
    TextView tvProgress;
    Button add;
    UnproductiveCall unproductiveCall;
    int outletId;
    DatabaseHandler dbHandler;
    SharedPref sharedPref;
    LocationManager locationManager;
    Boolean gpsActive, networkActive;
    Location finalLocation = null;
    UnproductiveCallLocationListener locationListener;
    ProgressWheel progressWheel;
    private boolean locSwitch;
    private Handler locationHandler;
    private Runnable switchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unproductive_call);


        dbHandler = DatabaseHandler.getDbHandler(UnproductiveCallActivity.this);
        sharedPref = SharedPref.getInstance(UnproductiveCallActivity.this);

        outletId = sharedPref.getSelectedOutletId();

        locationManager = (LocationManager) UnproductiveCallActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new UnproductiveCallLocationListener();
        locationHandler = new Handler();

        try {
            gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            gpsActive = false;
        }
        try {
            networkActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            networkActive = false;
        }


        reason = (Spinner) findViewById(R.id.sp_unproductive_call_reason);
        remarks = (EditText) findViewById(R.id.et_unproductive_call_remarks);
        add = (Button) findViewById(R.id.bt_unproductive_call_add);
        progressWheel = (ProgressWheel) findViewById(R.id.mark_attendance_progress_location);
        tvProgress = (TextView) findViewById(R.id.mark_attendance_tv_progress);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unproductive_call_reasons, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        reason.setAdapter(adapter);
        requestUpdates();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reason.getSelectedItemPosition() != 0) {
                    if (finalLocation != null) {
                        String remark = remarks.getText().toString();
                        String reason_str = (String) reason.getSelectedItem();
                        int batteryLevel = getBatteryLevel();
                        long now = System.currentTimeMillis() / 1000L;
                        unproductiveCall = new UnproductiveCall(now, reason_str, remark, outletId, finalLocation.getLatitude(), finalLocation.getLongitude(), batteryLevel);
                        dbHandler.storeUnproductiveCall(unproductiveCall);
                        onBackPressed();
                    } else {
                        Toast.makeText(UnproductiveCallActivity.this, "Please wait. Application is still fetching the location", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UnproductiveCallActivity.this, "Please Select a Reason", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level / (float) scale) * 100;
        return (int) batteryPct;
    }

    private void requestUpdates() {
        try {
            gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsActive = false;
        }

        try {
            networkActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            networkActive = false;
        }

        if (networkActive) {
            if (gpsActive) {
                // Request timed location update using GPS
                requestGPSLocation();

                if (switchRunnable == null) {
                    switchRunnable = new Runnable() {
                        @Override
                        public void run() {
                            locationManager.removeUpdates(locationListener);
                            requestNetworkLocation();
                            locSwitch = true;
                        }
                    };
                }

                // Stop getting the location from GPS  and get from network if taking too long
                locationHandler.postDelayed(switchRunnable, 10 * 1000);
            } else {
                // Request location update using network
                requestNetworkLocation();
            }
        } else {
            if (gpsActive) {
                // Only GPS active. Request location from GPS provider
                requestGPSLocation();
            } else {
                Toast.makeText(UnproductiveCallActivity.this, "Please enable location service and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestGPSLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        tvProgress.setVisibility(View.VISIBLE);
        tvProgress.setText("Getting location (GPS)");
        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();
    }

    private void requestNetworkLocation() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        tvProgress.setVisibility(View.VISIBLE);
        tvProgress.setText("Getting location (Network)");
        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();
    }

    @Override
    public void onBackPressed() {
        if (switchRunnable != null && !locSwitch) {
            locationHandler.removeCallbacks(switchRunnable);
        }
        super.onBackPressed();
    }

    private class UnproductiveCallLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            finalLocation = location;
            locationManager.removeUpdates(this);
            progressWheel.stopSpinning();
            tvProgress.setText("Location: " + location.getLatitude() + ", " + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            if (finalLocation == null) {
                Toast.makeText(UnproductiveCallActivity.this, "Provider enabled. Accessing location", Toast.LENGTH_SHORT).show();
                tvProgress.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.spin();
                requestUpdates();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(UnproductiveCallActivity.this, "Provider disabled", Toast.LENGTH_SHORT).show();
            tvProgress.setVisibility(View.INVISIBLE);
            progressWheel.stopSpinning();
        }
    }
}

package com.colombosoft.ednasalespad;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.NetworkFunctions;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.libs.progress.ProgressWheel;
import com.colombosoft.ednasalespad.model.Attendance;
import com.colombosoft.ednasalespad.utils.NetworkUtil;
import com.colombosoft.ednasalespad.utils.RequestCodes;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.markushi.ui.CircleButton;

public class MarkAttendanceActivity extends ActionBarActivity {

    private static Resources resource;
    private final String LOG_TAG = MarkAttendanceActivity.class.getSimpleName();
    private Location finalLocation;
    private LocationManager locationManager;
    private ProgressWheel progressWheel;
    private TextView tvProgress;
    private DatabaseHandler dbHandler;
    private SharedPref sharedPref;
    private boolean openSequence = false;
    private boolean isDayStarted;
    private TextView tvTime;
    private TextView tvLocationAddressBegin, tvLocationCoordinatesBegin, tvCapturedTimeBegin;
    private Button tvBeginHeader, tvEndHeader;
    private CircleButton rndConfirmBegin;
    private TextView tvLocationAddressEnd, tvLocationCoordinatesEnd, tvCapturedTimeEnd;
    private CircleButton rndConfirmEnd;
    private Runnable countRunnable;
    private SimpleDateFormat dateFormat;
    private String formattedDate;
    private long capturedTime;
    private boolean gpsActive, networkActive;
    private AttendanceLocationListener locationListener;
    private Handler locationHandler;
    private Runnable switchRunnable;
    private boolean locSwitch;
    private NetworkFunctions networkFunctions;
    private boolean dayStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        dbHandler = DatabaseHandler.getDbHandler(MarkAttendanceActivity.this);
        sharedPref = SharedPref.getInstance(MarkAttendanceActivity.this);
        networkFunctions = new NetworkFunctions();

        isDayStarted = sharedPref.isDayStarted();
        int sessionId = sharedPref.getLocalSessionId();
        Attendance attendance = dbHandler.getAttendanceOfCurrentSession(sessionId);

        TextView tvDate = (TextView) findViewById(R.id.mark_attendance_tv_date);
        tvTime = (TextView) findViewById(R.id.mark_attendance_tv_clock);

        tvBeginHeader = (Button) findViewById(R.id.mark_attendance_tv_begin_header);
        tvEndHeader = (Button) findViewById(R.id.mark_attendance_tv_end_header);
        tvCapturedTimeBegin = (TextView) findViewById(R.id.mark_attendance_tv_begin_time);
        tvLocationAddressBegin = (TextView) findViewById(R.id.mark_attendance_tv_begin_location);
        tvLocationCoordinatesBegin = (TextView) findViewById(R.id.mark_attendance_tv_begin_location_coordinates);
        rndConfirmBegin = (CircleButton) findViewById(R.id.mark_attendance_crclbtn_start_function);

        tvCapturedTimeEnd = (TextView) findViewById(R.id.mark_attendance_tv_end_time);
        tvLocationAddressEnd = (TextView) findViewById(R.id.mark_attendance_tv_end_location);
        tvLocationCoordinatesEnd = (TextView) findViewById(R.id.mark_attendance_tv_end_location_coordinates);
        rndConfirmEnd = (CircleButton) findViewById(R.id.mark_attendance_crclbtn_end_function);

        rndConfirmBegin.setScaleX(0);
        rndConfirmBegin.setScaleY(0);

        rndConfirmEnd.setScaleX(0);
        rndConfirmEnd.setScaleY(0);

        rndConfirmBegin.setEnabled(false);
        rndConfirmEnd.setEnabled(false);

        rndConfirmBegin.setVisibility(View.VISIBLE);
        rndConfirmEnd.setVisibility(View.VISIBLE);

        dateFormat = new SimpleDateFormat("hh:mm:ss aaa", Locale.getDefault());

        tvEndHeader.setEnabled(false);

        if (attendance != null && isDayStarted) {
            tvEndHeader.setEnabled(true);
            tvBeginHeader.setEnabled(false);
            tvCapturedTimeBegin.setText(dateFormat.format(attendance.getLogTime()));
            tvLocationAddressBegin.setText(attendance.getLoc());
            tvLocationCoordinatesBegin.setText(String.valueOf(attendance.getLatitude()) + ", " + String.valueOf(attendance.getLongitude()));

        } else {
            tvCapturedTimeBegin.setText("");
            tvLocationAddressBegin.setText("");
            tvLocationCoordinatesBegin.setText("");
        }

        resource = getResources();
        tvBeginHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUpdates();

            }
        });
        tvEndHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUpdates();
            }
        });

        tvDate.setText(new SimpleDateFormat("yyyy MMM dd", Locale.getDefault()).format(new Date(System.currentTimeMillis())));

        Thread timeThread;
        countRunnable = new Runnable() {
            @Override
            public void run() {
                formattedDate = dateFormat.format(new Date(System.currentTimeMillis()));
                tvTime.setText(formattedDate);
            }
        };

        Runnable finalRunnable = new TimeRunner();
        timeThread = new Thread(finalRunnable);
        timeThread.start();

//        isDayStarted = sharedPref.isDayStarted();

        openSequence = getIntent().getBooleanExtra(RequestCodes.KEY_STARTING_SEQUENCE, false);

//        btnAdd = (CircleButton)findViewById(R.id.mark_attendance_crclbtn_function);
//        btnAdd.setScaleX(0);
//        btnAdd.setScaleY(0);
//        ViewPropertyAnimator.animate(btnAdd).scaleX(0).scaleY(0).setDuration(10).start();
//        btnAdd.setVisibility(View.VISIBLE);

//        tvFunction = (TextView)findViewById(R.id.mark_attendance_tv_function);

        progressWheel = (ProgressWheel) findViewById(R.id.mark_attendance_progress_location);
        tvProgress = (TextView) findViewById(R.id.mark_attendance_tv_progress);

        locationManager = (LocationManager) MarkAttendanceActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new AttendanceLocationListener();

        try {
            gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsActive = false;
        }

//        boolean networkActive;
        try {
            networkActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            networkActive = false;
        }

        rndConfirmBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sessionId = sharedPref.startDay();
                Attendance attendance = new Attendance(capturedTime, Attendance.ATTENDANCE_STATUS_START);
                attendance.setLatitude(finalLocation.getLatitude());
                attendance.setLongitude(finalLocation.getLongitude());
                attendance.setLoc(tvLocationAddressBegin.getText().toString());
                attendance.setLocalSession(sessionId);
                dbHandler.storeAttendance(attendance);

                new Runnable(){
                    @Override
                    public void run() {
                        new NotifyServer().execute("begin");
                    }
                };

                if (openSequence) {
                    Log.i(LOG_TAG, "View Routes");

                    Intent intent = new Intent(MarkAttendanceActivity.this, ViewRoutesActivity.class);
                    intent.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
                    startActivity(intent);
                    finish();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 150);
                }

            }
        });

        rndConfirmEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sessionId = sharedPref.getLocalSessionId();
                Attendance attendance = new Attendance(capturedTime, Attendance.ATTENDANCE_STATUS_END);
                attendance.setLatitude(finalLocation.getLatitude());
                attendance.setLongitude(finalLocation.getLongitude());
                attendance.setLocalSession(sessionId);
                dbHandler.storeAttendance(attendance);
                sharedPref.endDay();

                new Runnable(){
                    @Override
                    public void run() {
                new NotifyServer().execute("end");
                    }
                };
                System.exit(0);
                finish();
            }
        });

        locationHandler = new Handler();


    }

    private void showButton() {
        if (isDayStarted) {
            rndConfirmEnd.setEnabled(true);
            ViewPropertyAnimator.animate(rndConfirmEnd).cancel();
            ViewPropertyAnimator.animate(rndConfirmEnd).scaleX(1).scaleY(1).setDuration(200).setStartDelay(50).start();
        } else {
            rndConfirmBegin.setEnabled(true);
            ViewPropertyAnimator.animate(rndConfirmBegin).cancel();
            ViewPropertyAnimator.animate(rndConfirmBegin).scaleX(1).scaleY(1).setDuration(200).setStartDelay(50).start();
        }
    }

    private void requestGPSLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        tvProgress.setVisibility(View.VISIBLE);
        tvProgress.setText("Getting location (GPS)");
//        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();
    }

    private void requestNetworkLocation() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        tvProgress.setVisibility(View.VISIBLE);
        tvProgress.setText("Getting location (Network)");
//        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.spin();
    }

    private void requestUpdates() {
//        boolean gpsActive;
        try {
            gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsActive = false;
        }

//        boolean networkActive;
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
                tvProgress.setVisibility(View.INVISIBLE);
                progressWheel.stopSpinning();
                Toast.makeText(MarkAttendanceActivity.this, "Please enable location service and try again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (switchRunnable != null && !locSwitch) {
            locationHandler.removeCallbacks(switchRunnable);
        }
        super.onBackPressed();
    }

    private class AttendanceLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                finalLocation = location;
                Log.i(LOG_TAG, "Lat : " + String.valueOf(location.getLatitude()) + "\nLon : " + String.valueOf(location.getLongitude()));
                locationManager.removeUpdates(this);

//                showButton();
                capturedTime = System.currentTimeMillis();

                if (NetworkUtil.isNetworkAvailable(MarkAttendanceActivity.this)) {
                    if (isDayStarted) {
                        tvCapturedTimeEnd.setText(dateFormat.format(new Date(capturedTime)));
                        tvLocationAddressEnd.setText("Please wait...");
                        tvLocationCoordinatesEnd.setText(String.valueOf(finalLocation.getLatitude()) + ", " + String.valueOf(finalLocation.getLongitude()));
                    } else {
                        tvCapturedTimeBegin.setText(dateFormat.format(new Date(capturedTime)));
                        tvLocationAddressBegin.setText("Please wait...");
                        tvLocationCoordinatesBegin.setText(String.valueOf(finalLocation.getLatitude()) + ", " + String.valueOf(finalLocation.getLongitude()));
                    }
                    new GetAddress().execute();
                } else {
                    tvProgress.setVisibility(View.INVISIBLE);
                    progressWheel.stopSpinning();

                    if (isDayStarted) {
                        tvCapturedTimeEnd.setText(dateFormat.format(new Date(capturedTime)));
                        tvLocationAddressEnd.setText("Cannot process location");
                        tvLocationCoordinatesEnd.setText(String.valueOf(finalLocation.getLatitude()) + ", " + String.valueOf(finalLocation.getLongitude()));
                    } else {
                        tvCapturedTimeBegin.setText(dateFormat.format(new Date(capturedTime)));
                        tvLocationAddressBegin.setText("Cannot process location");
                        tvLocationCoordinatesBegin.setText(String.valueOf(finalLocation.getLatitude()) + ", " + String.valueOf(finalLocation.getLongitude()));
                    }

                    showButton();
                }

//                tvProgress.setVisibility(View.INVISIBLE);
//                progressWheel.stopSpinning();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            if (finalLocation == null) {
                Toast.makeText(MarkAttendanceActivity.this, "Provider enabled. Accessing location", Toast.LENGTH_SHORT).show();

                tvProgress.setVisibility(View.VISIBLE);
//                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.spin();
                requestUpdates();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MarkAttendanceActivity.this, "Provider disabled", Toast.LENGTH_SHORT).show();
            tvProgress.setVisibility(View.INVISIBLE);
            progressWheel.stopSpinning();
        }

    }

    private class NotifyServer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                networkFunctions.setAttendance(finalLocation, params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class GetAddress extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvProgress.setText("Processing location");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                String response = networkFunctions.getAddressOfLocation(finalLocation);

                if (response != null && response.length() > 0) {
                    JSONObject responseJSON = new JSONObject(response);

                    boolean status = responseJSON.getString("status").equalsIgnoreCase("OK");

                    if (status) {
                        JSONArray addressesArray = responseJSON.getJSONArray("results");
                        if (addressesArray.length() > 0) {
                            String finalAddress = addressesArray.getJSONObject(0).getString("formatted_address");
                            Log.i(LOG_TAG, "Final Address: " + finalAddress);
                            if (finalAddress != null) {
                                return finalAddress;
                            }
                        }
                    }
                }

                return null;

            } catch (IOException e) {
                Log.d(LOG_TAG, "IOException caught");
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                Log.d(LOG_TAG, "JSONException caught");
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            tvAddress.setText(s);

            if (isDayStarted) {
                if (s != null) {
                    tvLocationAddressEnd.setText(s);
                } else {
                    tvLocationAddressEnd.setText("Cannot process location");
                }
                tvLocationCoordinatesEnd.setText(String.valueOf(finalLocation.getLatitude()) + ", " + String.valueOf(finalLocation.getLongitude()));
            } else {
                if (s != null) {
                    tvLocationAddressBegin.setText(s);
                } else {
                    tvLocationAddressBegin.setText("Cannot process location");
                }

                tvLocationCoordinatesBegin.setText(String.valueOf(finalLocation.getLatitude()) + ", " + String.valueOf(finalLocation.getLongitude()));
            }

            tvProgress.setVisibility(View.INVISIBLE);
            progressWheel.stopSpinning();

            showButton();
        }
    }

    private class TimeRunner implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    runOnUiThread(countRunnable);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

package com.colombosoft.ednasalespad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.User;
import com.colombosoft.ednasalespad.utils.RequestCodes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SplashActivity extends Activity {

    private final String LOG_TAG = SplashActivity.class.getSimpleName();

    private SharedPref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //instantiate SharePref Object
        pref = SharedPref.getInstance(SplashActivity.this);

        DatabaseHandler databaseHandler = DatabaseHandler.getDbHandler(SplashActivity.this);
        User user = pref.getLoginUser();
        if (user != null) {
            int userId = user.getId();
            long lastLoggedInTime = databaseHandler.getLoggedInTime(userId);
            if (isNewDay(lastLoggedInTime)) {
                pref.setLoginStatus(false);
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.isLoggedIn()) {

//                    Intent intent2 = new Intent(SplashActivity.this, MarkAttendanceActivity.class);
//                    intent2.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
//                    startActivity(intent2);
//                    finish();


                    if (pref.isDayStarted()) {
                        if (pref.getSelectedRoute() == null) {
                            Toast.makeText(SplashActivity.this, "Select Route", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SplashActivity.this, ViewRoutesActivity.class);
                            intent.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SplashActivity.this, "Dashboard", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();


                        }
//                        startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
//                        finish();
                    } else {
                        Toast.makeText(SplashActivity.this, "Mark Attendance", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SplashActivity.this, MarkAttendanceActivity.class);
                        intent.putExtra(RequestCodes.KEY_STARTING_SEQUENCE, true);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    User user = pref.getLoginUser();
                    if (user != null) {
                        Toast.makeText(SplashActivity.this, "Unlock", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SplashActivity.this, UnlockActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.i(LOG_TAG, "Login");
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        }, 1000);

    }

    private boolean isNewDay(long lastTimestamp) {

        long now = System.currentTimeMillis() / 1000L;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String lastDate = simpleDateFormat.format(new Date(lastTimestamp * 1000));
        String nowDate = simpleDateFormat.format(new Date(now * 1000));

        Log.i(LOG_TAG, lastDate);
        Log.i(LOG_TAG, nowDate);

        if (nowDate.equals(lastDate)) {
            //the day has not changed
            return false;
        } else {
            //day has changed
            return true;
        }
    }
}

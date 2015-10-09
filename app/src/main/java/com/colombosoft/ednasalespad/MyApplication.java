package com.colombosoft.ednasalespad;

import android.app.Application;
import android.content.Context;

/**
 * Created by Admin on 10/8/15.
 */
public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}

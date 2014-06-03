package com.morrowbone.mafiacards.app.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MafiaApp extends Application {
    private volatile static MafiaApp mInstance;

    private SharedPreferences userPrefs;

    public static MafiaApp getInstance(){
        if (mInstance == null) {
            synchronized (MafiaApp.class) {
                if (mInstance == null) {
                    mInstance = new MafiaApp();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        userPrefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
    }

    public static boolean checkInternetConection(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    public void putValueToUserPrefs(String key, String value) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getFromUserPrefs(String key, String defValue) {
        return userPrefs.getString(key, defValue);
    }

    public void clearUserPrefs() {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.clear();
        editor.commit();
    }

    public SharedPreferences getUserSharedPrefrences() {
        return userPrefs;
    }


}

package com.morrowbone.mafiacards.app.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.morrowbone.mafiacards.app.R;

import java.util.HashMap;

public class MafiaApp extends Application {
    private volatile static MafiaApp mInstance;

    private SharedPreferences userPrefs;

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-53588694-1";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public MafiaApp() {
        super();
    }

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

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
                    R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

}

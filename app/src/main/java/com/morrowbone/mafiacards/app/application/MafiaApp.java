package com.morrowbone.mafiacards.app.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.morrowbone.mafiacards.app.R;

import java.util.HashMap;

public class MafiaApp extends MultiDexApplication {
    private volatile static MafiaApp mInstance;

    public MafiaApp() {
        super();
    }

    public static MafiaApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}

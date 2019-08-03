package com.morrowbone.mafiacards.app.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.morrowbone.mafiacards.app.application.MafiaApp;

public class StatisticUtils {
    public static void sendActionInfo(String category, String action) {
        Tracker t = MafiaApp.getInstance().getTracker(
                MafiaApp.TrackerName.APP_TRACKER);
        t.send(new HitBuilders.EventBuilder().setAction(action).
                setCategory(category).build());
    }

    public static void sendActionInfo(String category, String action, int size) {
        Tracker t = MafiaApp.getInstance().getTracker(
                MafiaApp.TrackerName.APP_TRACKER);
        t.send(new HitBuilders.EventBuilder().setAction(action).
                setCategory(category).setValue(size).build());
    }
}

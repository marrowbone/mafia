package com.morrowbone.mafiacards.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by morrow on 30.08.2014.
 */
public class Utils {
    public static Integer getPlayedGameCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.sharedPreferencesName, Context.MODE_PRIVATE);

        if (prefs.contains(Constants.Pref.games)) {
            Integer games = prefs.getInt(Constants.Pref.games, 0);
            return games;
        } else {
            prefs.edit().putInt(Constants.Pref.games, 0).apply();
            return 0;
        }
    }

    public static void incrementPlayedGameCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.sharedPreferencesName, Context.MODE_PRIVATE);

        Integer games = prefs.getInt(Constants.Pref.games, 0);
        games++;
        prefs.edit().putInt(Constants.Pref.games, games).apply();
    }

    public static Integer getRateAfterCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.sharedPreferencesName, Context.MODE_PRIVATE);

        if (prefs.contains(Constants.Pref.rateAfter)) {
            Integer games = prefs.getInt(Constants.Pref.rateAfter, 3);
            return games;
        } else {
            prefs.edit().putInt(Constants.Pref.rateAfter, 3).apply();
            return 3;
        }
    }

    public static void incrementRateAfter(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.sharedPreferencesName, Context.MODE_PRIVATE);

        Integer games = prefs.getInt(Constants.Pref.rateAfter, 3);
        games += 8;
        prefs.edit().putInt(Constants.Pref.rateAfter, games).apply();
    }

    public static Boolean isEnableRateApp(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.sharedPreferencesName, Context.MODE_PRIVATE);

        Boolean neverRate = prefs.getBoolean(Constants.Pref.neverRate, false);

        return !neverRate;
    }

    public static void setIsEnableRateAppToFalse(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.sharedPreferencesName, Context.MODE_PRIVATE);

        prefs.edit().putBoolean(Constants.Pref.neverRate, true).apply();
    }

}

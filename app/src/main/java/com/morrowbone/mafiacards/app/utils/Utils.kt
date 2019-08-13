package com.morrowbone.mafiacards.app.utils

import android.content.Context
import android.content.SharedPreferences

import com.morrowbone.mafiacards.app.utils.sharedPreferencesName

/**
 * Created by morrow on 30.08.2014.
 */
object Utils {
    fun getPlayedGameCount(context: Context): Int? {
        val prefs = context.getSharedPreferences(
                sharedPreferencesName, Context.MODE_PRIVATE)

        if (prefs.contains(CardsUtils.Pref.games)) {
            return prefs.getInt(CardsUtils.Pref.games, 0)
        } else {
            prefs.edit().putInt(CardsUtils.Pref.games, 0).apply()
            return 0
        }
    }

    fun incrementPlayedGameCount(context: Context) {
        val prefs = context.getSharedPreferences(
                sharedPreferencesName, Context.MODE_PRIVATE)

        var games: Int = prefs.getInt(CardsUtils.Pref.games, 0)
        games++
        prefs.edit().putInt(CardsUtils.Pref.games, games).apply()
    }

    fun getRateAfterCount(context: Context): Int? {
        val prefs = context.getSharedPreferences(
                sharedPreferencesName, Context.MODE_PRIVATE)

        if (prefs.contains(CardsUtils.Pref.rateAfter)) {
            return prefs.getInt(CardsUtils.Pref.rateAfter, 3)
        } else {
            prefs.edit().putInt(CardsUtils.Pref.rateAfter, 3).apply()
            return 3
        }
    }

    fun incrementRateAfter(context: Context) {
        val prefs = context.getSharedPreferences(
                sharedPreferencesName, Context.MODE_PRIVATE)

        var games: Int = prefs.getInt(CardsUtils.Pref.rateAfter, 3)
        games += 8
        prefs.edit().putInt(CardsUtils.Pref.rateAfter, games).apply()
    }

    fun isEnableRateApp(context: Context): Boolean? {
        val prefs = context.getSharedPreferences(
                sharedPreferencesName, Context.MODE_PRIVATE)

        val neverRate = prefs.getBoolean(CardsUtils.Pref.neverRate, false)

        return !neverRate
    }

    fun setIsEnableRateAppToFalse(context: Context) {
        val prefs = context.getSharedPreferences(
                sharedPreferencesName, Context.MODE_PRIVATE)

        prefs.edit().putBoolean(CardsUtils.Pref.neverRate, true).apply()
    }
}

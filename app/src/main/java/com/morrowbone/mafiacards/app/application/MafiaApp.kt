package com.morrowbone.mafiacards.app.application

import android.content.ContextWrapper
import androidx.multidex.MultiDexApplication
import com.morrowbone.mafiacards.app.data.AppDatabase
import com.morrowbone.mafiacards.app.utils.Utils
import com.pixplicity.easyprefs.library.Prefs

class MafiaApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()

        // I use this to force database updating
        val dataBaseVersion = AppDatabase.getInstance(this).openHelper.readableDatabase.version
    }

    companion object {
        var instance: MafiaApp? = null
            private set
    }
}

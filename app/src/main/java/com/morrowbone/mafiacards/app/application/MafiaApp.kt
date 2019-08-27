package com.morrowbone.mafiacards.app.application

import android.content.ContextWrapper
import androidx.multidex.MultiDexApplication
import com.morrowbone.mafiacards.app.data.AppDatabase
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
        forceDatabaseInitialisation()
    }

    private fun forceDatabaseInitialisation() {
        val database = AppDatabase.getInstance(applicationContext)
        database.deckDao().getDecks()
    }

    companion object {
        var instance: MafiaApp? = null
            private set
    }
}

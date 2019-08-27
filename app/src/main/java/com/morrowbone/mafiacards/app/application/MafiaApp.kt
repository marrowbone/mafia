package com.morrowbone.mafiacards.app.application

import androidx.multidex.MultiDexApplication
import com.morrowbone.mafiacards.app.data.AppDatabase

class MafiaApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
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

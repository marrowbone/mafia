package com.morrowbone.mafiacards.app.application

import androidx.multidex.MultiDexApplication
import com.morrowbone.mafiacards.app.data.AppDatabase

class MafiaApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: MafiaApp? = null
            private set
    }
}

package com.morrowbone.mafiacards.app.utils

import android.content.Context
import com.morrowbone.mafiacards.app.data.AppDatabase
import com.morrowbone.mafiacards.app.data.CardRepository

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun getCardRepository(context: Context): CardRepository {
        val appDatabase = AppDatabase.getInstance(context.applicationContext)
        return CardRepository.getInstance(appDatabase.cardDao(), appDatabase.defaultCardDao())
    }
}

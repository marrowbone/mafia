package com.morrowbone.mafiacards.app.utils

import android.content.Context
import com.morrowbone.mafiacards.app.data.AppDatabase
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getCardRepository(context: Context): CardRepository {
        val appDatabase = AppDatabase.getInstance(context.applicationContext)
        return CardRepository.getInstance(appDatabase.cardDao(), appDatabase.defaultCardDao())
    }

    fun provideCardListViewModelFactory(context: Context): CardListViewModelFactory {
        val repository = getCardRepository(context)
        return CardListViewModelFactory(repository)
    }
}

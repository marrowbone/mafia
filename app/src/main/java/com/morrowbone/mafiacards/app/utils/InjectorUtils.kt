package com.morrowbone.mafiacards.app.utils

import android.content.Context
import com.morrowbone.mafiacards.app.data.AppDatabase
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModelFactory
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getCardRepository(context: Context): CardRepository {
        val appDatabase = AppDatabase.getInstance(context.applicationContext)
        return CardRepository.getInstance(appDatabase.cardDao(), appDatabase.defaultCardDao())
    }

    private fun getDeckRepository(context: Context): DeckRepository {
        val appDatabase = AppDatabase.getInstance(context.applicationContext)
        return DeckRepository.getInstance(appDatabase.deckDao(), appDatabase.defaultDeckDao())
    }

    fun provideCardListViewModelFactory(context: Context): CardListViewModelFactory {
        val repository = getCardRepository(context)
        return CardListViewModelFactory(repository)
    }

    fun provideDeckViewModelFactory(context: Context): DeckViewModelFactory {
        val repository = getDeckRepository(context)
        return DeckViewModelFactory(repository)
    }
}

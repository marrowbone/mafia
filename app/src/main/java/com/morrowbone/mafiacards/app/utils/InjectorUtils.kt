package com.morrowbone.mafiacards.app.utils

import android.content.Context
import com.morrowbone.mafiacards.app.data.AppDatabase
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.viewmodels.BaseDecksViewModelFactory
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModelFactory
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getCardRepository(context: Context): CardRepository {
        val appDatabase = AppDatabase.getInstance(context.applicationContext)
        return CardRepository.getInstance(appDatabase.cardDao())
    }

    private fun getDeckRepository(context: Context): DeckRepository {
        val appDatabase = AppDatabase.getInstance(context.applicationContext)
        return DeckRepository.getInstance(appDatabase.deckDao())
    }

    fun provideCardListViewModelFactory(context: Context): CardListViewModelFactory {
        val repository = getCardRepository(context)
        return CardListViewModelFactory(repository)
    }

    fun provideDeckViewModelFactory(context: Context, deckId: Int): DeckViewModelFactory {
        val deckRepository = getDeckRepository(context)
        val cardRepository = getCardRepository(context)
        return DeckViewModelFactory(deckRepository, cardRepository, deckId)
    }

    fun provideBaseViewModelFactory(context: Context): BaseDecksViewModelFactory {
        val deckRepository = getDeckRepository(context)
        return BaseDecksViewModelFactory(deckRepository, getCardRepository(context))
    }
}

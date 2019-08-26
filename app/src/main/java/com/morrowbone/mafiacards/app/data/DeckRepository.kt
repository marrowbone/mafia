package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData

class DeckRepository private constructor(
        private val deckDao: DeckDao,
        private val defaultDeckDao: DefaultDeckDao) {

    fun getDefaultDecks(): LiveData<List<DefaultDeck>> = defaultDeckDao.getDefaultDecks()

    fun getDeck(id: Int) = deckDao.getDeck(id)

    fun insertDeck(deck: Deck) {
        deckDao.insert(deck)
    }

    companion object {
        @Volatile
        private var instance: DeckRepository? = null

        fun getInstance(deckDao: DeckDao, defaultDeckDao: DefaultDeckDao) =
                instance ?: synchronized(this) {
                    instance ?: DeckRepository(deckDao, defaultDeckDao).also { instance = it }
                }

        const val USER_DECK = 1
        const val DEFAULT_DECK = 2
        const val USER_DECK_DRAFT = 3

        const val MISSED_DEFAULT_DECK = 2
    }
}
package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData

class DeckRepository private constructor(
        private val deckDao: DeckDao,
        private val defaultDeckDao: DefaultDeckDao) {

    fun getDefaultDeck(cardCount: Int): LiveData<DefaultDeck> = defaultDeckDao.getDefaultDeck(cardCount)

    fun insertLastUsedDeck(deck: Deck) {
        deck.deckId = LAST_USED_DECK_ID
        deckDao.insert(deck)
    }

    companion object {
        @Volatile
        private var instance: DeckRepository? = null

        fun getInstance(deckDao: DeckDao, defaultDeckDao: DefaultDeckDao) =
                instance ?: synchronized(this) {
                    instance ?: DeckRepository(deckDao, defaultDeckDao).also { instance = it }
                }

        val LAST_USED_DECK_ID = Int.MAX_VALUE.toLong()
    }
}
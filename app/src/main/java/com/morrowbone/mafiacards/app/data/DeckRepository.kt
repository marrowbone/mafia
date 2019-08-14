package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData

class DeckRepository private constructor(
        private val deckDao: DeckDao,
        private val defaultDeckDao: DefaultDeckDao) {

    fun getDefaultDeck(cardCount: Int): LiveData<DefaultDeck> = defaultDeckDao.getDefaultDeck(cardCount)

    fun getDeck(id: Long): LiveData<Deck> = deckDao.getDeck(id)

    fun getLastUsedDeck(): LiveData<Deck> = getDeck(LAST_USED_DECK_ID)

    fun insertLastUsedDeck(deck: Deck) {
        deck.deckId = LAST_USED_DECK_ID
        deckDao.insert(deck)
    }

    fun insertDeck(deck: Deck): Long {
        val id = deck.hashCode().toLong()
        deck.deckId = id
        deckDao.insert(deck)
        return id
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
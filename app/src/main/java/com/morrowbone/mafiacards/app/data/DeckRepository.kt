package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DeckRepository private constructor(
        private val deckDao: DeckDao,
        private val defaultDeckDao: DefaultDeckDao) {

    fun getDefaultDecks(): LiveData<List<DefaultDeck>> = defaultDeckDao.getDefaultDecks()

    fun getDeck(id: Int) = deckDao.getDeck(id)

    fun getDefaultDeck(id: Int) = defaultDeckDao.getDefaultDeck(id)

    suspend fun insertDeck(deck: Deck) {
        withContext(IO) {
            deckDao.insert(deck)
        }
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
        const val DEFAULT_DECKS_COUNT = 25
    }
}
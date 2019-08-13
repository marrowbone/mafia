package com.morrowbone.mafiacards.app.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeckRepository private constructor(
        private val deckDao: DeckDao,
        private val defaultDeckDao: DefaultDeckDao) {

    suspend fun getDefaultDeck(cardCount: Int): DefaultDeck {
        return withContext(Dispatchers.IO) {
            defaultDeckDao.getDefaultDeck(cardCount)
        }
    }

    companion object {
        @Volatile
        private var instance: DeckRepository? = null

        fun getInstance(deckDao: DeckDao, defaultDeckDao: DefaultDeckDao) =
                instance ?: synchronized(this) {
                    instance ?: DeckRepository(deckDao, defaultDeckDao).also { instance = it }
                }
    }
}
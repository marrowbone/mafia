package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData

class DeckRepository private constructor(
        private val deckDao: DeckDao,
        private val defaultDeckDao: DefaultDeckDao) {

    fun getDefaultDeck(cardCount: Int): LiveData<DefaultDeck> = defaultDeckDao.getDefaultDeck(cardCount)

    companion object {
        @Volatile
        private var instance: DeckRepository? = null

        fun getInstance(deckDao: DeckDao, defaultDeckDao: DefaultDeckDao) =
                instance ?: synchronized(this) {
                    instance ?: DeckRepository(deckDao, defaultDeckDao).also { instance = it }
                }
    }
}
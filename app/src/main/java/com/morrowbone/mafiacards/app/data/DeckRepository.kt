package com.morrowbone.mafiacards.app.data

class DeckRepository private constructor(
        private val deckDao: DeckDao,
        private val defaultDeckDao: DefaultDeckDao) {

    fun getUserCards() = deckDao.getDecks()

    fun getDefaultDeck(cardCount: Int): DefaultDeck = defaultDeckDao.getDefaultDeck(cardCount)

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

    }
}
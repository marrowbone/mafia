package com.morrowbone.mafiacards.app.data

class CardRepository private constructor(
        private val cardDao: CardDao,
        private val defaultCardDao: DefaultCardDao) {

    fun getCards(): List<AbstractCard> {
        val defaultCards = defaultCardDao.getCards()
        val userCards = cardDao.getCards()
        val allCards = ArrayList<AbstractCard>(defaultCards)
                .also { it.addAll(userCards) }
        return allCards
    }

    companion object {
        @Volatile
        private var instance: CardRepository? = null

        fun getInstance(cardDao: CardDao, defaultCardDao: DefaultCardDao) =
                instance ?: synchronized(this) {
                    instance ?: CardRepository(cardDao, defaultCardDao).also { instance = it }
                }
    }
}
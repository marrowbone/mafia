package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class CardRepository private constructor(
        private val cardDao: CardDao,
        private val defaultCardDao: DefaultCardDao) {

    fun getCards(): LiveData<List<AbstractCard>> {
        val defaultCards = mutableListOf<DefaultCard>()
        val userCards = mutableListOf<Card>()
        fun getAllCards() = ArrayList<AbstractCard>(defaultCards).apply { addAll(userCards) }

        val mergedLiveData = MediatorLiveData<List<AbstractCard>>()
        mergedLiveData.addSource(defaultCardDao.getCards(), Observer {
            defaultCards.clear()
            defaultCards.addAll(it)
            mergedLiveData.value = getAllCards()
        })
        mergedLiveData.addSource(cardDao.getCards(), Observer {
            userCards.clear()
            userCards.addAll(it)
            mergedLiveData.value = getAllCards()
        })
        return mergedLiveData
    }

    suspend fun createCard(card: Card) {
        withContext(IO) {
            cardDao.insert(card)
        }
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
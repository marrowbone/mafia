package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class CardRepository private constructor(
        private val cardDao: CardDao) {

    fun getCards(): LiveData<List<AbstractCard>> {
        val cards = mutableListOf<AbstractCard>()
        val mergedLiveData = MediatorLiveData<List<AbstractCard>>()
        mergedLiveData.addSource(cardDao.getCards(), Observer {
            cards.clear()
            cards.addAll(DefaultDeckDao.getCards())
            cards.addAll(it)
            mergedLiveData.value = cards
        })
        return mergedLiveData
    }

    fun getUserCard(cardId: String): LiveData<Card> {
        return cardDao.getCard(cardId)
    }

    fun getUserCardNow(cardId: String): Card? {
        return cardDao.getCardNow(cardId)
    }

    suspend fun createCard(card: Card) {
        withContext(IO) {
            cardDao.insert(card)
        }
    }

    suspend fun deleteCard(card: Card) {
        withContext(IO){
            cardDao.delete(card)
        }
    }

    companion object {
        @Volatile
        private var instance: CardRepository? = null

        fun getInstance(cardDao: CardDao) =
                instance ?: synchronized(this) {
                    instance ?: CardRepository(cardDao).also { instance = it }
                }
    }
}
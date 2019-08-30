package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.*
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.Card
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.DefaultDeckDao
import kotlinx.coroutines.launch

abstract class BaseCardViewModel internal constructor(protected val cardRepository: CardRepository) : ViewModel() {
    fun getCard(cardId: String, isDefaultCard: Boolean): LiveData<AbstractCard> {
        val liveData = MediatorLiveData<AbstractCard>()
        if (isDefaultCard) {
            liveData.value = DefaultDeckDao.findCard(cardId)!!
        } else {
            val userCardLiveData = getUserCard(cardId)
            liveData.addSource(userCardLiveData, Observer {
                liveData.value = it
            })
        }
        return liveData
    }

    fun getUserCard(cardId: String): LiveData<Card> {
        return cardRepository.getUserCard(cardId)
    }

    fun addCard(card: Card) {
        viewModelScope.launch {
            cardRepository.createCard(card)
        }
    }

    fun saveChanged(card: Card) {
        addCard(card)
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            cardRepository.deleteCard(card)
        }
    }
}
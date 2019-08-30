package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.*
import kotlinx.coroutines.launch

class CardListViewModel internal constructor(private val cardRepository: CardRepository) : ViewModel() {
    val cards: LiveData<List<AbstractCard>> = cardRepository.getCards()

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
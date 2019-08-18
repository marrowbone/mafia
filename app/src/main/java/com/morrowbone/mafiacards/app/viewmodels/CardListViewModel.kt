package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.Card
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.DefaultCard
import kotlinx.coroutines.launch

class CardListViewModel internal constructor(private val cardRepository: CardRepository) : ViewModel() {
    val cards: LiveData<List<AbstractCard>> = cardRepository.getCards()

    fun getCard(cardId: String, isDefaultCard: Boolean) = if (isDefaultCard) {
        getDefaultCard(cardId)
    } else {
        getUserCard(cardId)
    }


    fun getUserCard(cardId: String): LiveData<Card> {
        return cardRepository.getUserCard(cardId)
    }

    fun getDefaultCard(cardId: String): LiveData<DefaultCard> {
        return cardRepository.getDefaultCard(cardId)
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
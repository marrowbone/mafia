package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.Card
import com.morrowbone.mafiacards.app.data.CardRepository
import kotlinx.coroutines.launch

class CardListViewModel internal constructor(val cardRepository: CardRepository) : ViewModel() {
    val cards: LiveData<List<AbstractCard>> = cardRepository.getCards()

    fun addCard(card: Card) {
        viewModelScope.launch {
            cardRepository.createCard(card)
        }
    }
}
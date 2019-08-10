package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.CardRepository

class CardListViewModel internal constructor(cardRepository: CardRepository) : ViewModel() {
    val cards: LiveData<List<AbstractCard>> = cardRepository.getCards()
}
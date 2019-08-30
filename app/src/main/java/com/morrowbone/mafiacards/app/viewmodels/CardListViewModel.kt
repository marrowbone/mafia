package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.*
import kotlinx.coroutines.launch

class CardListViewModel internal constructor(cardRepository: CardRepository) : BaseCardViewModel(cardRepository) {
    val cards: LiveData<List<AbstractCard>> = cardRepository.getCards()
}
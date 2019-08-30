package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.DeckRepository

class BaseDecksViewModelFactory(
        private val deckRepository: DeckRepository,
        private val cardRepository: CardRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = BaseDeckViewModel(deckRepository, cardRepository) as T
}
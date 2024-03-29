package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.DeckRepository

class DeckViewModelFactory(
        private val deckRepository: DeckRepository,
        private val cardRepository: CardRepository,
        private val deckId: Int
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DeckViewModel(deckRepository, cardRepository, deckId) as T
}
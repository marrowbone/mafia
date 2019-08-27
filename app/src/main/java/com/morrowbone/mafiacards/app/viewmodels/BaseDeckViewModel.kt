package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class BaseDeckViewModel internal constructor(protected val deckRepository: DeckRepository) : ViewModel() {
    fun saveDeck(deck: Deck) {
        viewModelScope.launch {
            deckRepository.insertDeck(deck)
        }
    }
}
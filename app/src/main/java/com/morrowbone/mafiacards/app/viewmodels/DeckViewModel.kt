package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository
import kotlinx.coroutines.launch

open class DeckViewModel internal constructor(protected val deckRepository: DeckRepository) : ViewModel() {
    fun getUserDeck(deckId: Int) = deckRepository.getDeck(deckId)

    fun getUserDecks() = deckRepository.getUserCards()

    fun saveDeck(deck: Deck) {
        deckRepository.insertDeck(deck)
    }

    fun getDefaultDeck(deckId: Int) = deckRepository.getDefaultDeck(deckId)
}
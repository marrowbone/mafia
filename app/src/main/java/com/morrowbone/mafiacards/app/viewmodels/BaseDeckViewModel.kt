package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository

abstract class BaseDeckViewModel internal constructor(protected val deckRepository: DeckRepository) : ViewModel() {
    fun saveDeck(deck: Deck) {
        deckRepository.insertDeck(deck)
    }
}
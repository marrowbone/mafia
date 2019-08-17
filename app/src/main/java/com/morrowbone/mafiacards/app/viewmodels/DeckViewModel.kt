package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository

class DeckViewModel internal constructor(private val deckRepository: DeckRepository) : ViewModel() {
    fun getUserDeck(deckId: Int) = deckRepository.getDeck(deckId)

    fun getUserDecks() = deckRepository.getUserCards()

    fun saveDeck(deck: Deck) = deckRepository.insertDeck(deck)

    fun getDefaultDeck(deckId: Int) = deckRepository.getDefaultDeck(deckId)
}
package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.DeckRepository

class DefaultDecksViewModel internal constructor(private val deckRepository: DeckRepository) : ViewModel() {
    val decks = deckRepository.getDefaultDecks()
}
package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.DeckRepository

class DefaultDecksViewModel internal constructor(deckRepository: DeckRepository) : BaseDeckViewModel(deckRepository) {
    val decks = deckRepository.getDefaultDecks()
}
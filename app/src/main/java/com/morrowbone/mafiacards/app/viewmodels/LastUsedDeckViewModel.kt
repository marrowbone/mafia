package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository

class LastUsedDeckViewModel internal constructor(deckRepository: DeckRepository) : ViewModel() {
    val deck: LiveData<Deck> = deckRepository.getLastUsedDeck()
}
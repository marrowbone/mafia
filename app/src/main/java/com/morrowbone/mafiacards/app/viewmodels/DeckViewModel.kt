package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository

class DeckViewModel internal constructor(deckRepository: DeckRepository, deckId: Long) : ViewModel() {
    val deck: LiveData<Deck> = deckRepository.getDeck(deckId)
}
package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.data.DefaultDeck

class DefaultDeckViewModel internal constructor(deckRepository: DeckRepository, deckId: Int) : ViewModel() {
    val deck: LiveData<DefaultDeck> = deckRepository.getDefaultDeck(deckId)
}
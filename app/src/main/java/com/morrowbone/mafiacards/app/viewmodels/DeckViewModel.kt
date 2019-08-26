package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository
import kotlinx.coroutines.launch

open class DeckViewModel internal constructor(deckRepository: DeckRepository, deckId: Int) : BaseDeckViewModel(deckRepository) {
    val deck: LiveData<Deck> = deckRepository.getDeck(deckId)
}
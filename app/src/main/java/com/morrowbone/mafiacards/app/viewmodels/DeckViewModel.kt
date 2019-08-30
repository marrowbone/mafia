package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository
import kotlinx.coroutines.launch

open class DeckViewModel internal constructor(deckRepository: DeckRepository, cardRepository: CardRepository, deckId: Int) : BaseDeckViewModel(deckRepository, cardRepository) {
    val deck: LiveData<Deck> = deckRepository.getDeck(deckId)
    val cards: LiveData<List<AbstractCard>> = Transformations.switchMap(deck) {
        getCards(it?.cardIds ?: mutableListOf())
    }
}
package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.data.DefaultDeck

class DefaultDecksViewModel internal constructor(deckRepository: DeckRepository) : BaseDeckViewModel(deckRepository) {
    val deck: MediatorLiveData<DefaultDeck> = MediatorLiveData()
    private var source: LiveData<DefaultDeck>? = null

    fun updateDeck(deckId: Int) {
        if (source != null) {
            deck.removeSource(source!!)
        }
        source = deckRepository.getDefaultDeck(deckId)
        deck.addSource(source!!, Observer {
            if (deck != null) {
                deck.value = it
            }
        })
    }
}
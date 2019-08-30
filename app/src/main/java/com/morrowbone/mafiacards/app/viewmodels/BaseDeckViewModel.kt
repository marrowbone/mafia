package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morrowbone.mafiacards.app.data.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseDeckViewModel internal constructor(protected val deckRepository: DeckRepository, private val cardRepository: CardRepository) : ViewModel() {
    fun saveDeck(deck: Deck) {
        viewModelScope.launch {
            deckRepository.insertDeck(deck)
        }
    }

    fun getCards(ids: List<String>): LiveData<List<AbstractCard>> {
        val cardsLiveData: MediatorLiveData<List<AbstractCard>> = MediatorLiveData()
        GlobalScope.launch(IO) {
            val cards = mutableListOf<AbstractCard>()
            for (id in ids) {
                var card: AbstractCard? = DefaultDeckDao.findCard(id)
                if (card == null) {
                    card = cardRepository.getUserCardNow(id)
                    if (card == null) {
                        throw RuntimeException("Card $id is missed")
                    }
                }
                cards.add(card)
            }
            withContext(Main) {
                cardsLiveData.value = cards
            }
        }

        return cardsLiveData
    }
}
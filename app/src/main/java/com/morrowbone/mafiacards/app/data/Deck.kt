package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val deckId: Int,
        @Embedded
        val cardsSet: CardsSet
) {
    var cardOrder: MutableList<String> = mutableListOf()

    fun getCards(): List<AbstractCard> {
        return if (cardOrder.isEmpty()) {
            ArrayList<AbstractCard>(cardsSet.defaultCards).apply { addAll(cardsSet.userCards) }
        } else {
            cardOrder.map { cardId ->
                fun predicate(card: AbstractCard): Boolean = card.getId() == cardId
                cardsSet.defaultCards.find(::predicate) ?: cardsSet.userCards.find(::predicate)!!
            }
        }
    }

    fun shuffle(): Deck {
        cardOrder.clear()
        cardOrder.addAll(getCards().shuffled().map { it.getId() })
        return this
    }
}
package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
        @ColumnInfo() val defaultCards: List<DefaultCard>,
        val userCards: List<Card> = mutableListOf()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var deckId: Long = 0

    var cardOrder: MutableList<String> = mutableListOf()

    fun getCards(): List<AbstractCard> {
        return if (cardOrder.isEmpty()) {
            ArrayList<AbstractCard>(defaultCards).apply { addAll(userCards) }
        } else {
            cardOrder.map { cardId ->
                fun predicate(card: AbstractCard): Boolean = card.getId() == cardId
                defaultCards.find(::predicate) ?: userCards.find(::predicate)!!
            }
        }
    }

    fun shuffle(): Deck {
        cardOrder.clear()
        cardOrder.addAll(getCards().shuffled().map { it.getId() })
        return this
    }
}
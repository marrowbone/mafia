package com.morrowbone.mafiacards.app.data

import androidx.room.*

@Entity(tableName = "default_decks")
data class DefaultDeck(
        @Embedded
        val deck: Deck,

        @PrimaryKey @ColumnInfo(name = "player_count") val playerCount: Int = deck.defaultCards.size
)
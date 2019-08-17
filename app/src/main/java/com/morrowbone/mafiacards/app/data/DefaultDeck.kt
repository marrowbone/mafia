package com.morrowbone.mafiacards.app.data

import androidx.room.*

@Entity(tableName = "default_decks")
data class DefaultDeck(
        @Embedded
        val cardsSet: CardsSet,

        @PrimaryKey @ColumnInfo(name = "player_count") val playerCount: Int = cardsSet.defaultCards.size
)
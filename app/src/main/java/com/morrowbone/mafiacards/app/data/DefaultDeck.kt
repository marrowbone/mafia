package com.morrowbone.mafiacards.app.data

import androidx.room.*

@Entity(tableName = "default_decks", foreignKeys = [ForeignKey(entity = Deck::class, parentColumns = ["id"], childColumns = ["deck_id"])],
        indices = [Index("deck_id")])
data class DefaultDeck(
        @PrimaryKey @ColumnInfo(name = "player_count") val playerCount: Int,

        @ColumnInfo(name = "deck_id") val deckId: Long
)
package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
        @ColumnInfo() val defaultCards: List<DefaultCard>,
        val userCards: List<Card>
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var deckId: Long = 0
}
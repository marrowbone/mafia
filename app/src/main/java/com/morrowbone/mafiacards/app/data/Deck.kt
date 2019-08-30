package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "decks")
data class Deck(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val deckId: Int,
        val cardIds: MutableList<String> = mutableListOf()
) {
    fun shuffle() {
        val shuffled = cardIds.shuffled()
        cardIds.clear()
        cardIds.addAll(shuffled)
    }
}
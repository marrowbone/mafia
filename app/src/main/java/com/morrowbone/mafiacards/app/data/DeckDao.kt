package com.morrowbone.mafiacards.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeckDao {
    @Query("SELECT * FROM decks")
    fun getDecks(): List<Deck>

    @Query("SELECT * FROM decks WHERE id = :deckId")
    fun getDeck(deckId: Long): Deck

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(deck: Deck)
}
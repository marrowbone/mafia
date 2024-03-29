package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeckDao {
    @Query("SELECT * FROM decks")
    fun getDecks(): LiveData<List<Deck>>

    @Query("SELECT * FROM decks WHERE id = :deckId")
    fun getDeck(deckId: Int): LiveData<Deck>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deck: Deck)
}
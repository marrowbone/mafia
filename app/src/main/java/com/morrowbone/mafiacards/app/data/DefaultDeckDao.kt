package com.morrowbone.mafiacards.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DefaultDeckDao {

    @Query("SELECT * FROM default_decks")
    fun getDefaultDecks(): List<DefaultDeck>

    @Query("SELECT * FROM default_decks WHERE player_count =:playerNumber")
    fun getDefaultDeck(playerNumber: Int): DefaultDeck

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cards: List<DefaultCard>)
}
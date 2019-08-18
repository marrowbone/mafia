package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DefaultCardDao {
    @Query("SELECT * FROM default_cards")
    fun getCards(): LiveData<List<DefaultCard>>

    @Query("SELECT * FROM default_cards WHERE id = :cardId")
    fun getCard(cardId: String): LiveData<DefaultCard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cards: List<DefaultCard>)
}
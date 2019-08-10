package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM cards WHERE id = :cardId")
    fun getCard(cardId: Int): Card

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(cards: List<Card>)
}
package com.morrowbone.mafiacards.app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM cards WHERE id = :cardId")
    fun getCard(cardId: String): LiveData<Card>

    @Query("SELECT * FROM cards WHERE id = :cardId")
    fun getCardNow(cardId: String): Card
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(cards: List<Card>)

    @Delete
    fun delete(card: Card)
}
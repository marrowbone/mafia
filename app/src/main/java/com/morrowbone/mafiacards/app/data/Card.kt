package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morrowbone.mafiacards.app.R

@Entity(tableName = "cards")
data class Card(val name: String, val info: String) : AbstractCard {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var cardId: Long = 0

    override fun getId() = cardId.toString()
    override fun getTitle() = name
    override fun getDescription() = info
    override fun getImageResId() = R.drawable.civilian_cr
}
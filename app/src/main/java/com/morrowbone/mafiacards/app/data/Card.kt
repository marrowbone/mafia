package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morrowbone.mafiacards.app.R

@Entity(tableName = "cards")
data class Card(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val cardId: String,
        val name: String,
        val info: String,
        @RoleType
        override val roleType: Int) : AbstractCard {

    override fun getId() = cardId
    override fun getTitle() = name
    override fun getDescription() = info
    override fun getImageResId() = R.drawable.user_role_cr
}
package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morrowbone.mafiacards.app.application.MafiaApp

@Entity(tableName = "default_cards")
data class DefaultCard(
        @PrimaryKey @ColumnInfo(name = "id") val cardId: String,
        val nameResId: Int,
        val descriptionResId: Int,
        val pictureResId: Int
) : AbstractCard {
    override fun getId(): String = cardId
    override fun getTitle(): String = MafiaApp.instance!!.getString(nameResId)
    override fun getDescription(): String = MafiaApp.instance!!.getString(descriptionResId)
    override fun getImageResId(): Int = pictureResId

    companion object {
        const val CIVILIAN = "civilian"
        const val MAFIA = "mafia"
        const val DETECTIVE = "detective"
        const val DON_MAFIA = "don_mafia"
        const val DOCTOR = "doctor"
        const val IMMORTAL = "immortal"
        const val MANIAC = "maniac"
    }
}
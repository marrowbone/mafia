package com.morrowbone.mafiacards.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.application.MafiaApp

@Entity(tableName = "default_cards")
data class DefaultCard(
        @PrimaryKey @ColumnInfo(name = "id") val cardId: String,
        @RoleType
        override val roleType: Int
) : AbstractCard {
    override fun getId(): String = cardId
    override fun getTitle(): String {
        val nameResId = when (cardId) {
            CIVILIAN -> R.string.role_civilian
            MAFIA -> R.string.role_mafia
            DETECTIVE -> R.string.role_detective
            DON_MAFIA -> R.string.role_don
            DOCTOR -> R.string.role_doctor
            IMMORTAL -> R.string.role_immortal
            MANIAC -> R.string.role_maniac
            PROSTITUTE -> R.string.putana
            else -> R.string.role_civilian
        }

        return MafiaApp.instance!!.getString(nameResId)
    }

    override fun getDescription(): String {
        val descriptionResId = when (cardId) {
            CIVILIAN -> R.string.role_civilian_info
            MAFIA -> R.string.role_mafia_info
            DETECTIVE -> R.string.role_detective_info
            DON_MAFIA -> R.string.role_don_info
            DOCTOR -> R.string.role_doctor_info
            IMMORTAL -> R.string.role_immortal_info
            MANIAC -> R.string.role_maniac_info
            PROSTITUTE -> R.string.role_putana_info
            else -> R.string.role_civilian_info
        }
        return MafiaApp.instance!!.getString(descriptionResId)
    }

    override fun getImageResId(): Int = when (cardId) {
        CIVILIAN -> R.drawable.civilian_cr
        MAFIA -> R.drawable.mafia_cr
        DETECTIVE -> R.drawable.detective_cr
        DON_MAFIA -> R.drawable.don_cr
        DOCTOR -> R.drawable.doctor_cr
        IMMORTAL -> R.drawable.immortal_cr
        MANIAC -> R.drawable.manic_cr
        PROSTITUTE -> R.drawable.ic_putana
        else -> R.drawable.civilian_cr
    }

    companion object {
        const val CIVILIAN = "civilian"
        const val MAFIA = "mafia"
        const val DETECTIVE = "detective"
        const val DON_MAFIA = "don_mafia"
        const val DOCTOR = "doctor"
        const val IMMORTAL = "immortal"
        const val MANIAC = "maniac"
        const val PROSTITUTE = "putana"
    }
}
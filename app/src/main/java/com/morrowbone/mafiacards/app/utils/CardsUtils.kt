package com.morrowbone.mafiacards.app.utils

import android.graphics.Typeface

import com.morrowbone.mafiacards.app.model.Card
import com.morrowbone.mafiacards.app.model.roles.Civilian
import com.morrowbone.mafiacards.app.model.roles.Detective
import com.morrowbone.mafiacards.app.model.roles.Doctor
import com.morrowbone.mafiacards.app.model.roles.DonMafia
import com.morrowbone.mafiacards.app.model.roles.Immortal
import com.morrowbone.mafiacards.app.model.roles.Mafia
import com.morrowbone.mafiacards.app.model.roles.Maniac

import java.util.ArrayList
import java.util.Locale

object CardsUtils {

    private var roles: MutableList<Card> = mutableListOf()

    object Pref {
        val games = "games"
        val rateAfter = "rate_after"
        val neverRate = "never_rate"
    }

    fun getRoles(): List<Card>? {
        return roles
    }


    init {
        roles = ArrayList()

        var card: Card
        card = Civilian()
        roles.add(card)

        card = Mafia()
        roles.add(card)

        card = Detective()
        roles.add(card)

        card = DonMafia()
        roles.add(card)

        card = Doctor()
        roles.add(card)

        card = Immortal()
        roles.add(card)

        card = Maniac()
        roles.add(card)
    }
}

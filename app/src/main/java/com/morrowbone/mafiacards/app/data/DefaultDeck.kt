package com.morrowbone.mafiacards.app.data

data class DefaultDeck(
        val cards: MutableList<DefaultCard>,
        val playerCount: Int = cards.size
)
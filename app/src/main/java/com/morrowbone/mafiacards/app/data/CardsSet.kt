package com.morrowbone.mafiacards.app.data

data class CardsSet(
        val defaultCards: List<DefaultCard>,
        val userCards: List<Card> = mutableListOf()) {
}
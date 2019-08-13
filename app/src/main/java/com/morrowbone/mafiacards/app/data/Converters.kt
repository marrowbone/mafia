package com.morrowbone.mafiacards.app.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun stringToDefaultCards(cardsString: String): List<DefaultCard> {
        val gson = Gson()
        val type = object : TypeToken<List<DefaultCard>>() {}.type
        return gson.fromJson(cardsString, type)
    }

    @TypeConverter
    fun defaultCardsToString(defaultCards: List<DefaultCard>): String {
        val gson = Gson()
        val type = object : TypeToken<List<DefaultCard>>() {}.type
        return gson.toJson(defaultCards, type)
    }

    @TypeConverter
    fun stringToCards(cardsString: String): List<Card> {
        val gson = Gson()
        val type = object : TypeToken<List<Card>>() {}.type
        return gson.fromJson(cardsString, type)
    }

    @TypeConverter
    fun cardsToString(cards: List<Card>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Card>>() {}.type
        return gson.toJson(cards, type)
    }
}
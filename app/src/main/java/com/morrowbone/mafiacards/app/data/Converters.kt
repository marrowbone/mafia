package com.morrowbone.mafiacards.app.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        val gson = Gson()
        val stringToDefaultCardType = object : TypeToken<List<DefaultCard>>() {}.type
        val defaultCardToStringType = object : TypeToken<List<DefaultCard>>() {}.type
        val stringToCards = object : TypeToken<List<Card>>() {}.type
        val cardsToString = object : TypeToken<List<Card>>() {}.type
        val stringToListOfString = object : TypeToken<List<String>>() {}.type
        val listOfStringToString = object : TypeToken<List<String>>() {}.type
    }

    @TypeConverter
    fun stringToDefaultCards(cardsString: String): List<DefaultCard> {
        return gson.fromJson(cardsString, stringToDefaultCardType)
    }

    @TypeConverter
    fun defaultCardsToString(defaultCards: List<DefaultCard>): String {
        return gson.toJson(defaultCards, defaultCardToStringType)
    }

    @TypeConverter
    fun stringToCards(cardsString: String): List<Card> {
        return gson.fromJson(cardsString, stringToCards)
    }

    @TypeConverter
    fun cardsToString(cards: List<Card>): String {
        return gson.toJson(cards, cardsToString)
    }

    @TypeConverter
    fun stringToListOfString(string: String): List<String> {
        return gson.fromJson(string, stringToListOfString)
    }

    @TypeConverter
    fun listOfStringToString(list: List<String>): String {
        return gson.toJson(list, listOfStringToString)
    }
}
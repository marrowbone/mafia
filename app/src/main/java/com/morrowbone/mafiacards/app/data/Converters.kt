package com.morrowbone.mafiacards.app.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        val gson = Gson()
        val stringToListOfString = object : TypeToken<List<String>>() {}.type
        val listOfStringToString = object : TypeToken<List<String>>() {}.type
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
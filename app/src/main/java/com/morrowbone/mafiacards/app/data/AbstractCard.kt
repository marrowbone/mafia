package com.morrowbone.mafiacards.app.data

interface AbstractCard {
    fun getId(): String
    fun getTitle(): String
    fun getDescription(): String
    fun getImageResId(): Int
}
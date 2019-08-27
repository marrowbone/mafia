package com.morrowbone.mafiacards.app.data

interface AbstractCard {
    @RoleType
    val roleType: Int

    fun getId(): String
    fun getTitle(): String
    fun getDescription(): String
    fun getImageResId(): Int
}
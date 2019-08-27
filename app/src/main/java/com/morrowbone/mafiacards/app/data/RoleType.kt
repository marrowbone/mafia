package com.morrowbone.mafiacards.app.data

import androidx.annotation.IntDef

@IntDef(CIVILIAN, MAFIA, NEUTRAL)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class RoleType

const val CIVILIAN = 0
const val MAFIA = 1
const val NEUTRAL = 2
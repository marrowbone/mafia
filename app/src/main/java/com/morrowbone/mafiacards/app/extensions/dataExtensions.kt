package com.morrowbone.mafiacards.app.extensions

import com.morrowbone.mafiacards.app.data.AbstractCard

fun List<AbstractCard>.ids() = ArrayList(map { it.getId() })
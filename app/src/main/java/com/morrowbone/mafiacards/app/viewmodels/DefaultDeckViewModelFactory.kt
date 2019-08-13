package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.data.DeckRepository

class DefaultDeckViewModelFactory(
        private val repository: DeckRepository,
        private val cardCount: Int
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DefaultDeckViewModel(repository, cardCount) as T
}
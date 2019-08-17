package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.data.DeckRepository

class DeckViewModelFactory(
        private val repository: DeckRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DeckViewModel(repository) as T
}
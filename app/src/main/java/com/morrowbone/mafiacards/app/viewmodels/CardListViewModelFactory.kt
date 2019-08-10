package com.morrowbone.mafiacards.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.data.CardRepository

class CardListViewModelFactory(
        private val repository: CardRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CardListViewModel(repository) as T
}
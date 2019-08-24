package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.DefaultDeckAdapter
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.data.DefaultDeck
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.DefaultDecksViewModel
import kotlinx.android.synthetic.main.fragment_default_decks.*

class DefaultDecksFragment : Fragment() {
    private lateinit var adapter: DefaultDeckAdapter
    private val decksViewModel: DefaultDecksViewModel by viewModels {
        InjectorUtils.provideDefaultDecksViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_default_decks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        decksViewModel.decks.observe(this, Observer {
            val maxCount = it.size + DeckRepository.MISSED_DEFAULT_DECK
            val lastPlayerCount = 6
            counterView.minValue = DeckRepository.MISSED_DEFAULT_DECK + 1
            counterView.maxValue = maxCount
            counterView.cardCount = lastPlayerCount
            val deck = it[lastPlayerCount]
            updateAdapter(deck)
        })

        val layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = DefaultDeckAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        counterView.onCountChangedListener = {
            val newDeck = decksViewModel.decks.value!![it - counterView.minValue]
            updateAdapter(newDeck)
        }
    }

    private fun updateAdapter(deck: DefaultDeck) {
        adapter.updateCards(deck.cardsSet.defaultCards)
    }
}
package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.DefaultDeckAdapter
import com.morrowbone.mafiacards.app.data.Deck
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
        decksViewModel.deck.observe(this, Observer { deck ->
            if (deck == null) {
                return@Observer
            }
            updateAdapter(deck)
            save_btn.setOnClickListener {
                val shuffledDeck = Deck(DeckRepository.DEFAULT_DECK, deck.cardsSet).shuffle()
                decksViewModel.saveDeck(shuffledDeck)
                val direction = DefaultDecksFragmentDirections.actionDefaultDecksFragmentToTakeCardsFragment(shuffledDeck.deckId)
                findNavController().navigate(direction)
            }
        })
        val lastPlayerCount = 6
        decksViewModel.updateDeck(lastPlayerCount)

        val layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = DefaultDeckAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setRecycledViewPool(recyclerPool)

        counterView.minValue = DeckRepository.MISSED_DEFAULT_DECK + 1
        counterView.maxValue = DeckRepository.DEFAULT_DECKS_COUNT + counterView.minValue
        counterView.cardCount = lastPlayerCount
        counterView.onCountChangedListener = {
            decksViewModel.updateDeck(it)
        }
    }

    private fun updateAdapter(deck: DefaultDeck) {
        adapter.updateCards(deck.cardsSet.defaultCards)
    }

    companion object {
        val recyclerPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 10) }
    }
}
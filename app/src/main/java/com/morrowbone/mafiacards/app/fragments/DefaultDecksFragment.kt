package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.DefaultDeckAdapter
import com.morrowbone.mafiacards.app.data.*
import com.morrowbone.mafiacards.app.extensions.ids
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.BaseDeckViewModel
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_default_decks.*

class DefaultDecksFragment : BaseMafiaFragment() {
    private lateinit var adapter: DefaultDeckAdapter
    private val decksViewModel by viewModels<BaseDeckViewModel> {
        InjectorUtils.provideBaseViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_default_decks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lastPlayerCount = Prefs.getInt(LAST_PLAYER_COUNT, 6)
        val layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = DefaultDeckAdapter(mutableListOf(), this::showCardInfoDialog)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setRecycledViewPool(recyclerPool)

        counterView.minValue = DeckRepository.MISSED_DEFAULT_DECK + 1
        counterView.maxValue = DeckRepository.DEFAULT_DECKS_COUNT + counterView.minValue
        counterView.cardCount = lastPlayerCount
        counterView.onCountChangedListener = {
            update(DefaultDeckDao.getDefaultDeck(it)!!)
        }

        update(DefaultDeckDao.getDefaultDeck(lastPlayerCount)!!)
    }

    private fun update(deck: DefaultDeck) {
        updateAdapter(deck.cards)
        save_btn.setOnClickListener {
            val shuffledDeck = Deck(DeckRepository.DEFAULT_DECK, deck.cards.ids()).apply { shuffle() }
            decksViewModel.saveDeck(shuffledDeck)
            Prefs.putInt(LAST_PLAYER_COUNT, counterView.cardCount)
            val direction = DefaultDecksFragmentDirections.actionDefaultDecksFragmentToTakeCardsFragment(shuffledDeck.deckId)
            findNavController().navigate(direction)
        }
    }

    private fun updateAdapter(cards: List<DefaultCard>) {
        adapter.updateCards(cards)
    }

    companion object {
        private const val LAST_PLAYER_COUNT = "last_player_count"

        val recyclerPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 10) }
    }
}
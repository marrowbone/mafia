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
import com.morrowbone.mafiacards.app.adapter.DeckAdapter
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.utils.Utils
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModel
import kotlinx.android.synthetic.main.fragment_prev_game.*

class PrevGameFragment : Fragment() {
    private val deckViewModel: DeckViewModel by viewModels {
        InjectorUtils.provideDeckViewModelFactory(requireContext(), Utils.getLastUsedDeckId())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prev_game, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deckViewModel.cards.observe(this, Observer {
            val deckAdapter = DeckAdapter(it)
            recyclerView.adapter = deckAdapter
        })
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
    }
}
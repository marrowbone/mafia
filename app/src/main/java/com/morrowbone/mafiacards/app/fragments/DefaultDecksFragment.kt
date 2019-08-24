package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.DefaultDecksViewModel
import kotlinx.android.synthetic.main.fragment_default_decks.*

class DefaultDecksFragment : Fragment() {
    private val decksViewModel: DefaultDecksViewModel by viewModels {
        InjectorUtils.provideDefaultDecksViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_default_decks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        decksViewModel.decks.observe(this, Observer {
            val maxCount = it.size + DeckRepository.MISSED_DEFAULT_DECK
            counterView.minValue = DeckRepository.MISSED_DEFAULT_DECK + 1
            counterView.maxValue = maxCount
            counterView.cardCount = 6
        })
    }
}
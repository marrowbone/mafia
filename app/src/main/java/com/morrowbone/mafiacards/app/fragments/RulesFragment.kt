package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.RolesArrayAdapter
import com.morrowbone.mafiacards.app.application.MafiaApp
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import kotlinx.android.synthetic.main.fragment_rules.*

class RulesFragment : Fragment() {
    private val viewModel: CardListViewModel by viewModels {
        InjectorUtils.provideCardListViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rules, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.cards.observe(this, Observer {
            val adapter = RolesArrayAdapter(MafiaApp.instance!!, it)
            game_type_listview.adapter = adapter
        })
    }
}
package com.morrowbone.mafiacards.app.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.RolesArrayAdapter
import com.morrowbone.mafiacards.app.application.MafiaApp
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import kotlinx.android.synthetic.main.fragment_rules.*

class RulesActivity : FragmentActivity() {
    private val viewModel: CardListViewModel by lazy {
        val factory = InjectorUtils.provideCardListViewModelFactory(this)
        ViewModelProvider(this, factory).get(CardListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_rules)

        viewModel.cards.observe(this, Observer {
            val adapter = RolesArrayAdapter(MafiaApp.instance!!, it)
            game_type_listview.adapter = adapter
        })
    }
}

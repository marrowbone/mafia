package com.morrowbone.mafiacards.app.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.DeckAdapter
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.LastUsedDeckViewModel
import kotlinx.android.synthetic.main.activity_previous_game_info.*

class PreviousGameInfoActivity : FragmentActivity() {

    private val lastUsedDeckViewModel: LastUsedDeckViewModel by lazy {
        val viewModelFactory = InjectorUtils.provideLastUsedDeckViewModelFactory(this)
        ViewModelProvider(this, viewModelFactory).get(LastUsedDeckViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_game_info)

        lastUsedDeckViewModel.deck.observe(this, Observer {
            val deckAdapter = DeckAdapter(it)
            recyclerView.adapter = deckAdapter
        })
        recyclerView.layoutManager = GridLayoutManager(this, 3)

    }
}

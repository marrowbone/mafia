package com.morrowbone.mafiacards.app.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.DeckAdapter
import com.morrowbone.mafiacards.app.model.Deck
import kotlinx.android.synthetic.main.activity_previous_game_info.*

class PreviousGameInfoActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_game_info)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val deckAdapter = DeckAdapter(mDeck!!)
        recyclerView.adapter = deckAdapter
    }

    companion object {
        var mDeck: Deck? = null
    }
}

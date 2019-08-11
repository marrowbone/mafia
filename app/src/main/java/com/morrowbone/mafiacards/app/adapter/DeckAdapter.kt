package com.morrowbone.mafiacards.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.model.Card
import com.morrowbone.mafiacards.app.model.Deck
import com.morrowbone.mafiacards.app.views.CardView

class DeckAdapter(private val mDeck: Deck) : RecyclerView.Adapter<DeckAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_deck_card, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = mDeck.getCard(position)
        val cardView = holder.cardView
        cardView.setPlayerNum(position + 1)
        cardView.setCardImageResource(card.cartFrontSideImageId!!)
        cardView.setRoleNameResId(card.roleNameStringId)
    }

    override fun getItemCount(): Int {
        return mDeck.size()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView

        init {
            cardView = itemView.findViewById(R.id.cardView)
            cardView.setOnClickListener { cardView.flipit() }
            cardView.setOnLongClickListener {
                cardView.flipit()
                true
            }
        }
    }
}

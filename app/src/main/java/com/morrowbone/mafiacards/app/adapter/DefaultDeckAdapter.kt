package com.morrowbone.mafiacards.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArraySet
import androidx.recyclerview.widget.RecyclerView
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.DefaultCard
import com.morrowbone.mafiacards.app.views.CardView
import kotlinx.android.synthetic.main.view_default_decks_list_item.view.*

class DefaultDeckAdapter(private var cards: List<DefaultCard>) : RecyclerView.Adapter<DefaultDeckAdapter.ViewHolder>() {
    private var cardsSet = prepareCardsSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_default_decks_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cardsSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardsSet.valueAt(position)!!
        val cardCount = cards.count {
            it == card
        }
        holder.itemView.cardCountTextView.text = cardCount.toString()
        val cardView = holder.itemView.cardView
        cardView.show(CardView.CardSide.FRONT)
        cardView.setRoleName(card.getTitle())
        cardView.setCardImageResource(card.getImageResId())
    }

    private fun prepareCardsSet(): ArraySet<DefaultCard> = ArraySet(cards)

    fun updateCards(newCards: List<DefaultCard>) {
        cards = newCards
        cardsSet = prepareCardsSet()
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}
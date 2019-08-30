package com.morrowbone.mafiacards.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArraySet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.DefaultCard
import com.morrowbone.mafiacards.app.views.CardView
import kotlinx.android.synthetic.main.view_default_decks_list_item.view.*

class DefaultDeckAdapter(var cards: List<DefaultCard>) : ListAdapter<Pair<DefaultCard, Int>, DefaultDeckAdapter.ViewHolder>(DefaultCardDiffCallback()) {

    init {
        submitList(prepareCardsList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_default_decks_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val card = item.first
        val cardCount = item.second
        holder.itemView.cardCountTextView.text = cardCount.toString()
        val cardView = holder.itemView.cardView
        cardView.show(CardView.CardSide.FRONT)
        cardView.setRoleName(card.getTitle())
        cardView.setCardImageResource(card.getImageResId())
    }

    private fun prepareCardsList(): List<Pair<DefaultCard, Int>> {
        val setOfCards = ArraySet(cards).sortedBy {
            return@sortedBy when (it.cardId) {
                DefaultCard.CIVILIAN -> 1
                DefaultCard.DETECTIVE -> 2
                DefaultCard.DON_MAFIA -> 3
                DefaultCard.MAFIA -> 4
                DefaultCard.DOCTOR -> 5
                DefaultCard.MANIAC -> 6
                DefaultCard.IMMORTAL -> 7
                DefaultCard.PROSTITUTE -> 8
                else -> Int.MAX_VALUE
            }
        }
        val listOfPair: MutableList<Pair<DefaultCard, Int>> = mutableListOf()
        for (card in setOfCards) {
            val cardCount = cards.count {
                it == card
            }
            val pair = Pair(card, cardCount)
            listOfPair.add(pair)
        }
        return listOfPair
    }

    fun updateCards(newCards: List<DefaultCard>) {
        cards = newCards
        submitList(prepareCardsList())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}

private class DefaultCardDiffCallback : DiffUtil.ItemCallback<Pair<DefaultCard, Int>>() {

    override fun areItemsTheSame(oldItem: Pair<DefaultCard, Int>, newItem: Pair<DefaultCard, Int>): Boolean {
        return oldItem.first.cardId == newItem.first.cardId
    }

    override fun areContentsTheSame(oldItem: Pair<DefaultCard, Int>, newItem: Pair<DefaultCard, Int>): Boolean {
        return oldItem == newItem
    }
}
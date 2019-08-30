package com.morrowbone.mafiacards.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.Card
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.extensions.ids
import com.morrowbone.mafiacards.app.views.CounterView
import kotlinx.android.synthetic.main.view_creator_card.view.*

class CreateDeckAdapter(
        private val onCardCountChanged: (Int) -> Unit,
        private val editCardCallback: (cardId: String) -> Unit,
        private val cards: MutableList<AbstractCard> = mutableListOf()
) : RecyclerView.Adapter<CreateDeckAdapter.ViewHolder>() {
    val deckCards: MutableList<AbstractCard> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.view_creator_card, null))
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cards[position]
        val itemId = item.getId()

        holder.setTitle(item.getTitle())
        holder.setImageResource(item.getImageResId())

        var curCount = deckCards.count { it.getId() == itemId }
        val counterView = holder.counterView
        counterView.onCountChangedListener = {
            curCount = it
        }
        counterView.cardCount = curCount
        counterView.onDecrementListener = {
            removeCard(item)
            onDeckChanged()
        }
        counterView.onIncrementListener = {
            addCard(item)
            onDeckChanged()
        }

        val isUserCard = item is Card
        holder.editIcon.isVisible = isUserCard
        if (isUserCard) {
            holder.titleLayout.setOnClickListener {
                onEditClick(itemId)
            }
            holder.image.setOnClickListener { onEditClick(itemId) }
        } else {
            holder.titleLayout.setOnClickListener(null)
            holder.image.setOnClickListener(null)
        }
    }

    fun updateCards(cards: List<AbstractCard>) {
        this.cards.clear()
        this.cards.addAll(cards)
        val newDeckCards = deckCards.filter {
            cards.contains(it)
        }
        deckCards.clear()
        deckCards.addAll(newDeckCards)
        onDeckChanged()
        notifyDataSetChanged()
    }

    fun updateDeck(newCards: List<AbstractCard>) {
        deckCards.clear()
        deckCards.addAll(newCards)
        onDeckChanged()
        notifyDataSetChanged()
    }

    private fun onEditClick(cardId: String) {
        editCardCallback.invoke(cardId)
    }

    private fun onDeckChanged() {
        val cardCount = deckCards.size
        onCardCountChanged.invoke(cardCount)
    }

    private fun addCard(card: AbstractCard) {
        deckCards.add(card)
    }

    private fun removeCard(card: AbstractCard) {
        deckCards.remove(card)
    }

    fun clearDeck() {
        deckCards.clear()
        onDeckChanged()
        notifyDataSetChanged()
    }

    fun prepareDeck(): Deck {
        return Deck(DeckRepository.USER_DECK, deckCards.ids())
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val counterView: CounterView = itemView.counterView
        val editIcon: ImageView = itemView.editIcon
        val titleLayout: ViewGroup = itemView.card_title_layout
        val image: ImageView = itemView.card_image

        fun setTitle(title: String) {
            itemView.card_title.text = title
        }

        fun setImageResource(imageResId: Int) {
            itemView.card_image.setImageResource(imageResId)
        }
    }
}
package com.morrowbone.mafiacards.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.*
import kotlinx.android.synthetic.main.view_creator_card.view.*

class CreateDeckAdapter(
        private val onCardCountChanged: (Int) -> Unit,
        private val editCardCallback: (cardId: String) -> Unit,
        private val cards: MutableList<AbstractCard> = mutableListOf()
) : RecyclerView.Adapter<CreateDeckAdapter.ViewHolder>() {
    private val defaultCards = mutableListOf<DefaultCard>()
    private val userCards = mutableListOf<Card>()
    val deck = Deck(DeckRepository.USER_DECK, CardsSet(defaultCards, userCards))

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

        var curCount = deck.getCards().count { it.getId() == itemId }

        holder.setCardCount(curCount)
        fun updateArrows(cardCount: Int) {
            holder.decrement.isInvisible = cardCount <= 0
            holder.increment.isInvisible = cardCount >= 100
        }
        updateArrows(curCount)

        holder.decrement.setOnClickListener {
            curCount--
            holder.setCardCount(curCount)
            removeCard(item)
            onDeckChanged()
            updateArrows(curCount)
        }

        holder.increment.isInvisible = curCount == 100
        holder.increment.setOnClickListener {
            curCount++
            holder.setCardCount(curCount)
            addCard(item)
            onDeckChanged()
            updateArrows(curCount)
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
        val newUserCards = userCards.filter {
            cards.contains(it)
        }
        userCards.clear()
        userCards.addAll(newUserCards)
        onDeckChanged()
        notifyDataSetChanged()
    }

    fun updateDeck(deck: Deck?) {
        if (deck == null) {
            return
        }

        defaultCards.clear()
        userCards.clear()

        defaultCards.addAll(deck.cardsSet.defaultCards)
        userCards.addAll(deck.cardsSet.userCards)
        onDeckChanged()
        notifyDataSetChanged()
    }

    private fun onEditClick(cardId: String) {
        editCardCallback.invoke(cardId)
    }

    private fun onDeckChanged() {
        val cardCount = deck.getCards().size
        onCardCountChanged.invoke(cardCount)
    }

    private fun addCard(card: AbstractCard) {
        if (card is DefaultCard) {
            defaultCards.add(card)
        } else if (card is Card) {
            userCards.add(card)
        }
    }

    private fun removeCard(card: AbstractCard) {
        if (card is DefaultCard) {
            defaultCards.remove(card)
        } else if (card is Card) {
            userCards.remove(card)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val decrement: ImageView = itemView.decrement
        val increment: ImageView = itemView.increment
        val editIcon: ImageView = itemView.editIcon
        val titleLayout: ViewGroup = itemView.card_title_layout
        val image: ImageView = itemView.card_image

        fun setTitle(title: String) {
            itemView.card_title.text = title
        }

        fun setImageResource(imageResId: Int) {
            itemView.card_image.setImageResource(imageResId)
        }

        fun setCardCount(curCount: Int) {
            itemView.card_count.text = curCount.toString()
        }
    }
}
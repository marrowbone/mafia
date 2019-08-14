package com.morrowbone.mafiacards.app.adapter

/**
 * Created by morrow on 31.07.2014.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.Card
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DefaultCard

class CreateDeckArrayAdapter(context: Context, values: List<AbstractCard>, private val onCardCountChanged: (Int) -> Unit) : ArrayAdapter<AbstractCard>(context, layout_id, values) {
    private val defaultCards = mutableListOf<DefaultCard>()
    private val userCards = mutableListOf<Card>()
    val deck = Deck(defaultCards, userCards)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val item = getItem(position)!!
        val itemId = item.getId()
        val holder: Holder

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(layout_id, parent, false)

            holder = Holder()
            holder.title = convertView!!
                    .findViewById<View>(R.id.game_title) as TextView

            holder.image = convertView.findViewById<View>(R.id.game_type_image) as ImageView

            holder.decrement = convertView.findViewById(R.id.decrement)
            holder.increment = convertView.findViewById(R.id.increment)
            holder.cardCount = convertView.findViewById<View>(R.id.card_count) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as Holder
        }

        holder.title!!.text = item.getTitle()
        holder.image!!.setImageResource(item.getImageResId())

        var curCount = deck.getCards().count { it.getId() == itemId }

        holder.cardCount!!.text = curCount.toString()
        holder.decrement!!.setOnClickListener {
            if (curCount > 0) {
                curCount--
                holder.cardCount!!.text = curCount.toString()
                mCardCount--
                onCardCountChanged.invoke(mCardCount)
                removeCard(item)
            }
        }

        holder.increment!!.setOnClickListener {
            if (curCount < 100) {
                curCount++
                holder.cardCount!!.text = curCount.toString()

                mCardCount++
                onCardCountChanged.invoke(mCardCount)
                addCard(item)
            }
        }

        return convertView
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

    /**
     * View holder for the views we need access to
     */
    private class Holder {
        var title: TextView? = null
        var image: ImageView? = null
        var increment: View? = null
        var decrement: View? = null
        var cardCount: TextView? = null
    }

    companion object {
        private val layout_id = R.layout.view_creator_cart
        private var mCardCount: Int = 0
    }
}

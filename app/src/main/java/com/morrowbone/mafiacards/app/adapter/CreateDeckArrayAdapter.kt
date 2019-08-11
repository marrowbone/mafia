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
import com.morrowbone.mafiacards.app.activity.CreatorActivity
import com.morrowbone.mafiacards.app.model.Card

class CreateDeckArrayAdapter(context: Context, values: List<Card>) : ArrayAdapter<Card>(context, layout_id, values) {
    private val mInflater: LayoutInflater
    private val mContext: CreatorActivity

    init {
        mInflater = getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mContext = context as CreatorActivity
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val item = getItem(position)
        val holder: Holder

        if (convertView == null) {
            convertView = mInflater.inflate(layout_id, parent, false)

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

        holder.title!!.setText(item!!.roleNameStringId)
        holder.image!!.setImageResource(item.cartFrontSideImageId!!)

        val curCount = arrayOf(item.countInDeck)

        holder.cardCount!!.text = curCount[0].toString()
        holder.decrement!!.setOnClickListener {
            if (curCount[0] > 0) {
                curCount[0]--
                holder.cardCount!!.text = curCount[0].toString()
                item.countInDeck = curCount[0]

                mCardCount--
                mContext.setCardCount(mCardCount)
            }
        }

        holder.increment!!.setOnClickListener {
            if (curCount[0] < 100) {
                curCount[0]++
                item.countInDeck = curCount[0]
                holder.cardCount!!.text = curCount[0].toString()

                mCardCount++
                mContext.setCardCount(mCardCount)
            }
        }

        return convertView
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

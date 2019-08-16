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

class RolesArrayAdapter(context: Context, values: List<AbstractCard>) : ArrayAdapter<AbstractCard>(context, layout_id, values) {
    private val mInflater: LayoutInflater

    init {
        mInflater = getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val item = getItem(position)
        val holder: Holder

        if (convertView == null) {
            convertView = mInflater.inflate(layout_id, parent, false)

            holder = Holder()
            holder.title = convertView!!
                    .findViewById<View>(R.id.card_title) as TextView

            holder.description = convertView
                    .findViewById<View>(R.id.game_description) as TextView

            holder.image = convertView.findViewById<View>(R.id.game_type_image) as ImageView

            convertView.tag = holder
        } else {
            holder = convertView.tag as Holder
        }

        holder.title!!.text = item!!.getTitle()
        var description = item.getDescription();
        if (description.isBlank()) {
            description = context.getString(R.string.no_description)
        }
        holder.description!!.text = description
        holder.image!!.setImageResource(item.getImageResId())

        return convertView
    }

    /**
     * View holder for the views we need access to
     */
    private class Holder {
        var title: TextView? = null
        var description: TextView? = null
        var image: ImageView? = null
    }

    companion object {

        private val layout_id = R.layout.view_role
    }

}

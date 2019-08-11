package com.morrowbone.mafiacards.app.adapter

/**
 * Created by morrow on 03.06.2014.
 */

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.activity.ShowUserCartActivity
import com.morrowbone.mafiacards.app.model.Deck
import com.morrowbone.mafiacards.app.views.CardView
import com.morrowbone.mafiacards.app.views.CardView.CardSide

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentManager, private val mContext: Context, deck: Deck) : FragmentPagerAdapter(fm) {

    init {
        mDeck = deck
    }


    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return mDeck!!.size()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val cartNameStringId = mDeck!!.getCard(position).roleNameStringId

        return mContext.resources.getString(cartNameStringId)
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {
        private var mCardView: CardView? = null
        private var mHelpText: TextView? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_show_user_cart, container, false)
            mCardView = rootView.findViewById(R.id.card)
            mCardView!!.show(CardSide.BACKSIDE)

            val sectionNum = arguments!!.getInt(ARG_SECTION_NUMBER)
            val playerNum = sectionNum + 1
            val cartNameStringId = mDeck!!.getCard(sectionNum).roleNameStringId
            val imageId = mDeck!!.getCard(sectionNum).cartFrontSideImageId!!

            mCardView!!.setCardImageResource(imageId)
            mCardView!!.setRoleNameResId(cartNameStringId)
            mCardView!!.setPlayerNum(playerNum)


            mHelpText = rootView.findViewById(R.id.help_text)
            hideHelpField()

            rootView.setOnClickListener(this)
            rootView.setOnLongClickListener(this)

            return rootView
        }

        override fun onClick(view: View) {
            val id = view.id
            when (id) {
                R.id.root -> if (mCardView!!.state == CardSide.FRONT) {
                    hideHelpField()
                    mCardView!!.flipit()
                    showNextPage()
                } else {
                    showHelpField()
                }
            }
        }

        override fun onLongClick(view: View): Boolean {
            if (mCardView!!.state == CardSide.BACKSIDE) {
                hideHelpField()
                mCardView!!.flipit()
            } else {
                return false
            }
            return true
        }

        private fun showHelpField() {
            mHelpText!!.visibility = View.VISIBLE
        }

        private fun hideHelpField() {
            mHelpText!!.visibility = View.GONE
        }

        fun showNextPage() {
            val parent = activity as ShowUserCartActivity?
            parent!!.showNextPage()
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object {

        private var mDeck: Deck? = null
    }
}

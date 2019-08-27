package com.morrowbone.mafiacards.app.adapter

/**
 * Created by morrow on 03.06.2014.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.*
import androidx.lifecycle.Observer

import androidx.viewpager.widget.PagerAdapter

import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DefaultCard
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import com.morrowbone.mafiacards.app.views.CardView
import com.morrowbone.mafiacards.app.views.CardView.CardSide

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentManager, val deck: Deck) : FragmentStatePagerAdapter(fm) {

    private var cards: List<AbstractCard> = deck.getCards()

    override fun getItem(position: Int): Fragment {
        val card = cards[position]
        val isDefaultCard = card is DefaultCard
        return PlaceholderFragment.newInstance(position, card.getId(), isDefaultCard)
    }

    override fun getCount(): Int {
        return cards.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val card = cards[position]
        return card.getTitle()
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {
        private var mCardView: CardView? = null
        private var mHelpText: TextView? = null

        private val cardViewMode: CardListViewModel by viewModels {
            InjectorUtils.provideCardListViewModelFactory(requireContext())
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_show_user_cart, container, false)
            mCardView = rootView.findViewById(R.id.card)
            mCardView!!.show(CardSide.BACKSIDE)
            val sectionNum = arguments!!.getInt(ARG_SECTION_NUMBER)
            val cardId = arguments!!.getString(ARG_CARD_ID)!!
            val isDefaultCard = arguments!!.getBoolean(ARG_IS_DEFAILT_CARD)
            val playerNum = sectionNum + 1
            mCardView!!.setPlayerNum(playerNum)
            mHelpText = rootView.findViewById(R.id.help_text)
            hideHelpField()

            rootView.setOnClickListener(this)
            rootView.setOnLongClickListener(this)

            val cardLiveData = cardViewMode.getCard(cardId, isDefaultCard)
            cardLiveData.observe(this, object : Observer<AbstractCard> {
                override fun onChanged(abstractCard: AbstractCard?) {
                    if (abstractCard == null) {
                        return
                    }
                    cardLiveData.removeObserver(this)
                    mCardView!!.setCardImageResource(abstractCard.getImageResId())
                    mCardView!!.setRoleName(abstractCard.getTitle())
                    mCardView!!.setRoleType(abstractCard.roleType)
                }
            })
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

        private fun showNextPage() {
            cardShowListeners.forEach {
                it.invoke()
            }
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"
            private val ARG_CARD_ID = "card_id"
            private val ARG_IS_DEFAILT_CARD = "is_default_card"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int, cardId: String, isDefaultCard: Boolean): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                args.putString(ARG_CARD_ID, cardId)
                args.putBoolean(ARG_IS_DEFAILT_CARD, isDefaultCard)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object {
        val cardShowListeners: MutableList<() -> Unit> = mutableListOf()
    }
}

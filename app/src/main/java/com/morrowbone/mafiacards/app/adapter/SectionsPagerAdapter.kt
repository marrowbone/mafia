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
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.DefaultCard
import com.morrowbone.mafiacards.app.fragments.BaseMafiaFragment
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import com.morrowbone.mafiacards.app.viewmodels.CardViewModel
import com.morrowbone.mafiacards.app.views.CardView
import com.morrowbone.mafiacards.app.views.CardView.CardSide
import kotlinx.android.synthetic.main.fragment_show_user_cart.view.*

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentManager, val cards: List<AbstractCard>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val card = cards[position]
        return PlaceholderFragment.newInstance(position, card.getId())
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
    class PlaceholderFragment : BaseMafiaFragment(), View.OnClickListener, View.OnLongClickListener {
        private var mCardView: CardView? = null
        private var mHelpText: TextView? = null
        private lateinit var card: AbstractCard

        private val cardViewMode: CardViewModel by viewModels {
            InjectorUtils.provideCardViewModelFactory(requireContext())
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            val cardId = arguments!!.getString(ARG_CARD_ID)!!
            cardViewMode.setCardId(cardId)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_show_user_cart, container, false)
            mCardView = rootView.findViewById(R.id.card)
            mCardView!!.show(CardSide.BACKSIDE)
            val sectionNum = arguments!!.getInt(ARG_SECTION_NUMBER)
            val playerNum = sectionNum + 1
            mCardView!!.setPlayerNum(playerNum)
            mHelpText = rootView.findViewById(R.id.help_text)
            hideHelpField()

            rootView.setOnClickListener(this)
            rootView.card.setOnClickListener(this)
            rootView.setOnLongClickListener(this)
            rootView.card.setOnLongClickListener(this)

            cardViewMode.card.observe(this, object : Observer<AbstractCard> {
                override fun onChanged(abstractCard: AbstractCard?) {
                    if (abstractCard == null) {
                        return
                    }
                    mCardView!!.setCardImageResource(abstractCard.getImageResId())
                    mCardView!!.setRoleName(abstractCard.getTitle())
                    card = abstractCard
                }
            })
            return rootView
        }

        override fun onClick(view: View) {
            val id = view.id
            val isCardFrom = mCardView!!.state == CardSide.FRONT
            if (id == R.id.card && isCardFrom) {
                hideHelpField()
                showCardInfoDialog(card, allowEdit = false, isCancelable = false) {
                    mCardView!!.flipit()
                    showNextPage()
                }
            } else if (!isCardFrom) {
                showHelpField()
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

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int, cardId: String): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                args.putString(ARG_CARD_ID, cardId)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object {
        val cardShowListeners: MutableList<() -> Unit> = mutableListOf()
    }
}

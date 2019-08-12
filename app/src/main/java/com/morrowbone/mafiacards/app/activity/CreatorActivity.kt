package com.morrowbone.mafiacards.app.activity

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.CreateDeckArrayAdapter
import com.morrowbone.mafiacards.app.model.Card
import com.morrowbone.mafiacards.app.model.Deck
import com.morrowbone.mafiacards.app.model.roles.Civilian
import com.morrowbone.mafiacards.app.model.roles.Detective
import com.morrowbone.mafiacards.app.model.roles.Doctor
import com.morrowbone.mafiacards.app.model.roles.DonMafia
import com.morrowbone.mafiacards.app.model.roles.Immortal
import com.morrowbone.mafiacards.app.model.roles.Mafia
import com.morrowbone.mafiacards.app.model.roles.Maniac
import com.morrowbone.mafiacards.app.utils.CardsUtils

class CreatorActivity : Activity(), View.OnClickListener {
    private var mSaveButton: Button? = null
    private var mCardCountTextView: TextView? = null
    private var mListView: ListView? = null
    private var mArrayAdapter: ArrayAdapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        mRolesList = CardsUtils.getRoles()
        mArrayAdapter = CreateDeckArrayAdapter(this, mRolesList!!)
        mListView = findViewById<View>(R.id.listview) as ListView
        mListView!!.adapter = mArrayAdapter

        mCardCountTextView = findViewById<View>(R.id.card_count_textview) as TextView
        mCardCountTextView!!.text = mCardCount.toString()

        mSaveButton = findViewById<View>(R.id.save_btn) as Button
        mSaveButton!!.setOnClickListener(this)
    }

    fun setCardCount(cardCount: Int) {
        mCardCount = cardCount
        mCardCountTextView!!.text = cardCount.toString()
    }

    override fun onClick(v: View) {
        if (mCardCount > 0) {
            val deck = convertToDeck(mRolesList!!)
            val intent = ShowUserCartActivity.getIntent(this, deck)
            startActivity(intent)
        } else {
            showErrorDialog(R.string.error_add_some_cards)
        }
    }

    private fun showErrorDialog(title: Int) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.positive_button_text) { _, _ -> }
        builder.show()
    }

    companion object {
        private var mCardCount = 0
        private var mRolesList: List<Card>? = null

        private fun convertToDeck(cards: List<Card>): Deck {
            val deck = Deck()
            for (card in cards) {
                val cardCount: Int?
                if (card.javaClass == Civilian::class.java) {
                    cardCount = card.countInDeck
                    for (i in 0 until cardCount) {
                        deck.addCard(Civilian())
                    }
                } else if (card.javaClass == Mafia::class.java) {
                    cardCount = card.countInDeck
                    for (i in 0 until cardCount) {
                        deck.addCard(Mafia())
                    }
                } else if (card.javaClass == Detective::class.java) {
                    cardCount = card.countInDeck
                    for (i in 0 until cardCount) {
                        deck.addCard(Detective())
                    }
                } else if (card.javaClass == Doctor::class.java) {
                    cardCount = card.countInDeck
                    for (i in 0 until cardCount) {
                        deck.addCard(Doctor())
                    }
                } else if (card.javaClass == DonMafia::class.java) {
                    cardCount = card.countInDeck
                    for (i in 0 until cardCount) {
                        deck.addCard(DonMafia())
                    }
                } else if (card.javaClass == Immortal::class.java) {
                    cardCount = card.countInDeck
                    for (i in 0 until cardCount) {
                        deck.addCard(Immortal())
                    }
                } else if (card.javaClass == Maniac::class.java) {
                    cardCount = card.countInDeck
                    for (i in 0 until cardCount) {
                        deck.addCard(Maniac())
                    }
                }
            }
            return deck
        }
    }
}

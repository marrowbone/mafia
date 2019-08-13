package com.morrowbone.mafiacards.app.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.CreateDeckArrayAdapter
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel

class CreatorActivity : FragmentActivity(), View.OnClickListener {
    private var mSaveButton: Button? = null
    private var mCardCountTextView: TextView? = null
    private var mListView: ListView? = null
    private lateinit var mArrayAdapter: CreateDeckArrayAdapter
    private var cardCount = 0
    private val viewModel: CardListViewModel by lazy {
        val factory = InjectorUtils.provideCardListViewModelFactory(this)
        ViewModelProvider(this, factory).get(CardListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        viewModel.cards.observe(this, Observer {
            mArrayAdapter = CreateDeckArrayAdapter(this, it)
            mListView!!.adapter = mArrayAdapter
        })

        mListView = findViewById<View>(R.id.listview) as ListView


        mCardCountTextView = findViewById<View>(R.id.card_count_textview) as TextView
        mCardCountTextView!!.text = cardCount.toString()

        mSaveButton = findViewById<View>(R.id.save_btn) as Button
        mSaveButton!!.setOnClickListener(this)
    }

    fun setCardCount(cardCount: Int) {
        this.cardCount = cardCount
        mCardCountTextView!!.text = cardCount.toString()
    }

    override fun onClick(v: View) {
        if (cardCount > 0) {
            val intent = ShowUserCartActivity.getIntent(this, mArrayAdapter.deck)
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
}

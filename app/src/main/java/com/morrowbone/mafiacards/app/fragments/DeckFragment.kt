package com.morrowbone.mafiacards.app.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.CreateDeckArrayAdapter
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModel
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeckFragment : Fragment() {
    private lateinit var arrayAdapter: CreateDeckArrayAdapter
    private val cardViewModel: CardListViewModel by viewModels {
        InjectorUtils.provideCardListViewModelFactory(requireContext())
    }
    private val deckViewModel: DeckViewModel by viewModels {
        InjectorUtils.provideDeckViewModelFactory(requireContext())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cardViewModel.cards.observe(this, Observer {
            fun onCardCountChanged(cardCount: Int) {
                card_count_textview.text = cardCount.toString()
            }
            arrayAdapter = CreateDeckArrayAdapter(
                    requireContext(),
                    it,
                    ::onCardCountChanged,
                    this@DeckFragment::showEditDialog)
            listview.adapter = arrayAdapter

            GlobalScope.launch(IO) {
                val draftUserDeck = deckViewModel.getUserDeck(DeckRepository.USER_DECK_DRAFT)
                withContext(Main) {
                    arrayAdapter.updateDeck(draftUserDeck)
                }
            }
        })

        card_count_textview!!.text = 0.toString()
        save_btn.setOnClickListener {
            onTakeCardsClick()
        }

        addCardButton.setOnClickListener {
            AddCardDialogFragment().show(fragmentManager!!, "add_card_dialog")
        }
    }

    override fun onPause() {
        GlobalScope.launch(IO) {
            val userDeckDraft = arrayAdapter.deck.copy(deckId = DeckRepository.USER_DECK_DRAFT)
            deckViewModel.saveDeck(userDeckDraft)
        }
        super.onPause()
    }

    private fun showEditDialog(cardId: String) {
        EditCardDialogFragment.newInstance(cardId).show(fragmentManager!!, "edit_card")
    }

    private fun onTakeCardsClick() {
        val cardCount = arrayAdapter.deck.getCards().size
        if (cardCount > 0) {
            val deck = arrayAdapter.deck.shuffle()
            GlobalScope.launch(Dispatchers.IO) {
                deckViewModel.saveDeck(deck)
                withContext(Dispatchers.Main) {
                    val direction = DeckFragmentDirections.actionDeckFragmentToTakeCardsFragment(deck.deckId)
                    findNavController().navigate(direction)
                }
            }
        } else {
            showErrorDialog(R.string.error_add_some_cards)
        }
    }

    private fun showErrorDialog(title: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.positive_button_text) { _, _ -> }
        builder.show()
    }
}
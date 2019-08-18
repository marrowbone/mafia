package com.morrowbone.mafiacards.app.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.CreateDeckAdapter
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModel
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateDeckFragment : Fragment() {
    private val cardViewModel: CardListViewModel by viewModels {
        InjectorUtils.provideCardListViewModelFactory(requireContext())
    }
    private val deckViewModel: DeckViewModel by viewModels {
        InjectorUtils.provideDeckViewModelFactory(requireContext())
    }
    private lateinit var adapter: CreateDeckAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CreateDeckAdapter(this::onCardCountChanged, this::showEditDialog)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        cardViewModel.cards.observe(this, Observer {
            adapter.updateCards(it)
        })

        onCardCountChanged(0)
        save_btn.setOnClickListener {
            onTakeCardsClick()
        }

        addCardButton.setOnClickListener {
            AddCardDialogFragment().show(fragmentManager!!, "add_card_dialog")
        }
        clearDeckButton.setOnClickListener {
            showConfirmClearDialog();
        }
    }

    private fun showConfirmClearDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.clear_deck)
                .setMessage(R.string.clear_deck_dialog_message)
                .setPositiveButton(R.string.clear) { _, _ ->
                    adapter.clearDeck()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> {} }
                .create()
        alertDialog.show()
    }

    override fun onPause() {
        val userDeckDraft = adapter.deck.copy(deckId = DeckRepository.USER_DECK_DRAFT)
        deckViewModel.saveDeck(userDeckDraft)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(IO) {
            val draftUserDeck = deckViewModel.getUserDeck(DeckRepository.USER_DECK_DRAFT)
            withContext(Main) {
                adapter.updateDeck(draftUserDeck)
            }
        }
    }

    private fun onCardCountChanged(cardCount: Int) {
        card_count_textview.text = cardCount.toString()
        clearDeckButton.isVisible = cardCount > 0
    }

    private fun showEditDialog(cardId: String) {
        EditCardDialogFragment.newInstance(cardId).show(fragmentManager!!, "edit_card")
    }

    private fun onTakeCardsClick() {
        val cardCount = adapter.deck.getCards().size
        if (cardCount > 0) {
            val deck = adapter.deck.shuffle()
            deckViewModel.saveDeck(deck)
            val direction = CreateDeckFragmentDirections.actionDeckFragmentToTakeCardsFragment(deck.deckId)
            findNavController().navigate(direction)
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
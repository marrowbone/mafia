package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.CreateDeckAdapter
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModel
import kotlinx.android.synthetic.main.fragment_deck.*

class CreateDeckFragment : BaseMafiaFragment() {
    private val cardViewModel: CardListViewModel by viewModels {
        InjectorUtils.provideCardListViewModelFactory(requireContext())
    }
    private val deckViewModel: DeckViewModel by viewModels {
        InjectorUtils.provideDeckViewModelFactory(requireContext(), DeckRepository.USER_DECK_DRAFT)
    }
    private lateinit var adapter: CreateDeckAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CreateDeckAdapter(this::onCardCountChanged, this::onCardClick)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
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

        deckViewModel.cards.observe(this, Observer {
            adapter.updateDeck(it)
        })
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
        val userDeckDraft = adapter.prepareDeck().copy(deckId = DeckRepository.USER_DECK_DRAFT)
        deckViewModel.saveDeck(userDeckDraft)
        super.onPause()
    }

    private fun onCardCountChanged(cardCount: Int) {
        val text = String.format(requireContext().getString(R.string.cards_in_deck_deck_constructor), cardCount)
        val finalText = HtmlCompat.fromHtml(text, FROM_HTML_MODE_COMPACT)
        cards_in_deck.text = finalText
        clearDeckButton.isVisible = cardCount > 0
    }

    private fun onCardClick(card: AbstractCard) {
        showCardInfoDialog(card)
    }

    private fun onTakeCardsClick() {
        val deck = adapter.prepareDeck()
        val cardCount = deck.cardIds.size
        if (cardCount > 0) {
            deck.shuffle()
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
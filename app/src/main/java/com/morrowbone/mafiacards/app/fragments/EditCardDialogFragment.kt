package com.morrowbone.mafiacards.app.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.Card
import kotlinx.android.synthetic.main.fragment_dialog_card.*

class EditCardDialogFragment : AddCardDialogFragment() {
    companion object {
        private const val CARD_ID = "card_id"

        fun newInstance(cardId: String): EditCardDialogFragment {
            val fragment = EditCardDialogFragment()
            val arguments = Bundle().apply {
                putString(CARD_ID, cardId)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var card: Card

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        okButton.setText(R.string.save)
        super.onViewCreated(view, savedInstanceState)
        deleteButton.isVisible = true
        deleteButton.setOnClickListener {
            showConfirmDeleteDialog()
        }
        val cardId = requireArguments().getString(CARD_ID)!!
        viewModel.getUserCard(cardId).observe(this, Observer {
            card = it
            cardTitleEditText.setText(card.getTitle())
            cardDescriptionEditText.setText(card.getDescription())
        })
        okButton.setOnClickListener {
            saveChanges()
            dismiss()
        }
        cardDescriptionEditText.addTextChangedListener {
            okButton.isInvisible = !isNeedShowOkButton()
        }
        dialog_title.setText(R.string.change_card)
    }

    private fun saveChanges() {
        val cardTitle = cardTitleEditText.text.toString()
        val cardDescription = cardDescriptionEditText.text.toString()
        val changedCard = card.copy(name = cardTitle, info = cardDescription)
        viewModel.saveChanged(changedCard)
    }

    private fun showConfirmDeleteDialog() {
        val confirmDeleteDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(getString(R.string.delete_dialog_message, card.name))
                .setPositiveButton(R.string.delete) { _, _ ->
                    viewModel.deleteCard(card)
                    dismiss()
                }.setNegativeButton(android.R.string.cancel, object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                    }

                }).create()
        confirmDeleteDialog.show()
    }

    override fun isNeedShowOkButton(titleString: String, descriptionString: String): Boolean {
        val titleChanged = titleString != card.getTitle()
        val descriptionChanged = descriptionString != card.getDescription()
        val cardChanged = titleChanged || descriptionChanged
        return super.isNeedShowOkButton(titleString, descriptionString) && cardChanged
    }
}
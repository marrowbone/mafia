package com.morrowbone.mafiacards.app.fragments

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.Card

abstract class BaseMafiaFragment : Fragment() {
    protected fun showCardInfoDialog(card: AbstractCard, allowEdit: Boolean = true, isCancelable: Boolean = true, okCallback: (() -> Unit)? = null) {
        var description = card.getDescription()
        if (description.isBlank()) {
            description = getString(R.string.no_description)
        }
        val builder = AlertDialog.Builder(requireContext())
                .setTitle(card.getTitle())
                .setMessage(description)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    okCallback?.invoke()
                }
                .setCancelable(isCancelable)
        if (card is Card && allowEdit) {
            builder.setNeutralButton(R.string.edit) { _, _ ->
                EditCardDialogFragment.newInstance(card.getId()).show(fragmentManager!!, "edit_card")
            }
        }
        builder.show()
    }
}
package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.Card
import com.morrowbone.mafiacards.app.data.NEUTRAL
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.CardListViewModel
import kotlinx.android.synthetic.main.fragment_dialog_card.*
import java.util.*

open class AddCardDialogFragment : DialogFragment() {
    protected val viewModel: CardListViewModel by viewModels {
        InjectorUtils.provideCardListViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_card, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cardTitleEditText.addTextChangedListener {
            okButton.isInvisible = !isNeedShowOkButton()
        }
        okButton.setOnClickListener {
            createCard()
            dismiss()
        }
        cancelButton.setOnClickListener { dismiss() }
    }

    protected fun isNeedShowOkButton(): Boolean {
        val titleString: String = cardTitleEditText.text!!.toString()
        val descriptionString: String = cardDescriptionEditText.text!!.toString()
        return isNeedShowOkButton(titleString, descriptionString)
    }

    protected open fun isNeedShowOkButton(titleString: String, descriptionString: String): Boolean {
        return titleString.isNotBlank()
    }

    private fun createCard() {
        val cardId = UUID.randomUUID().toString()
        val cardTitle = cardTitleEditText.text.toString()
        val cardDescription = cardDescriptionEditText.text.toString()
        val card = Card(cardId, cardTitle, cardDescription, NEUTRAL)
        viewModel.addCard(card)
    }


    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes as WindowManager.LayoutParams
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params
    }
}
package com.morrowbone.mafiacards.app.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.utils.Utils
import com.morrowbone.mafiacards.app.viewmodels.DeckViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), NavController.OnDestinationChangedListener {

    private val deckViewModel: DeckViewModel by viewModels {
        InjectorUtils.provideDeckViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prev_game.isVisible = Utils.hasLastUsedDeck()
    }

    override fun onResume() {
        super.onResume()
        initClickListeners()
        findNavController().addOnDestinationChangedListener(this)
    }

    private fun initClickListeners() {
        initPrevGameBtn()
        initPlayBtn()
        initCreatorBtn()
        initRulesBtn()
    }

    override fun onPause() {
        findNavController().removeOnDestinationChangedListener(this)
        super.onPause()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (controller.graph.startDestination != destination.id) {
            clearClickListeners()
        }
    }

    private fun clearClickListeners() {
        play_btn.setOnClickListener(null)
        creator_btn.setOnClickListener(null)
        rules_btn.setOnClickListener(null)
        prev_game.setOnClickListener(null)
    }

    private fun initPrevGameBtn() {
        prev_game.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_prevGameFragment)
        }
    }

    private fun initRulesBtn() {
        rules_btn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_rulesFragment)
        }
    }

    private fun initCreatorBtn() {
        creator_btn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_deckFragment)
        }
    }

    private fun initPlayBtn() {
        play_btn.setOnClickListener {
            clearClickListeners()
            val max = 13
            val min = 3

            val dialogContent = layoutInflater.inflate(R.layout.dialog_num_of_player, null)
            val editText = dialogContent.findViewById<View>(R.id.input_field) as EditText
            val startValue = 6
            editText.setText(startValue.toString())

            val seekBar = dialogContent.findViewById<View>(R.id.search_bar) as SeekBar

            seekBar.max = max - min
            seekBar.progress = startValue - min

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    val cardCount = seekBar.progress + min
                    editText.setText(cardCount.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                }

                override fun afterTextChanged(editable: Editable) {
                    val value = editText.text.toString()
                    if (value != "") {
                        val progress = Integer.valueOf(editText.text.toString()) - min
                        seekBar.progress = progress
                    }
                }
            })
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle(R.string.dialog_number_of_player)
            builder.setView(dialogContent).setCancelable(true)
            builder.setPositiveButton(R.string.dialog_number_of_player_positive_btn) { _, _ ->
                if (editText.text.toString() != "") {
                    val cartCount = Integer.valueOf(editText.text.toString())
                    if (cartCount < min) {
                        showMessage(R.string.error, R.string.wrong_player_count)
                    } else {
                        val defaultDeck = deckViewModel.getDefaultDeck(cartCount)
                        val deck = Deck(DeckRepository.DEFAULT_DECK, defaultDeck.cardsSet).shuffle()
                        deckViewModel.saveDeck(deck)
                        val direction = MainFragmentDirections.actionMainFragmentToTakeCardsFragment(deck.deckId)
                        findNavController().navigate(direction)
                    }

                }
            }
            builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
            builder.setOnDismissListener {
                initClickListeners()
            }
            builder.show()
        }
    }

    private fun showMessage(titleResId: Int, messageResId: Int) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(titleResId).setMessage(messageResId).setCancelable(false)
        builder.setPositiveButton(R.string.positive_button_text) { _, _ -> }
        builder.show()
    }
}
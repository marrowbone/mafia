package com.morrowbone.mafiacards.app.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.DeckRepository
import com.morrowbone.mafiacards.app.data.DefaultDeck
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.DefaultDeckViewModel
import com.morrowbone.mafiacards.app.viewmodels.LastUsedDeckViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {
    private val lastUsedDeckViewModel: LastUsedDeckViewModel by viewModels {
        InjectorUtils.provideLastUsedDeckViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lastUsedDeckViewModel.deck.observe(this, Observer {
            prev_game.isVisible = it != null
        })

        initPrevGameBtn()
        initPlayBtn()
        initCreatorBtn()
        initRulesBtn()
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
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(R.string.dialog_number_of_player)
            builder.setView(dialogContent).setCancelable(true)
            builder.setPositiveButton(R.string.dialog_number_of_player_positive_btn) { _, _ ->
                if (editText.text.toString() != "") {
                    val cartCount = Integer.valueOf(editText.text.toString())
                    if (cartCount < min) {
                        showMessage(R.string.error, R.string.wrong_player_count)
                    } else {
                        val deckViewModelFactory = InjectorUtils.provideDefaultDeckViewModelFactory(requireContext(), cartCount)
                        val deckViewModel = ViewModelProvider(this, deckViewModelFactory).get(DefaultDeckViewModel::class.java)
                        deckViewModel.deck.observe(this, object : Observer<DefaultDeck> {
                            override fun onChanged(defaultDeck: DefaultDeck?) {
                                if (defaultDeck == null) {
                                    return
                                }
                                val deck = defaultDeck.deck.apply { shuffle() }
                                GlobalScope.launch(Dispatchers.IO) {
                                    val repository = InjectorUtils.getDeckRepository(requireContext())
                                    repository.insertDeck(deck)
                                    withContext(Dispatchers.Main) {
                                        val direction = MainFragmentDirections.actionMainFragmentToTakeCardsFragment(deck.deckId)
                                        findNavController().navigate(direction)
                                    }
                                }
                                deckViewModel.deck.removeObserver(this)
                            }
                        })
                    }

                }
            }

            builder.show()
        }
    }

    private fun showMessage(titleResId: Int, messageResId: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(titleResId).setMessage(messageResId).setCancelable(false)
        builder.setPositiveButton(R.string.positive_button_text) { _, _ -> }
        builder.show()
    }
}
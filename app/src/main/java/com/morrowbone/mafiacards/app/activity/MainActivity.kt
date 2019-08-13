package com.morrowbone.mafiacards.app.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.DefaultDeck
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import com.morrowbone.mafiacards.app.viewmodels.DefaultDeckViewModel
import com.morrowbone.mafiacards.app.viewmodels.LastUsedDeckViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    private val lastUsedDeckViewModel: LastUsedDeckViewModel by lazy {
        val viewModelFactory = InjectorUtils.provideLastUsedDeckViewModelFactory(this)
        ViewModelProvider(this, viewModelFactory).get(LastUsedDeckViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lastUsedDeckViewModel.deck.observe(this, Observer {
            prev_game.isVisible = it != null
        })

        val title = findViewById<View>(R.id.title) as TextView
        title.text = "Mafia"

        initPrevGameBtn()

        initPlayBtn()

        initCreatorBtn()

        initRulesBtn()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initPrevGameBtn() {
        prev_game.setOnClickListener {
            val intent = Intent(this@MainActivity, PreviousGameInfoActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initRulesBtn() {
        val rulesBtn = findViewById<View>(R.id.rules_btn) as Button
        rulesBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, RulesActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initCreatorBtn() {
        val creatorBtn = findViewById<View>(R.id.creator_btn) as Button
        creatorBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, CreatorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
    }

    private fun initPlayBtn() {
        val playBtn = findViewById<View>(R.id.play_btn) as Button
        playBtn.setOnClickListener {
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
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(R.string.dialog_number_of_player)
            builder.setView(dialogContent).setCancelable(true)
            builder.setPositiveButton(R.string.dialog_number_of_player_positive_btn) { _, _ ->
                if (editText.text.toString() != "") {
                    val cartCount = Integer.valueOf(editText.text.toString())
                    if (cartCount < min) {
                        showMessage(R.string.error, R.string.wrong_player_count)
                    } else {
                        val deckViewModelFactory = InjectorUtils.provideDefaultDeckViewModelFactory(this, cartCount)
                        val deckViewModel = ViewModelProvider(this, deckViewModelFactory).get(DefaultDeckViewModel::class.java)
                        deckViewModel.deck.observe(this, object : Observer<DefaultDeck> {
                            override fun onChanged(deck: DefaultDeck?) {
                                if (deck == null) {
                                    return
                                }
                                val intent = ShowUserCartActivity.getIntent(this@MainActivity, deck.deck)
                                startActivity(intent)
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
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(titleResId).setMessage(messageResId).setCancelable(false)
        builder.setPositiveButton(R.string.positive_button_text) { _, _ -> }

        builder.show()
    }
}

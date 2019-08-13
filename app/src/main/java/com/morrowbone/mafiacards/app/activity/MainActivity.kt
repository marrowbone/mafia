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
import androidx.fragment.app.FragmentActivity
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private var prevGameBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val title = findViewById<View>(R.id.title) as TextView
        title.text = "Mafia"

        initPrevGameBtn()

        initPlayBtn()

        initCreatorBtn()

        initRulesBtn()
    }

    override fun onResume() {
        super.onResume()
        if (PreviousGameInfoActivity.mDeck != null) {
            prevGameBtn!!.visibility = View.VISIBLE
        } else {
            prevGameBtn!!.visibility = View.GONE
        }
    }

    private fun initPrevGameBtn() {
        prevGameBtn = findViewById(R.id.prev_game)
        prevGameBtn!!.setOnClickListener {
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
            builder.setPositiveButton(R.string.dialog_number_of_player_positive_btn) { arg0, arg1 ->
                if (editText.text.toString() != "") {

                    val cartCount = Integer.valueOf(editText.text.toString())
                    if (cartCount < min) {
                        showMessage(R.string.error, R.string.wrong_player_count)
                    } else {
                        GlobalScope.launch(Main) {
                            val task = async(IO) {
                                println("InjectorUtils.getDeckRepository")
                                val deckRepository = InjectorUtils.getDeckRepository(this@MainActivity);
                                deckRepository.getDefaultDeck(cartCount)
                            }
                            println("before task.await()")
                            val deck = task.await()
                            println("after task.await()")
                            val intent = ShowUserCartActivity.getIntent(this@MainActivity, deck.deck)
                            startActivity(intent)
                        }
                    }

                }
            }

            builder.show()
        }
    }

    private fun showMessage(titleResId: Int, messageResId: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(titleResId).setMessage(messageResId).setCancelable(false)
        builder.setPositiveButton(R.string.positive_button_text) { arg0, arg1 -> }

        builder.show()
    }
}

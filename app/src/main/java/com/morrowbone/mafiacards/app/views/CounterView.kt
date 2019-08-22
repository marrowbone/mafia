package com.morrowbone.mafiacards.app.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import com.morrowbone.mafiacards.app.R
import kotlinx.android.synthetic.main.view_counter.view.*

class CounterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    var cardCount = 6
        set(value) {
            field = value
            updateUI()
        }
    var onCountChangedListener: OnCountChangedListener? = null
    var onIncrementListener: OnIncrement? = null
    var onDecrementListener: OnDecrement? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_counter, this)

        increment.setOnClickListener {
            cardCount++
            onIncrementListener?.invoke()
            onChanged()
            updateUI()
        }

        decrement.setOnClickListener {
            cardCount--
            onChanged()
            onDecrementListener?.invoke()
            updateUI()
        }
    }

    private fun onChanged() {
        onCountChangedListener?.invoke(cardCount)
    }

    private fun updateUI() {
        decrement.isInvisible = cardCount <= 0
        increment.isInvisible = cardCount >= 100
        card_count.text = cardCount.toString()
    }
}

typealias OnCountChangedListener = (cardCount: Int) -> Unit
typealias OnIncrement = () -> Unit
typealias OnDecrement = () -> Unit
package com.morrowbone.mafiacards.app.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.morrowbone.mafiacards.app.R

class CardView : FrameLayout {
    var state: CardSide? = null
        private set

    private var mCartFrontView: View? = null
    private var mCartBackSideView: View? = null

    private val accelerator = AccelerateInterpolator()
    private val decelerator = DecelerateInterpolator()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_card, this)

        mCartFrontView = findViewById(R.id.card_view_front)
        mCartBackSideView = findViewById(R.id.card_view_backside)
        show(CardSide.BACKSIDE)
    }

    fun show(state: CardSide) {
        if (state == CardSide.BACKSIDE) {
            mCartFrontView!!.visibility = View.INVISIBLE
            mCartBackSideView!!.visibility = View.VISIBLE
            this.state = CardSide.BACKSIDE
        } else {
            mCartFrontView!!.visibility = View.VISIBLE
            mCartBackSideView!!.visibility = View.INVISIBLE
            this.state = CardSide.FRONT
        }
    }

    fun flipit() {
        val visibleView: View?
        val invisibleView: View?
        if (state == CardSide.BACKSIDE) {
            visibleView = mCartBackSideView
            invisibleView = mCartFrontView
            state = CardSide.FRONT
        } else {
            invisibleView = mCartBackSideView
            visibleView = mCartFrontView
            state = CardSide.BACKSIDE
        }
        val visToInvis = ObjectAnimator.ofFloat(visibleView, "rotationY", 0f, 90f)
        visToInvis.duration = 500
        visToInvis.interpolator = accelerator
        val invisToVis = ObjectAnimator.ofFloat(invisibleView, "rotationY",
                -90f, 0f)
        invisToVis.duration = 500
        invisToVis.interpolator = decelerator
        visToInvis.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(anim: Animator) {
                visibleView!!.visibility = View.INVISIBLE
                invisToVis.start()
                invisibleView!!.visibility = View.VISIBLE
            }
        })
        visToInvis.start()
    }

    fun setCardImageResource(imageId: Int) {
        val frontSideImage = findViewById<ImageView>(R.id.card_frontside_image)
        frontSideImage.setImageResource(imageId)
    }

    fun setRoleNameResId(cartNameStringId: Int) {
        val titleTextView = findViewById<TextView>(R.id.role)
        titleTextView.setText(cartNameStringId)
    }

    fun setPlayerNum(playerNum: Int?) {
        val playerNumber = findViewById<TextView>(R.id.player_number)
        playerNumber.text = playerNum!!.toString()
    }

    enum class CardSide {
        BACKSIDE, FRONT
    }
}

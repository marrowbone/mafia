package com.morrowbone.mafiacards.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.morrowbone.mafiacards.app.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

public class CardView extends FrameLayout {
    private CardSide mState;

    private View mCartFrontView;
    private View mCartBackSideView;

    private volatile Interpolator accelerator = new AccelerateInterpolator();
    private volatile Interpolator decelerator = new DecelerateInterpolator();
    
    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_card, this);

        mCartFrontView = findViewById(R.id.card_view_front);
        mCartBackSideView = findViewById(R.id.card_view_backside);
        show(CardSide.BACKSIDE);
    }

    public void show(CardSide state) {
        if (state == CardSide.BACKSIDE) {
            mCartFrontView.setVisibility(View.GONE);
            mCartBackSideView.setVisibility(View.VISIBLE);
            mState = CardSide.BACKSIDE;
        } else {
            mCartFrontView.setVisibility(View.VISIBLE);
            mCartBackSideView.setVisibility(View.GONE);
            mState = CardSide.FRONT;
        }
    }

    public void flipit() {
        final View visibleView;
        final View invisibleView;
        if (mState == CardSide.BACKSIDE) {
            visibleView = mCartBackSideView;
            invisibleView = mCartFrontView;
            mState = CardSide.FRONT;
        } else {
            invisibleView = mCartBackSideView;
            visibleView = mCartFrontView;
            mState = CardSide.BACKSIDE;
        }
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibleView, "rotationY", 0f, 90f);
        visToInvis.setDuration(500);
        visToInvis.setInterpolator(accelerator);
        final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(invisibleView, "rotationY",
                -90f, 0f);
        invisToVis.setDuration(500);
        invisToVis.setInterpolator(decelerator);
        visToInvis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                visibleView.setVisibility(View.GONE);
                invisToVis.start();
                invisibleView.setVisibility(View.VISIBLE);
            }
        });
        visToInvis.start();
    }

    public void setCardImageResource(int imageId) {
        ImageView frontSideImage = findViewById(R.id.card_frontside_image);
        frontSideImage.setImageResource(imageId);
    }

    public void setRoleNameResId(int cartNameStringId) {
        TextView titleTextView = findViewById(R.id.role);
        titleTextView.setText(cartNameStringId);
    }

    public void setPlayerNum(Integer playerNum) {
        TextView playerNumber = findViewById(R.id.player_number);
        playerNumber.setText(playerNum.toString());
    }

    public CardSide getState() {
        return mState;
    }

    public enum CardSide {
        BACKSIDE, FRONT;
    }
}

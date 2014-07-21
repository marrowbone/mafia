package com.morrowbone.mafiacards.app.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.SectionsPagerAdapter;
import com.morrowbone.mafiacards.app.database.DatabaseHelper;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.utils.Constants;
import com.morrowbone.mafiacards.app.views.NonSwipeableViewPager;

import java.lang.reflect.Field;


public class ShowUserCartActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NonSwipeableViewPager mViewPager;
    private FixedSpeedScroller scroller;
    private static Deck mDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_cart);

        try {
            DatabaseHelper.Initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getIntent().hasExtra(Constants.EXTRA_CART_COUNT)) {
            Integer cardCount = getIntent().getIntExtra(Constants.EXTRA_CART_COUNT, 0);
            if (mDeck == null) {
                mDeck = getDeck(cardCount);
            }else if(cardCount != mDeck.size()){
                mDeck = getDeck(cardCount);
            }
            if (!mDeck.isShuffled()) {
                mDeck.shuffle();
            }
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, mDeck);

            // Set up the ViewPager with the sections adapter.
            mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            try {
                Field mScroller;
                mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                Interpolator sInterpolator = new DecelerateInterpolator();
                scroller = new FixedSpeedScroller(mViewPager.getContext(), sInterpolator);
                mScroller.set(mViewPager, scroller);
            } catch (NoSuchFieldException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }
        }
    }

    private Deck getDeck(int cardCount){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Deck deck = databaseHelper.getDeck(cardCount);
        return deck;
    }

    public void showNextPage() {
        int childCount = mSectionsPagerAdapter.getCount();
        int currItem = mViewPager.getCurrentItem();
        if (currItem == childCount - 1) {
            showLastCardDialog();
        } else {
            scroller.setFixedDuration(1000);
            mViewPager.setCurrentItem(currItem + 1, true);
            scroller.setFixedDuration(null);
        }

    }

    private void showLastCardDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle(R.string.final_dialog);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });

        builder.show();
    }

    public class FixedSpeedScroller extends Scroller {

        private Integer mDuration = null;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            if (mDuration == null) {
                super.startScroll(startX, startY, dx, dy, duration);
            } else {
                super.startScroll(startX, startY, dx, dy, mDuration);
            }
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            if (mDuration == null) {
                super.startScroll(startX, startY, dx, dy);
            } else {
                super.startScroll(startX, startY, dx, dy, mDuration);
            }
        }

        public void setFixedDuration(Integer duration) {
            mDuration = duration;
        }
    }

}

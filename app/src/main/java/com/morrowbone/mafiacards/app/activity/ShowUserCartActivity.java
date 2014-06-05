package com.morrowbone.mafiacards.app.activity;

import android.annotation.TargetApi;
import android.content.Context;
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
    private ViewPager mViewPager;
    private FixedSpeedScroller scroller;

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
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            Integer cardCount = getIntent().getIntExtra(Constants.EXTRA_CART_COUNT, 6);
            Deck deck = databaseHelper.getDeck(cardCount);
            deck.shuffle();
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, deck);

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
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

    public void showNextPage() {
        int childCount = mSectionsPagerAdapter.getCount();
        int currItem = mViewPager.getCurrentItem();
        if (currItem == childCount) {
//            TODO: show massage
        } else {
            scroller.setFixedDuration(1000);
            mViewPager.setCurrentItem(currItem + 1, true);
            scroller.setFixedDuration(null);
        }

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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.show_user_cart, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

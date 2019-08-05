package com.morrowbone.mafiacards.app.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.google.android.gms.ads.AdRequest;

import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.morrowbone.mafiacards.app.Fragments.AdsFragment;
import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.SectionsPagerAdapter;
import com.morrowbone.mafiacards.app.constants.StatisticConstants;
import com.morrowbone.mafiacards.app.database.SystemDatabaseHelper;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.utils.Constants;
import com.morrowbone.mafiacards.app.utils.StatisticUtils;
import com.morrowbone.mafiacards.app.utils.Utils;
import com.morrowbone.mafiacards.app.views.NonSwipeableViewPager;

import java.lang.reflect.Field;


public class ShowUserCartActivity extends FragmentActivity implements StatisticConstants {

    private static Deck mDeck;
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NonSwipeableViewPager mViewPager;
    private FixedSpeedScroller scroller;
    private TextView mCardCountTextView;
    private Typeface mTypeFace;
    private InterstitialAd interstitial;
    private View mAdsView;

    public static Intent getIntent(Context context, Deck deck) {
        mDeck = deck;
        mDeck.shuffle();
        Intent intent = new Intent(context, ShowUserCartActivity.class);
        return intent;
    }

    private boolean checkInternetConection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_cart);


        if (getIntent().hasExtra(Constants.EXTRA_CART_COUNT)) {
            try {
                SystemDatabaseHelper.Initialize(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Integer cardCount = getIntent().getIntExtra(Constants.EXTRA_CART_COUNT, 0);
            mDeck = getDeck(cardCount);
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

        mTypeFace = Typeface.createFromAsset(getAssets(), Constants.getTypeFacePath());
        mCardCountTextView = (TextView) findViewById(R.id.card_count_textview);
        mCardCountTextView.setTypeface(mTypeFace);
        mCardCountTextView.setText(String.valueOf(mDeck.size()));


        TextView view = (TextView) findViewById(R.id.text_above_card_count);
        view.setTypeface(mTypeFace);
        view = (TextView) findViewById(R.id.text_below_card_count);
        view.setTypeface(mTypeFace);

        mAdsView = findViewById(R.id.layout_for_fragment);

        AdsFragment adsFragment = AdsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_for_fragment, adsFragment).commit();


        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-7668409826365482/6093438917");

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        if (isEnableShowingAds()) {
            interstitial.loadAd(adRequest);
        }

    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Begin loading your interstitial.


        if (isEnableShowGooglePlayReviewIs() && checkInternetConection()) {
            showGooglePlayReviewDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Deck getDeck(int cardCount) {
        SystemDatabaseHelper databaseHelper = new SystemDatabaseHelper(this);
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

    private Boolean isEnableShowGooglePlayReviewIs() {
        Integer gamesFinished = Utils.getPlayedGameCount(this);
        Integer period = Utils.getRateAfterCount(this);
        if (gamesFinished % period == 0 && Utils.isEnableRateApp(this) && gamesFinished > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isEnableShowingAds() {
        return true;
    }

    private void showGooglePlayReviewDialog() {
        mAdsView.setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.are_you_like_app);
        builder.setMessage(R.string.review_app);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.review_now, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                Utils.setIsEnableRateAppToFalse(ShowUserCartActivity.this);
            }
        });
        builder.setNeutralButton(R.string.later_review, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.incrementRateAfter(ShowUserCartActivity.this);
            }
        });

        builder.setNegativeButton(R.string.never_review, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.setIsEnableRateAppToFalse(ShowUserCartActivity.this);
                mAdsView.setVisibility(View.VISIBLE);
            }
        });

        builder.show();
    }

    private void showLastCardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.final_dialog_title);
        builder.setMessage(R.string.final_dialog_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.final_dialog_go_to_main_menu, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                StatisticUtils.sendActionInfo(BUTTON_CATEGORY, "Game over", mDeck.size());
                if (isEnableShowingAds()) {
                    displayInterstitial();
                }
                Utils.incrementPlayedGameCount(ShowUserCartActivity.this);
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

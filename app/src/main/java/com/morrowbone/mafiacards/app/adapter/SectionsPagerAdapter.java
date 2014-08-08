package com.morrowbone.mafiacards.app.adapter;

/**
 * Created by morrow on 03.06.2014.
 */

import com.nineoldandroids.animation.*;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.activity.ShowUserCartActivity;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.utils.Constants;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    protected static Typeface mTypeFace;
    private static Deck mDeck;
    private Context mContext;

    public SectionsPagerAdapter(FragmentManager fm, Context context, Deck deck) {
        super(fm);
        mContext = context;
        mDeck = deck;
        mTypeFace = Typeface.createFromAsset(mContext.getAssets(), Constants.frontPathInAssets);
    }


    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mDeck.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int cartNameStringId = mDeck.getCard(position).getRoleNameStringId();
        String title = mContext.getResources().getString(cartNameStringId);

        return title;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        CardSide mState;
        private TextView mHelpText;
        private View mCartFrontView;
        private View mCartBackSideView;
        private Interpolator accelerator = new AccelerateInterpolator();
        private Interpolator decelerator = new DecelerateInterpolator();
        private View mRootView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.fragment_show_user_cart, container, false);
            mCartFrontView = mRootView.findViewById(R.id.card_view_front);
            mCartBackSideView = mRootView.findViewById(R.id.card_view_backside);
            show(CardSide.BACKSIDE);

            Integer sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            Integer playerNum = sectionNum + 1;

            int cartNameStringId = mDeck.getCard(sectionNum).getRoleNameStringId();
            String title = getActivity().getResources().getString(cartNameStringId);

            ImageView frontsideImage = (ImageView) mRootView.findViewById(R.id.card_frontside_image);
            frontsideImage.setImageResource(mDeck.getCard(sectionNum).getCartFrontSideImageId());

            TextView mTitleTextView = (TextView) mRootView.findViewById(R.id.role);
            mTitleTextView.setText(title);
            mTitleTextView.setTypeface(mTypeFace);

            TextView mPlayerNumber = (TextView) mRootView.findViewById(R.id.player_number);
            mPlayerNumber.setText(playerNum.toString());
            mPlayerNumber.setTypeface(mTypeFace);

            mHelpText = (TextView) mRootView.findViewById(R.id.help_text);
            hideHelpField();

            mRootView.setOnClickListener(this);
            mRootView.setOnLongClickListener(this);

            return mRootView;
        }

        private void show(CardSide state) {
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

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.root:
                    if (mState == CardSide.FRONT) {
                        hideHelpField();
                        flipit();
                        showNextPage();
                    } else {
                        showHelpField();
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mState == CardSide.BACKSIDE) {
                hideHelpField();
                flipit();
            } else {
                return false;
            }
            return true;
        }

        private void flipit() {
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
            invisToVis.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mState == CardSide.BACKSIDE) {
                        mHelpText.setText(R.string.message_tap_to_see_card);
                    } else {
                        mHelpText.setText(R.string.message_tap_to_hide_card);
                    }
                }
            });
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

        private void showHelpField() {
            mHelpText.setVisibility(View.VISIBLE);
        }

        private void hideHelpField() {
            mHelpText.setVisibility(View.GONE);
        }

        public void showNextPage() {
            ShowUserCartActivity parent = (ShowUserCartActivity) getActivity();
            parent.showNextPage();
        }

        private enum CardSide {
            BACKSIDE, FRONT;
        }
    }
}

package com.morrowbone.mafiacards.app.adapter;

/**
 * Created by morrow on 03.06.2014.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
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
    private static Deck mDeck;
    private Context mContext;

    public SectionsPagerAdapter(FragmentManager fm, Context context, Deck deck) {
        super(fm);
        mContext = context;
        mDeck = deck;

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
        State mState;
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
            mCartFrontView.setVisibility(View.GONE);
            mCartBackSideView.setVisibility(View.VISIBLE);
            mState = State.BACKSIDE;

            Integer sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            Integer playerNum = sectionNum + 1;

            int cartNameStringId = mDeck.getCard(sectionNum).getRoleNameStringId();
            String title = getActivity().getResources().getString(cartNameStringId);

            ImageView frontsideImage = (ImageView) mRootView.findViewById(R.id.card_frontside_image);
            frontsideImage.setImageResource(mDeck.getCard(sectionNum).getCartFrontSideImageId());

            TextView mTitleTextView = (TextView) mRootView.findViewById(R.id.role);
            mTitleTextView.setText(title);

            TextView mPlayerNumber = (TextView) mRootView.findViewById(R.id.player_number);
            mPlayerNumber.setText(playerNum.toString());

            mHelpText = (TextView) mRootView.findViewById(R.id.help_text);
            showHelpField();

            mRootView.setOnClickListener(this);
            mRootView.setOnLongClickListener(this);

            return mRootView;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.root:
                    if (mState == State.FRONT) {
                        hideHelpField();
                        flipit();
                        showNextPage();
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mState == State.BACKSIDE) {
                hideHelpField();
                flipit();
            } else {
                return false;
            }
            return true;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void flipit() {
            final View visibleView;
            final View invisibleView;
            if (mState == State.BACKSIDE) {
                visibleView = mCartBackSideView;
                invisibleView = mCartFrontView;
                mState = State.FRONT;
            } else {
                invisibleView = mCartBackSideView;
                visibleView = mCartFrontView;
                mState = State.BACKSIDE;
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
                    if (mState == State.BACKSIDE) {
                        mHelpText.setText(R.string.message_tap_to_see_card);
                    } else {
                        mHelpText.setText(R.string.message_tap_to_hide_card);
                    }
                    showHelpField();
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
            if (Constants.SHOW_TIPS) {
                mHelpText.setVisibility(View.VISIBLE);
            } else {
                mHelpText.setVisibility(View.GONE);
            }
        }

        private void hideHelpField() {
            mHelpText.setVisibility(View.GONE);
        }

        public void showNextPage() {
            ShowUserCartActivity parent = (ShowUserCartActivity) getActivity();
            parent.showNextPage();
        }

        private enum State {
            BACKSIDE, FRONT;
        }


    }
}

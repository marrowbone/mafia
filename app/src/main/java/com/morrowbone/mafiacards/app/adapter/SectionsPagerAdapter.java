package com.morrowbone.mafiacards.app.adapter;

/**
 * Created by morrow on 03.06.2014.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.activity.ShowUserCartActivity;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.views.CardView;
import com.morrowbone.mafiacards.app.views.CardView.CardSide;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
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
        private CardView mCardView;
        private TextView mHelpText;

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
            View rootView = inflater.inflate(R.layout.fragment_show_user_cart, container, false);
            mCardView = rootView.findViewById(R.id.card);
            mCardView.show(CardSide.BACKSIDE);

            Integer sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            Integer playerNum = sectionNum + 1;
            int cartNameStringId = mDeck.getCard(sectionNum).getRoleNameStringId();
            int imageId = mDeck.getCard(sectionNum).getCartFrontSideImageId();

            mCardView.setCardImageResource(imageId);
            mCardView.setRoleNameResId(cartNameStringId);
            mCardView.setPlayerNum(playerNum);


            mHelpText = rootView.findViewById(R.id.help_text);
            hideHelpField();

            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.root:
                    if (mCardView.getState() == CardSide.FRONT) {
                        hideHelpField();
                        mCardView.flipit();
                        showNextPage();
                    } else {
                        showHelpField();
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mCardView.getState() == CardSide.BACKSIDE) {
                hideHelpField();
                mCardView.flipit();
            } else {
                return false;
            }
            return true;
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
    }
}

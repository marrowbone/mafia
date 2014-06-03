package com.morrowbone.mafiacards.app.adapter;

/**
 * Created by morrow on 03.06.2014.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.model.Deck;

import java.util.Locale;

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
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private TextView mTitleTextView;

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_show_user_cart, container, false);

            Integer sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            mTitleTextView = (TextView) rootView.findViewById(R.id.section_label);
            int cartNameStringId = mDeck.getCard(sectionNum).getRoleNameStringId();
            String title = getActivity().getResources().getString(cartNameStringId);
            mTitleTextView.setText(title);
            return rootView;
        }
    }
}

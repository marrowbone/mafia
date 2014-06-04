package com.morrowbone.mafiacards.app.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.SectionsPagerAdapter;
import com.morrowbone.mafiacards.app.database.DatabaseHelper;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.utils.Constants;


public class ShowUserCartActivity extends ActionBarActivity {

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
    ViewPager mViewPager;

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
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, deck);

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_user_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

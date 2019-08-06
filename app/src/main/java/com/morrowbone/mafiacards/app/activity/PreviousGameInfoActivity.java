package com.morrowbone.mafiacards.app.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.model.Deck;

public class PreviousGameInfoActivity extends FragmentActivity {
    static Deck mDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_game_info);
    }
}

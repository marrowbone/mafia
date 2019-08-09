package com.morrowbone.mafiacards.app.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.DeckAdapter;
import com.morrowbone.mafiacards.app.model.Deck;

public class PreviousGameInfoActivity extends FragmentActivity {
    static Deck mDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_game_info);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        DeckAdapter deckAdapter = new DeckAdapter(mDeck);
        recyclerView.setAdapter(deckAdapter);
    }
}

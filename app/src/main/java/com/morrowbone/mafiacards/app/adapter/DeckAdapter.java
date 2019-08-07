package com.morrowbone.mafiacards.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.views.CardView;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {
    private Deck mDeck;

    public DeckAdapter(Deck deck) {
        mDeck = deck;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_deck_card, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardView.setPlayerNum(position + 1);
    }

    @Override
    public int getItemCount() {
        return mDeck.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardView.flipit();
                }
            });
        }
    }
}

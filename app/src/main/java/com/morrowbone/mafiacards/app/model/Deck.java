package com.morrowbone.mafiacards.app.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by morrow on 03.06.2014.
 */
public class Deck {
    private Integer cardCount;
    private ArrayList<Card> cards;
    private Boolean isShuffled;

    public Deck(Integer cartCount){
        this.cardCount = cartCount;
        isShuffled = false;
        createCards();
    }
    public int size(){
        return cards.size();
    }

    public Card getCard(int index){
        return cards.get(index);
    }

    private void createCards() {
        cards = new ArrayList<Card>(cardCount);
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public void shuffle(){
        Collections.shuffle(cards);
        isShuffled = true;
    }

    public Boolean isShuffled() {
        return isShuffled;
    }
}

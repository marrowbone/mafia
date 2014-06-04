package com.morrowbone.mafiacards.app.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by morrow on 03.06.2014.
 */
public class Deck {
    Integer cardCount;
    ArrayList<Card> cards;

    public Deck(Integer cartCount){
        this.cardCount = cartCount;
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
    }
}

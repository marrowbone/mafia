package com.morrowbone.mafiacards.app.model;

import com.morrowbone.mafiacards.app.R;

/**
 * Created by morrow on 03.06.2014.
 */
public abstract class Card {

    private Integer countInDeck = 0;

    protected Card() {
    }

    public Integer getCartFrontSideImageId() {
        return R.drawable.test_sherif;
    }

    public abstract int getRoleNameStringId();

    public abstract int getCardDescriptionStringId();

    public Integer getCountInDeck() {
        return countInDeck;
    }

    public void setCountInDeck(Integer countInDeck) {
        this.countInDeck = countInDeck;
    }
}

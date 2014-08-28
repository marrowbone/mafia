package com.morrowbone.mafiacards.app.model;

import com.morrowbone.mafiacards.app.R;

/**
 * Created by morrow on 03.06.2014.
 */
public abstract class Card {

    private Integer countInDeck = 0;
    private Integer cartBackSideId;

    protected Card() {
        cartBackSideId = R.color.gray_alfa;
    }

    public Integer getCartBackSideId() {
        return cartBackSideId;
    }

    public void setCartBackSideId(Integer cartBackSideId) {
        this.cartBackSideId = cartBackSideId;
    }

    public Integer getCartFrontSideImageId() {
        return R.drawable.test_sherif;
    }

    public abstract int getRoleNameStringId();

    public abstract int getCardDescriptionStringId();

    public Integer getCountInDeck(){
        return countInDeck;
    }

    public void setCountInDeck(Integer countInDeck) {
        this.countInDeck = countInDeck;
    }
}

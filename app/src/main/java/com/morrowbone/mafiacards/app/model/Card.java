package com.morrowbone.mafiacards.app.model;

import com.morrowbone.mafiacards.app.R;

/**
 * Created by morrow on 03.06.2014.
 */
public abstract class Card {
    private Integer cartBackSideId;

    protected Card() {
        cartBackSideId = R.drawable.card_back_side_ver1;
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
}

package com.morrowbone.mafiacards.app.model;

import com.morrowbone.mafiacards.app.R;

/**
 * Created by morrow on 03.06.2014.
 */
public abstract class Card {
    private Integer imageId;
    private Integer cartBackSideId;

    protected Card(){
        cartBackSideId = R.drawable.card_back_side_ver1;

    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getCartBackSideId() {
        return cartBackSideId;
    }

    public void setCartBackSideId(Integer cartBackSideId) {
        this.cartBackSideId = cartBackSideId;
    }

    public abstract int getRoleNameStringId();
}

package com.morrowbone.mafiacards.app.model.roles;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.model.Card;

/**
 * Created by morrow on 03.06.2014.
 */
public class Detective extends Card {
    @Override
    public int getRoleNameStringId() {
        return R.string.role_detective;
    }

    @Override
    public int getCardDescriptionStringId() {
        return R.string.role_detective_info;
    }

    @Override
    public Integer getCartFrontSideImageId() {
        return R.drawable.detective_cr;
    }
}

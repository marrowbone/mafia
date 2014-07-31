package com.morrowbone.mafiacards.app.model.roles;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.model.Card;

/**
 * Created by morrow on 03.06.2014.
 */
public class Doctor extends Card {
    @Override
    public int getRoleNameStringId() {
        return R.string.role_doctor;
    }

    @Override
    public int getCardDescriptionStringId() {
        return R.string.role_doctor_info;
    }

    @Override
    public Integer getCartFrontSideImageId() {
        return R.drawable.doctor_cr;
    }
}

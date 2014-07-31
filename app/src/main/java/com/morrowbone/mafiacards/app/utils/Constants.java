package com.morrowbone.mafiacards.app.utils;

import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.model.roles.Civilian;
import com.morrowbone.mafiacards.app.model.roles.Detective;
import com.morrowbone.mafiacards.app.model.roles.Doctor;
import com.morrowbone.mafiacards.app.model.roles.DonMafia;
import com.morrowbone.mafiacards.app.model.roles.Immortal;
import com.morrowbone.mafiacards.app.model.roles.Mafia;
import com.morrowbone.mafiacards.app.model.roles.Maniac;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String DEBUG_TAG = "mafia_debug";

    public static final String EXTRA_CART_COUNT = "game_id";

    public final static String frontPathInAssets = "fonts/CorleoneDue.ttf";

    private static ArrayList<Card> roles;

    public static List<Card> getRoles() {
        return roles;
    }


    static {
        roles = new ArrayList<Card>();

        Card card;
        card = new Civilian();
        roles.add(card);

        card = new Detective();
        roles.add(card);

        card = new Doctor();
        roles.add(card);

        card = new Immortal();
        roles.add(card);

        card = new Mafia();
        roles.add(card);

        card = new DonMafia();
        roles.add(card);

        card = new Maniac();
        roles.add(card);
    }

}

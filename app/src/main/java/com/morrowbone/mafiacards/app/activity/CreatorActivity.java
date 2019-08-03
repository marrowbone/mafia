package com.morrowbone.mafiacards.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.CreateDeckArrayAdapter;
import com.morrowbone.mafiacards.app.application.MafiaApp;
import com.morrowbone.mafiacards.app.constants.StatisticConstants;
import com.morrowbone.mafiacards.app.database.DatabaseHelper;
import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.model.roles.Civilian;
import com.morrowbone.mafiacards.app.model.roles.Detective;
import com.morrowbone.mafiacards.app.model.roles.Doctor;
import com.morrowbone.mafiacards.app.model.roles.DonMafia;
import com.morrowbone.mafiacards.app.model.roles.Immortal;
import com.morrowbone.mafiacards.app.model.roles.Mafia;
import com.morrowbone.mafiacards.app.model.roles.Maniac;
import com.morrowbone.mafiacards.app.utils.Constants;
import com.morrowbone.mafiacards.app.utils.StatisticUtils;

import java.util.List;

public class CreatorActivity extends Activity implements View.OnClickListener, StatisticConstants {
    private static int mCardCount = 0;
    private static List<Card> mRolesList;
    private Button mSaveButton;
    private TextView mCardCountTextView;
    private ListView mListView;
    private ArrayAdapter mArrayAdapter;
    private Typeface mTypeFace;

    private static Deck convertToDeck(List<Card> cards) {
        Deck deck = new Deck();
        for (Card card : cards) {
            Integer cardCount;
            if (card.getClass() == Civilian.class) {
                cardCount = card.getCountInDeck();
                for (int i = 0; i < cardCount; i++) {
                    card = new Civilian();
                    deck.addCard(card);
                }
            } else if (card.getClass() == Mafia.class) {
                cardCount = card.getCountInDeck();
                for (int i = 0; i < cardCount; i++) {
                    card = new Mafia();
                    deck.addCard(card);
                }
            } else if (card.getClass() == Detective.class) {
                cardCount = card.getCountInDeck();
                for (int i = 0; i < cardCount; i++) {
                    card = new Detective();
                    deck.addCard(card);
                }
            } else if (card.getClass() == Doctor.class) {
                cardCount = card.getCountInDeck();
                for (int i = 0; i < cardCount; i++) {
                    card = new Doctor();
                    deck.addCard(card);
                }
            } else if (card.getClass() == DonMafia.class) {
                cardCount = card.getCountInDeck();
                for (int i = 0; i < cardCount; i++) {
                    card = new DonMafia();
                    deck.addCard(card);
                }
            } else if (card.getClass() == Immortal.class) {
                cardCount = card.getCountInDeck();
                for (int i = 0; i < cardCount; i++) {
                    card = new Immortal();
                    deck.addCard(card);
                }
            } else if (card.getClass() == Maniac.class) {
                cardCount = card.getCountInDeck();
                for (int i = 0; i < cardCount; i++) {
                    card = new Maniac();
                    deck.addCard(card);
                }
            }
        }
        return deck;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        mTypeFace = Typeface.createFromAsset(getAssets(), Constants.getTypeFacePath());

        mRolesList = Constants.getRoles();
        mArrayAdapter = new CreateDeckArrayAdapter(this, mRolesList);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mArrayAdapter);

        mCardCountTextView = (TextView) findViewById(R.id.card_count_textview);
        mCardCountTextView.setTypeface(mTypeFace);
        mCardCountTextView.setText(String.valueOf(mCardCount));


        TextView view = (TextView) findViewById(R.id.text_above_card_count);
        view.setTypeface(mTypeFace);
        view = (TextView) findViewById(R.id.text_below_card_count);
        view.setTypeface(mTypeFace);

        mSaveButton = (Button) findViewById(R.id.save_btn);
        mSaveButton.setTypeface(mTypeFace);
        mSaveButton.setOnClickListener(this);
    }

    public void setCardCount(int cardCount) {
        mCardCount = cardCount;
        mCardCountTextView.setText(String.valueOf(cardCount));
    }

    @Override
    public void onClick(View v) {
        if (mCardCount > 0) {
            Deck deck = convertToDeck(mRolesList);
            StatisticUtils.sendActionInfo(BUTTON_CATEGORY, "Play in creator");
            Intent intent = ShowUserCartActivity.getIntent(this, deck);
            startActivity(intent);
        } else {
            showErrorDialog(R.string.error_add_some_cards);
        }
    }

    private void showErrorDialog(int title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.show();
    }
}

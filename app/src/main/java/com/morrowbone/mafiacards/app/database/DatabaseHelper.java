package com.morrowbone.mafiacards.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.model.roles.Civilian;
import com.morrowbone.mafiacards.app.model.roles.Detective;
import com.morrowbone.mafiacards.app.model.roles.Doctor;
import com.morrowbone.mafiacards.app.model.roles.DonMafia;
import com.morrowbone.mafiacards.app.model.roles.Immortal;
import com.morrowbone.mafiacards.app.model.roles.Mafia;
import com.morrowbone.mafiacards.app.model.roles.Maniac;
import com.morrowbone.mafiacards.app.model.roles.Wervoolf;
import com.morrowbone.mafiacards.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by morrow on 28.08.2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static public final String DB_NAME = "userDB";
    static final int DB_VERSION = 1;

    private final static String TABLE_ROLES_COMBINATION = "Combinations";
    private final static String TABLE_ROLES_COMBINATION_ID = "_id";
    private final static String TABLE_ROLES_COMBINATION_NAME = "name";
    private final static String TABLE_ROLES_COMBINATION_PLAYER_COUNT = "playerCount";
    private final static String TABLE_ROLES_COMBINATION_CIVILIAN_COUNT = "civilianCount";
    private final static String TABLE_ROLES_COMBINATION_MAFIA_COUNT = "mafiaCount";
    private final static String TABLE_ROLES_COMBINATION_DETECTIVE = "detective";
    private final static String TABLE_ROLES_COMBINATION_DOCTOR = "doctor";
    private final static String TABLE_ROLES_COMBINATION_MANIAC = "maniac";
    private final static String TABLE_ROLES_COMBINATION_IMMORTAL = "immortal";
    private final static String TABLE_ROLES_COMBINATION_DON = "don";
    private static final String DB_CREATE_TABLE_COMBINATION = "CREATE TABLE " + TABLE_ROLES_COMBINATION +
            " (" + TABLE_ROLES_COMBINATION_ID +
            " integer PRIMARY KEY  NOT NULL, " + TABLE_ROLES_COMBINATION_PLAYER_COUNT +
            " integer, " + TABLE_ROLES_COMBINATION_CIVILIAN_COUNT +
            " integer, " + TABLE_ROLES_COMBINATION_MAFIA_COUNT +
            " integer, " + TABLE_ROLES_COMBINATION_DETECTIVE +
            " integer, " + TABLE_ROLES_COMBINATION_IMMORTAL +
            " integer, " + TABLE_ROLES_COMBINATION_DOCTOR +
            " integer, " + TABLE_ROLES_COMBINATION_MANIAC +
            " integer, " + TABLE_ROLES_COMBINATION_DON +
            " integer, " + TABLE_ROLES_COMBINATION_NAME +
            " text NOT NULL);";

    private final static String TABLE_ROLES_COMBINATION_WERVOOLF = "wervoolf";
    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DB_CREATE_TABLE_COMBINATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLES_COMBINATION);

        onCreate(db);
    }

    public void saveDeck(List<Card> deck, String name) {
        // Rate

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            Integer cardCount = 0;
            for (Card card : deck) {
                if (card.getClass() == Civilian.class) {
                    cv.put(TABLE_ROLES_COMBINATION_CIVILIAN_COUNT, card.getCountInDeck());
                    cardCount += card.getCountInDeck();
                }

                if (card.getClass() == Mafia.class) {
                    cv.put(TABLE_ROLES_COMBINATION_MAFIA_COUNT, card.getCountInDeck());
                    cardCount += card.getCountInDeck();
                }

                if (card.getClass() == Detective.class) {
                    cv.put(TABLE_ROLES_COMBINATION_DETECTIVE, card.getCountInDeck());
                    cardCount += card.getCountInDeck();
                }

                if (card.getClass() == Doctor.class) {
                    cv.put(TABLE_ROLES_COMBINATION_DOCTOR, card.getCountInDeck());
                    cardCount += card.getCountInDeck();
                }

                if (card.getClass() == DonMafia.class) {
                    cv.put(TABLE_ROLES_COMBINATION_DON, card.getCountInDeck());
                    cardCount += card.getCountInDeck();
                }

                if (card.getClass() == Immortal.class) {
                    cv.put(TABLE_ROLES_COMBINATION_IMMORTAL, card.getCountInDeck());
                    cardCount += card.getCountInDeck();
                }

                if (card.getClass() == Maniac.class) {
                    cv.put(TABLE_ROLES_COMBINATION_MANIAC, card.getCountInDeck());
                    cardCount += card.getCountInDeck();
                }
            }
            cv.put(TABLE_ROLES_COMBINATION_PLAYER_COUNT, cardCount);
            cv.put(TABLE_ROLES_COMBINATION_NAME, name);
            db.insert(TABLE_ROLES_COMBINATION, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Deck> getDeckList() {
        SQLiteDatabase db = this.getWritableDatabase();

//        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ROLES_COMBINATION + " WHERE "
//                + TABLE_ROLES_COMBINATION_NAME + " = " + deckName, null);

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ROLES_COMBINATION, null);

        List<Deck> deckList = new ArrayList<Deck>();
        try {
            int count = c.getCount();
            c.moveToFirst();
            for (int j = 0; j < count; j++) {
                Deck deck = new Deck();
                Card card;
                // Civilian
                Integer civilianCount = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_CIVILIAN_COUNT));
                for (int i = 0; i < civilianCount; i++) {
                    card = new Civilian();
                    deck.addCard(card);
                }
                // Mafia
                Integer mafiaCount = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_MAFIA_COUNT));
                for (int i = 0; i < mafiaCount; i++) {
                    card = new Mafia();
                    deck.addCard(card);
                }
                // Detective
                int detective = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_DETECTIVE));
                if (detective > 0) {
                    card = new Detective();
                    deck.addCard(card);
                }
                // Don
                int don = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_DON));
                if (don > 0) {
                    card = new DonMafia();
                    deck.addCard(card);
                }
                // Doctor
                int doctor = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_DOCTOR));
                if (doctor > 0) {
                    card = new Doctor();
                    deck.addCard(card);
                }
                // Immortal
                int immortal = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_IMMORTAL));
                if (immortal > 0) {
                    card = new Immortal();
                    deck.addCard(card);
                }
                // Maniac
                int maniac = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_MANIAC));
                if (maniac > 0) {
                    card = new Maniac();
                    deck.addCard(card);
                }

                String name = c.getString(c.getColumnIndex(TABLE_ROLES_COMBINATION_NAME));
                deck.setName(name);

                deckList.add(deck);

                c.moveToNext();
            }


        } catch (Exception e) {
            Log.e(this.toString(), e.getMessage());
        } finally {
            c.close();
            db.close();
        }

        return deckList;
    }
}

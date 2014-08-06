package com.morrowbone.mafiacards.app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.morrowbone.mafiacards.app.application.MafiaApp;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mydatabase.sqlite";
    private static final String DB_ASSETS_PATH = "db/" + DB_NAME;
    private static final String DB_FOLDER = "/data/data/"
            + MafiaApp.getInstance().getPackageName() + "/databases/";
    private static final String DB_PATH = DB_FOLDER + DB_NAME;
    private static final int DB_VERSION = 4;
    private static final int DB_FILES_COPY_BUFFER_SIZE = 8192;

    private final static String TABLE_ROLES_COMBINATION = "Combinations";
    private final static String TABLE_ROLES_COMBINATION_ID = "_id";
    private final static String TABLE_ROLES_COMBINATION_PLAYER_COUNT = "playerCount";
    private final static String TABLE_ROLES_COMBINATION_CIVILIAN_COUNT = "civilianCount";
    private final static String TABLE_ROLES_COMBINATION_MAFIA_COUNT = "mafiaCount";
    private final static String TABLE_ROLES_COMBINATION_DETECTIVE = "detective";
    private final static String TABLE_ROLES_COMBINATION_DOCTOR = "doctor";
    private final static String TABLE_ROLES_COMBINATION_MANIAC = "maniac";
    private final static String TABLE_ROLES_COMBINATION_IMMORTAL = "immortal";
    private final static String TABLE_ROLES_COMBINATION_DON = "don";
    private final static String TABLE_ROLES_COMBINATION_WERVOOLF = "wervoolf";

    private static Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    /**
     * Инициализирует базу данных. Копирует базу из ресурсов приложения, если на не
     * существует или ее версия устарела. Вызвать перед тем как использовать базу.
     *
     * @throws IOException если инициализацию не удалось выполнить
     */
    public static void Initialize(Context context) throws Exception {
        mContext = context;
        if (!isInitialized()) {
            copyInialDBfromAssets();
        }
    }

    private static boolean isInitialized() {

        SQLiteDatabase checkDB = null;
        Boolean correctVersion = false;

        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            correctVersion = checkDB.getVersion() == DB_VERSION;
        } catch (SQLiteException e) {
            Log.w(Constants.DEBUG_TAG, e.getMessage());
        } finally {
            if (checkDB != null)
                checkDB.close();
        }

        return checkDB != null && correctVersion;
    }

    /**
     * Копирует файл базы данных из Assets в директорию для баз данных этого
     * приложения
     *
     * @throws SQLiteException если что-то пошло не так при компировании
     */
    private static void copyInialDBfromAssets() throws Exception {

        Context appContext = MafiaApp.getInstance().getApplicationContext();
        appContext.getPackageName();
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            inStream = new BufferedInputStream(appContext.getAssets().open(
                    DB_ASSETS_PATH), DB_FILES_COPY_BUFFER_SIZE);
            File dbDir = new File(DB_FOLDER);
            if (dbDir.exists() == false)
                dbDir.mkdir();
            outStream = new BufferedOutputStream(new FileOutputStream(DB_PATH),
                    DB_FILES_COPY_BUFFER_SIZE);

            byte[] buffer = new byte[DB_FILES_COPY_BUFFER_SIZE];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        } catch (IOException ex) {
            // Что-то пошло не так
            Log.e(Constants.DEBUG_TAG, ex.getMessage());
            throw new Exception(
                    "Fail to copy initial db from assets", ex);
        } finally {
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        throw new SQLiteException(
                "Call DatabaseHelper.Initialize first. This method should never be called.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException(
                "Call DatabaseHelper.Initialize first. This method should never be called.");
    }

    public Deck getDeck(Integer cardCount) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ROLES_COMBINATION + " WHERE "
                + TABLE_ROLES_COMBINATION_PLAYER_COUNT + " = " + cardCount + ";", null);
        Deck deck = new Deck(cardCount);
        try {

            int count = c.getCount();
            c.moveToFirst();
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
            // Wervoolf
            int wervoolf = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_WERVOOLF));
            if (wervoolf > 0) {
                card = new Wervoolf();
                deck.addCard(card);
            }
            // Maniac
            int maniac = c.getInt(c.getColumnIndex(TABLE_ROLES_COMBINATION_MANIAC));
            if (maniac > 0) {
                card = new Maniac();
                deck.addCard(card);
            }


        } catch (Exception e) {
            Log.e(this.toString(), e.getMessage());
        } finally {
            c.close();
            db.close();
        }

        return deck;

    }

    public Integer getMaxNumberOfPlayer(){
//        SELECT MAX(playerCount) FROM Combinations
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT MAX( " + TABLE_ROLES_COMBINATION_PLAYER_COUNT + " ) FROM "
                + TABLE_ROLES_COMBINATION + ";", null);
        try {
            c.moveToFirst();
            int maxPlayerCount = c.getInt(0);
            return maxPlayerCount;
        } catch (Exception e) {
            Log.e(this.toString(), e.getMessage());
        } finally {
            c.close();
            db.close();
        }
        return 0;
    }

    public Integer getMinNumberOfPlayer(){
        return 3;
    }
}

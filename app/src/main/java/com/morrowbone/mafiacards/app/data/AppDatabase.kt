package com.morrowbone.mafiacards.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.morrowbone.mafiacards.app.application.MafiaApp
import com.morrowbone.mafiacards.app.utils.DATABASE_NAME
import com.morrowbone.mafiacards.app.workers.SeedDatabaseWorker
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Card::class, DefaultCard::class, Deck::class, DefaultDeck::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun defaultCardDao(): DefaultCardDao
    abstract fun deckDao(): DeckDao
    abstract fun defaultDeckDao(): DefaultDeckDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Do nothing
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Do nothing
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                updateDefaultData(database)
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            fillDataBase()
                        }
                    })
                    .build()
        }

        private fun fillDataBase(){
            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
            WorkManager.getInstance(MafiaApp.instance!!.applicationContext).enqueue(request)
        }

        private fun updateDefaultData(database: SupportSQLiteDatabase) {
            database.execSQL("DELETE FROM `default_decks`")
            database.execSQL("DELETE FROM `default_cards`")
            database.execSQL("ALTER TABLE cards ADD COLUMN roleType INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE default_cards ADD COLUMN roleType INTEGER NOT NULL DEFAULT 0")
            fillDataBase()
        }
    }
}
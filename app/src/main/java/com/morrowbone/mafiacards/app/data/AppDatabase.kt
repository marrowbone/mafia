package com.morrowbone.mafiacards.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.morrowbone.mafiacards.app.utils.DATABASE_NAME
import com.morrowbone.mafiacards.app.utils.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Card::class, Deck::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun deckDao(): DeckDao

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
                GlobalScope.launch(IO) {
                    val userDeckTable = "decks"
                    database.execSQL("DROP TABLE 'default_decks'")
                    database.execSQL("DROP TABLE 'default_cards'")
                    database.execSQL("DROP TABLE $userDeckTable")
                    database.execSQL("CREATE TABLE $userDeckTable (`id` INTEGER, `cardIds` TEXT NOT NULL DEFAULT ``, " +
                            "PRIMARY KEY(`id`))")
                    Utils.setLastUsedDeckId(-1)
                }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
        }
    }
}
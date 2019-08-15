package com.morrowbone.mafiacards.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.AppDatabase
import com.morrowbone.mafiacards.app.data.Deck
import com.morrowbone.mafiacards.app.data.DefaultCard
import com.morrowbone.mafiacards.app.data.DefaultDeck
import kotlinx.coroutines.coroutineScope

class SeedDatabaseWorker(
        context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG by lazy { SeedDatabaseWorker::class.java.simpleName }

    override suspend fun doWork(): Result = coroutineScope {
        val defaultCards = getDefaultCards()
        val defaultDecks = getDefaultDecks(defaultCards)
        val database = AppDatabase.getInstance(applicationContext)
        database.defaultCardDao().insertAll(defaultCards)
        val deckDao = database.deckDao()
        for (defaultDeck in defaultDecks) {
            deckDao.insert(defaultDeck.deck)
        }
        database.defaultDeckDao().insertAll(defaultDecks)
        Result.success()
    }

    private fun getDefaultDecks(defaultCards: List<DefaultCard>): List<DefaultDeck> = mutableListOf<DefaultDeck>().apply {
        fun List<DefaultCard>.findById(id: String) = findLast { it.cardId == id }
        val civilian = defaultCards.findById(DefaultCard.CIVILIAN)!!
        val mafia = defaultCards.findById(DefaultCard.MAFIA)!!
        val detective = defaultCards.findById(DefaultCard.DETECTIVE)!!
        val don = defaultCards.findById(DefaultCard.DON_MAFIA)!!
        val doctor = defaultCards.findById(DefaultCard.DOCTOR)!!
        val immortal = defaultCards.findById(DefaultCard.IMMORTAL)!!
        val maniac = defaultCards.findById(DefaultCard.MANIAC)!!

        val defaultDecks = mutableListOf(
                DefaultDeck(Deck(mutableListOf(civilian, civilian, mafia))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, mafia))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, mafia))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, detective, don))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, mafia, detective, don))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, detective, don))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, detective, mafia, don))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, don))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, doctor, maniac, don))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, immortal, doctor, maniac, don))),
                DefaultDeck(Deck(mutableListOf(civilian, civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, immortal, doctor, maniac, don))))
        addAll(defaultDecks)
    }

    private fun getDefaultCards(): List<DefaultCard> = mutableListOf<DefaultCard>().apply {
        add(DefaultCard(DefaultCard.CIVILIAN))
        add(DefaultCard(DefaultCard.MAFIA))
        add(DefaultCard(DefaultCard.DETECTIVE))
        add(DefaultCard(DefaultCard.DON_MAFIA))
        add(DefaultCard(DefaultCard.DOCTOR))
        add(DefaultCard(DefaultCard.IMMORTAL))
        add(DefaultCard(DefaultCard.MANIAC))
    }
}
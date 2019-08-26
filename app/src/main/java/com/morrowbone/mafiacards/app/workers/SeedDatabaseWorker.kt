package com.morrowbone.mafiacards.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.data.*
import com.morrowbone.mafiacards.app.data.DeckRepository.Companion.DEFAULT_DECKS_COUNT
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

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
        database.defaultDeckDao().insertAll(defaultDecks)
        val emptyUserDeckDraft = Deck(DeckRepository.USER_DECK_DRAFT, CardsSet(mutableListOf()))
        database.deckDao().insert(emptyUserDeckDraft)
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
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, mafia))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, mafia))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, mafia))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, detective, don))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, mafia, detective, don))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, detective, don))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, detective, mafia, don))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, don))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, doctor, maniac, don))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, immortal, doctor, maniac, don))),
                DefaultDeck(CardsSet(mutableListOf(civilian, civilian, civilian, civilian, civilian, civilian, mafia, mafia, detective, immortal, doctor, maniac, don))))
        addAll(defaultDecks)

        val defaultDecksSize = defaultDecks.size
        for (i in defaultDecksSize..DEFAULT_DECKS_COUNT) {
            val prevDeck = get(i - 1)
            val nextDefaultCards = ArrayList(prevDeck.cardsSet.defaultCards).apply {
                if (i % 2 == 0) {
                    add(mafia)
                } else {
                    add(civilian)
                }
            }
            add(DefaultDeck(CardsSet(nextDefaultCards)))
        }
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
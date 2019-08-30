package com.morrowbone.mafiacards.app.data

object DefaultDeckDao {
    private val defaultCards: MutableList<DefaultCard> = mutableListOf()
    private val defaultCombination: MutableList<DefaultDeck> = mutableListOf()

    fun init() {
        defaultCards.addAll(createDefaultCards())
        defaultCombination.addAll(createDefaultCombinations())
    }

    private fun createDefaultCombinations(): List<DefaultDeck> = mutableListOf<DefaultDeck>().apply {
        fun List<DefaultCard>.findById(id: String) = findLast { it.cardId == id }
        val civilian = defaultCards.findById(DefaultCard.CIVILIAN)!!
        val mafia = defaultCards.findById(DefaultCard.MAFIA)!!
        val detective = defaultCards.findById(DefaultCard.DETECTIVE)!!
        val don = defaultCards.findById(DefaultCard.DON_MAFIA)!!
        val doctor = defaultCards.findById(DefaultCard.DOCTOR)!!
        val immortal = defaultCards.findById(DefaultCard.IMMORTAL)!!
        val maniac = defaultCards.findById(DefaultCard.MANIAC)!!
        val prostitute = defaultCards.findById(DefaultCard.PROSTITUTE)!!

        val defaultDecks = mutableListOf(
                DefaultDeck(mutableListOf(civilian, civilian, mafia)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, mafia)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, mafia)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, detective, don)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, detective, don, mafia)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, civilian, detective, don, mafia)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, civilian, detective, don, mafia, mafia)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, civilian, civilian, detective, don, mafia, mafia)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, civilian, detective, don, mafia, mafia, doctor, maniac)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, civilian, detective, don, mafia, mafia, doctor, maniac, immortal)),
                DefaultDeck(mutableListOf(civilian, civilian, civilian, civilian, civilian, detective, don, mafia, mafia, mafia, doctor, maniac, immortal)))
        addAll(defaultDecks)

        val defaultDecksSize = defaultDecks.size
        var containsProstitute = false
        for (i in defaultDecksSize..DeckRepository.DEFAULT_DECKS_COUNT) {
            val prevDeck = get(i - 1)
            val nextDefaultCards = ArrayList(prevDeck.cards).apply {
                if (i % 2 == 0) {
                    add(mafia)
                } else {
                    if (containsProstitute) {
                        add(civilian)
                    } else {
                        add(prostitute)
                        containsProstitute = true
                    }
                }
            }
            add(DefaultDeck(nextDefaultCards))
        }
    }

    private fun createDefaultCards(): List<DefaultCard> = mutableListOf<DefaultCard>().apply {
        add(DefaultCard(DefaultCard.CIVILIAN))
        add(DefaultCard(DefaultCard.DETECTIVE))
        add(DefaultCard(DefaultCard.DOCTOR))
        add(DefaultCard(DefaultCard.IMMORTAL))
        add(DefaultCard(DefaultCard.PROSTITUTE))
        add(DefaultCard(DefaultCard.MAFIA))
        add(DefaultCard(DefaultCard.DON_MAFIA))
        add(DefaultCard(DefaultCard.MANIAC))
    }

    fun getDefaultDeck(id: Int): DefaultDeck? {
        return defaultCombination.find { it.playerCount == id }
    }

    fun findCard(id: String): DefaultCard? {
        return defaultCards.find { it.getId() == id }
    }

    fun getCards() = ArrayList(defaultCards)
}
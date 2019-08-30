package com.morrowbone.mafiacards.app.viewmodels

import android.os.Handler
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.data.CardRepository
import com.morrowbone.mafiacards.app.data.DefaultDeckDao

class CardViewModel internal constructor(cardRepository: CardRepository) : BaseCardViewModel(cardRepository) {
    private val cardIdLiveData: MutableLiveData<String> = MutableLiveData()
    val card: LiveData<AbstractCard> = Transformations.switchMap(cardIdLiveData, Function { cardId ->
        val mediatorLiveData = MediatorLiveData<AbstractCard>()
        val defaultCard = DefaultDeckDao.findCard(cardId)
        if (defaultCard != null) {
            Handler().post {
                mediatorLiveData.value = defaultCard
            }
        } else {
            mediatorLiveData.addSource(cardRepository.getUserCard(cardId)) {
                mediatorLiveData.value = it
            }
        }
        return@Function mediatorLiveData
    })

    fun setCardId(cardId: String) {
        cardIdLiveData.value = cardId
    }
}
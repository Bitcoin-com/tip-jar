package com.example.tipjar.paymentDetails.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.core.extensions.launch
import com.example.tipjar.core.extensions.livedata.SingleLiveEvent
import com.example.tipjar.core.providers.CoroutineContextProvider
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.database.repositories.TipHistoryRepository
import javax.inject.Inject

class PaymentDetailsViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val tipHistoryRepository: TipHistoryRepository
) : ViewModel() {

    val tipHistoryEvent = SingleLiveEvent<TaskStatus<TipHistory>>()

    fun getTipHistoryById(id: Int) {
        viewModelScope.launch(
            coroutineContextProvider = coroutineContextProvider,
            work = {
                tipHistoryRepository.find(id)
            },
            onSuccess = {
                tipHistoryEvent.value = TaskStatus.success(it!!)
            },
            onFailure = {
                tipHistoryEvent.value = TaskStatus.error(it)
            }
        )
    }
}
package com.example.tipjar.paymentsHistory.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.core.extensions.launch
import com.example.tipjar.core.extensions.livedata.SingleLiveEvent
import com.example.tipjar.core.providers.CoroutineContextProvider
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.database.repositories.TipHistoryRepository
import javax.inject.Inject

class PaymentsHistoryViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val tipHistoryRepository: TipHistoryRepository
) : ViewModel() {

    val paymentsHistoryEvent = SingleLiveEvent<TaskStatus<List<TipHistory>>>()

    fun getPaymentsHistory() {
        viewModelScope.launch(
            coroutineContextProvider = coroutineContextProvider,
            work = {
                tipHistoryRepository.findAllSortByDate()
            },
            onSuccess = {
                paymentsHistoryEvent.value = TaskStatus.success(it)
            },
            onFailure = {
                paymentsHistoryEvent.value = TaskStatus.error(it)
            }
        )
    }
}
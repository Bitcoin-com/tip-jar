package com.example.tipjar.paymentsHistory.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipjar.core.extensions.launch
import com.example.tipjar.core.extensions.livedata.SingleLiveEvent
import com.example.tipjar.core.providers.CoroutineContextProvider
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.repositories.TipHistoryRepository
import com.example.tipjar.paymentsHistory.entities.TipHistoryItem
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

class PaymentsHistoryViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val tipHistoryRepository: TipHistoryRepository
) : ViewModel() {

    val getPaymentsHistoryEvent = SingleLiveEvent<TaskStatus<List<TipHistoryItem>>>()
    val deletePaymentEvent = SingleLiveEvent<TaskStatus<Int>>()

    fun getPaymentsHistory() {
        viewModelScope.launch(
            coroutineContextProvider = coroutineContextProvider,
            work = {
                tipHistoryRepository.findAllSortByDate().map {
                    TipHistoryItem(tipHistory = it)
                }
            },
            onSuccess = {
                getPaymentsHistoryEvent.value = TaskStatus.success(it)
            },
            onFailure = {
                getPaymentsHistoryEvent.value = TaskStatus.error(it)
            }
        )
    }

    fun deletePayment(ids: List<Int>) {
        GlobalScope.launch(
            coroutineContextProvider = coroutineContextProvider,
            work = {
                tipHistoryRepository.delete(ids.toList())
            },
            onSuccess = {
                deletePaymentEvent.value = TaskStatus.success(ids.count())
                getPaymentsHistory()
            },
            onFailure = {
                deletePaymentEvent.value = TaskStatus.error(it)
            }
        )
    }
}
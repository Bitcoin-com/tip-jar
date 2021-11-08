package com.example.tipjar.main.viewModels

import androidx.lifecycle.ViewModel
import com.example.tipjar.core.extensions.launch
import com.example.tipjar.core.extensions.livedata.SingleLiveEvent
import com.example.tipjar.core.providers.CoroutineContextProvider
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.database.repositories.TipHistoryRepository
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val tipHistoryRepository: TipHistoryRepository
) : ViewModel() {

    val saveTipEvent = SingleLiveEvent<TaskStatus<Any>>()

    fun saveTip(tip: TipHistory) {
        GlobalScope.launch(
            coroutineContextProvider = coroutineContextProvider,
            work = {
                tipHistoryRepository.insert(tip)
            },
            onSuccess = {
                saveTipEvent.value = TaskStatus.success()
            },
            onFailure = {
                saveTipEvent.value = TaskStatus.error(it)
            }
        )
    }
}
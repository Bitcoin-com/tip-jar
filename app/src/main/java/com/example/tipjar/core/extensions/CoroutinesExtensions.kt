package com.example.tipjar.core.extensions

import com.example.tipjar.core.providers.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun <R> CoroutineScope.launch(
    coroutineContextProvider: CoroutineContextProvider,
    work: suspend CoroutineScope.() -> R,
    onSuccess: (R) -> Unit,
    onFailure: (error: Throwable) -> Unit
) {
    launch(coroutineContextProvider.IO) {
        runCatching {
            work()
        }.also { result ->
            launch(coroutineContextProvider.Main) {
                result.onSuccess {
                    onSuccess(it)
                }.onFailure {
                    onFailure(it)
                }
            }
        }
    }
}

fun <T> CoroutineScope.launch(work: suspend CoroutineScope.() -> T) {
    launch(Dispatchers.IO) {
        work()
    }
}
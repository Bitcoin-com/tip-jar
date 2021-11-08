package com.example.tipjar.core.taskStatus

sealed class TaskStatus<out T> {
    data class Loading<out T>(val isLoading: Boolean = true) : TaskStatus<T>()
    data class Success<out T>(val message: String? = null) : TaskStatus<T>()
    data class SuccessWithResult<out T>(val result: T) : TaskStatus<T>()
    data class Failure<out T>(val error: Throwable) : TaskStatus<T>()

    companion object {
        fun <T> loading(isLoading: Boolean = true) = Loading<T>()
        fun <T> success(message: String? = null) = Success<T>()
        fun <T> success(result: T) = SuccessWithResult(result)
        fun <T> error(throwable: Throwable) = Failure<T>(throwable)
    }
}
package com.example.tipjar.core.extensions

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import timber.log.Timber
import java.util.*

fun Any.logDebug(message: String) {
    Timber.d("${javaClass.simpleName}: $message")
}

fun Any.logInfo(message: String) {
    Timber.i("${javaClass.simpleName}: $message")
}

fun Any.logError(message: String) {
    Timber.e("${javaClass.simpleName}: $message")
}

fun Any.logError(throwable: Throwable) {
    Timber.e("${javaClass.simpleName}: ${throwable.message}")
}

fun getFragmentTag(fragmentClass: Class<out Fragment>) : String {
    return fragmentClass.simpleName.toLowerCase(Locale.US)
}

fun allTrue(vararg booleans: Boolean): Boolean {
    return booleans.all { it }
}

fun runDelayed(delayMillis: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable(action), delayMillis)
}
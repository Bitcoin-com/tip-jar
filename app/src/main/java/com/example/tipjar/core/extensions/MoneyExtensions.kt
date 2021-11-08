package com.example.tipjar.core.extensions

import org.joda.money.Money

fun Money.toStringWithPrefix() : String {
    return currencyUnit.symbol + amount.toString()
}
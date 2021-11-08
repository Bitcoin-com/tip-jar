package com.example.tipjar.core.utils

import com.example.tipjar.core.extensions.majorPart
import org.joda.money.CurrencyUnit
import java.math.BigDecimal

object CurrencyUnitHelper {

    fun getMaxValueOfCurrency(currencyUnit: CurrencyUnit, maxValue: BigDecimal): String {
        val currencyUnitDecimalPLaces = currencyUnit.decimalPlaces
        val maxValue = StringBuilder(maxValue.majorPart().toString())
        if (currencyUnitDecimalPLaces > 0) {
            maxValue.append(".")
            for (i in 0 until currencyUnitDecimalPLaces) {
                maxValue.append("9")
            }
        }
        return maxValue.toString()
    }
}
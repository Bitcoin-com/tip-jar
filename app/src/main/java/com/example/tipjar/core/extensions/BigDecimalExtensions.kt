package com.example.tipjar.core.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.majorPart(): Int {
    return setScale(0, RoundingMode.DOWN).toInt()
}
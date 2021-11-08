package com.example.tipjar.core.extensions

fun String.replaceNonStandardDigits(): String {
    if (isEmpty()) {
        return this
    }
    val builder = StringBuilder()
    for (i in 0 until length) {
        val ch = this[i]
        if (isNonstandardDigit(ch)) {
            val numericValue = Character.getNumericValue(ch)
            if (numericValue >= 0) {
                builder.append(numericValue)
            }
        } else {
            builder.append(ch)
        }
    }
    return builder.toString()
}

fun String.onlyDigits(): String {
    return this.replace(Regex("[^\\d]"), "")
}

private fun isNonstandardDigit(ch: Char): Boolean {
    return Character.isDigit(ch) && !(ch >= '0' && ch <= '9')
}
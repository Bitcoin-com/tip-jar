package com.example.tipjar.core.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.tipjar.core.extensions.onlyDigits
import com.example.tipjar.core.extensions.replaceNonStandardDigits
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import kotlin.math.min

abstract class MoneyTextWatcher(
    private val editText: EditText,
    private val currency: CurrencyUnit,
    private val maxAmount: BigDecimal = BigDecimal(999999999)
) : TextWatcher {

    private val separator = DecimalFormatSymbols().decimalSeparator
    private val numberFormat = NumberFormat.getNumberInstance()

    override fun afterTextChanged(editable: Editable) {
        if (editable.isEmpty()) {
            onAmountChanged(Money.of(currency, BigDecimal(0.0)))
            return
        }
        var text = editable.toString()
        text = text.replace("[.,]".toRegex(), separator.toString())
        if (text.filter { it == separator }.count() > 1) {
            val start = editText.selectionStart
            text = editable.replace(start - 1, start, "").toString()
        }
        editText.removeTextChangedListener(this)

        val selectionStart = editText.selectionStart
        val startLength = editText.text.count()

        configureAmount(text)

        val textLength = editText.text.count()
        val selection = selectionStart + (textLength - startLength)
        if (selection > 0 && selection <= editText.text.length) {
            editText.setSelection(selection)
        } else {
            editText.setSelection(editText.text.length)
        }
        editText.addTextChangedListener(this)
        val amount = BigDecimal(editText.text.toString())
        onAmountChanged(
            Money.of(
                currency,
                if (amount >= maxAmount) {
                    maxAmount
                } else {
                    amount
                }
            )
        )
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    abstract fun onAmountChanged(amount: Money)

    private fun configureAmount(text: String) {
        if (text == "0".plus(separator) || text == separator.toString()) {
            editText.setText("0".plus(separator))
            return
        } else if (text.last() == separator) {
            editText.setText(text)
            return
        }
        val amountComponents = text.split(separator)
        val majorValue = try {
            numberFormat.parse(amountComponents.first())
        } catch (e: Exception) {
            0
        }
        val minorValue = amountComponents.last().onlyDigits()
        if (amountComponents.count() == 2 && minorValue.isNotEmpty()) {
            val endIndex = min(minorValue.count(), currency.decimalPlaces)
            val formatted = majorValue.toString()
                .plus(separator)
                .plus(minorValue)
                .substring(
                    0,
                    majorValue.toString().plus(1).length.plus(endIndex)
                ).replaceNonStandardDigits()
            editText.setText(
                if (BigDecimal(formatted) > maxAmount) {
                    CurrencyUnitHelper.getMaxValueOfCurrency(currency, maxAmount)
                } else {
                    formatted
                }
            )
        } else {
            editText.setText(
                if (BigDecimal(majorValue.toString()) > maxAmount) {
                    maxAmount.toString()
                } else {
                    majorValue.toString()
                }
            )
        }
    }
}
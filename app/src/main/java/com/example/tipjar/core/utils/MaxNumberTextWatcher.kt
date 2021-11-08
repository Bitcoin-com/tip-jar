package com.example.tipjar.core.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat

abstract class MaxNumberTextWatcher(
    private val editText: EditText,
    private val minValue: Int = 0,
    private val maxValue: Int = 100
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (editable?.isEmpty() == true) {
            editText.removeTextChangedListener(this)
            editText.setText(minValue.toString())
            onNumberChanged(minValue)
            editText.setSelection(minValue.toString().length)
            editText.addTextChangedListener(this)
            return
        }

        val selectionStart = editText.selectionStart
        val startLength = editText.text.count()

        editText.removeTextChangedListener(this)

        val value = NumberFormat.getInstance().parse(editable.toString())!!
        if (value.toInt() in minValue..maxValue) {
            editText.setText(value.toString())
        } else {
            if (value.toInt() > maxValue) {
                editText.setText(maxValue.toString())
            } else {
                editText.setText(minValue.toString())

            }
        }
        val textLength = editText.text.count()
        val selection = selectionStart + (textLength - startLength)
        if (selection > 0 && selection <= editText.text.length) {
            editText.setSelection(selection)
        } else {
            editText.setSelection(editText.text.length)
        }

        onNumberChanged(editText.text.toString().toInt())
        editText.addTextChangedListener(this)
    }

    abstract fun onNumberChanged(number: Int)
}
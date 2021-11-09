package com.example.tipjar.main.views

import android.content.Context
import com.example.tipjar.R
import com.example.tipjar.core.base.BaseFragmentView
import com.example.tipjar.core.extensions.setDebounceClickListener
import com.example.tipjar.core.extensions.toStringWithPrefix
import com.example.tipjar.core.utils.MaxNumberTextWatcher
import com.example.tipjar.core.utils.MoneyTextWatcher
import com.example.tipjar.main.configurations.MainConfiguration
import kotlinx.android.synthetic.main.view_main.view.*
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.atomic.AtomicInteger

interface MainViewDelegate {
    fun onPeopleCountChanged(count: Int)
    fun onTakePhotoOfReceipt()
    fun onPaymentChanged(payment: Money)
    fun onSavePayment()
    fun onTipPercentChanged(percent: Int)
    fun onTotalTipAmountChanged(tipAmount: Money)
    fun openPaymentsHistory()
}

class MainView(context: Context) : BaseFragmentView(context) {

    companion object {
        const val MAX_PEOPLE_COUNT = 20
        const val DEFAULT_TIP_PERCENT = 0
    }

    var delegate: MainViewDelegate? = null

    private val atomicInteger = AtomicInteger(0)
    private var amount: Money? = null
    private var tipPercentValue = DEFAULT_TIP_PERCENT

    init {
        inflate(context, R.layout.view_main, this)
        payment.addTextChangedListener(
            object : MoneyTextWatcher(payment, CurrencyUnit.USD) {
                override fun onAmountChanged(amount: Money) {
                    this@MainView.amount = amount
                    computeTotalTip()?.let {
                        computeTipPerPerson(it)
                    }
                    delegate?.onPaymentChanged(amount)
                }
            }
        )

        addPeople.setDebounceClickListener {
            if (atomicInteger.get() == MAX_PEOPLE_COUNT) {
                return@setDebounceClickListener
            }
            peopleCount.text = atomicInteger.incrementAndGet().toString()
            delegate?.onPeopleCountChanged(atomicInteger.get())
            computeTotalTip()?.let {
                computeTipPerPerson(it)
            }
        }
        minusPeople.setDebounceClickListener {
            if (atomicInteger.get() == 0) {
                return@setDebounceClickListener
            }
            peopleCount.text = atomicInteger.decrementAndGet().toString()
            delegate?.onPeopleCountChanged(atomicInteger.get())
            computeTotalTip()?.let {
                computeTipPerPerson(it)
            }
        }

        tipPercent.addTextChangedListener(object : MaxNumberTextWatcher(tipPercent) {
            override fun onNumberChanged(number: Int) {
                tipPercentValue = number
                computeTotalTip()?.let {
                    computeTipPerPerson(it)
                }
                delegate?.onTipPercentChanged(number)
            }
        })

        receiptPhotoCheckbox.setDebounceClickListener {
            delegate?.onTakePhotoOfReceipt()
        }

        savePayment.setDebounceClickListener {
            delegate?.onSavePayment()
        }

        paymentsHistory.setDebounceClickListener {
            delegate?.openPaymentsHistory()
        }
    }

    fun setUpView(configuration: MainConfiguration) {
        peopleCount.text = configuration.tipPeopleCount.toString()
        tipPercent.setText(configuration.tipPercent.toString())
        payment.setText(configuration.payment.toString())
        atomicInteger.set(configuration.tipPeopleCount)
        computeTotalTip()?.let {
            computeTipPerPerson(it)
        }
        receiptPhotoCheckbox.isChecked = configuration.receiptPhotoPath != null
    }

    fun computeTotalTip(): Money? {
        val amount = this.amount ?: return null
        val totalTip = amount.amount.toFloat() / tipPercentValue.toFloat()
        val tipMoney = Money.of(
            CurrencyUnit.USD,
            if (!totalTip.toDouble().isNaN() && !totalTip.toDouble().isInfinite()) {
                totalTip.toBigDecimal().setScale(CurrencyUnit.USD.decimalPlaces, RoundingMode.UP)
            } else {
                BigDecimal.ZERO
            }
        )
        totalTipAmount.text = tipMoney.toStringWithPrefix()

        delegate?.onTotalTipAmountChanged(tipMoney)
        return tipMoney
    }

    fun computeTipPerPerson(totalTipMoney: Money): Money? {
        val totalTip = totalTipMoney.amount.toDouble() / atomicInteger.get()
        val perPersonMoney = Money.of(
            CurrencyUnit.USD,
            if (!totalTip.isNaN() && !totalTip.isInfinite()) {
                totalTip.toBigDecimal().setScale(CurrencyUnit.USD.decimalPlaces, RoundingMode.UP)
            } else {
                BigDecimal.ZERO
            }
        )
        perPersonAmount.text = perPersonMoney.toStringWithPrefix()
        return perPersonMoney
    }

    fun setSavePaymentButtonEnabled(enable: Boolean) {
        savePayment.isEnabled = enable
    }
}
package com.example.tipjar.paymentDetails.views

import android.content.Context
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.tipjar.R
import com.example.tipjar.core.base.BaseFragmentView
import com.example.tipjar.core.extensions.dpToPx
import com.example.tipjar.core.extensions.setDebounceClickListener
import com.example.tipjar.core.extensions.toStringWithPrefix
import com.example.tipjar.database.entities.TipHistory
import kotlinx.android.synthetic.main.view_payment_details.view.*
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.io.File
import java.text.SimpleDateFormat

interface PaymentDetailsViewDelegate {
    fun onDismiss()
}

class PaymentDetailsView(context: Context) : BaseFragmentView(context) {

    var delegate: PaymentDetailsViewDelegate? = null

    init {
        inflate(context, R.layout.view_payment_details, this)

        container.setDebounceClickListener {
            delegate?.onDismiss()
        }
    }

    fun setUpView(tipHistory: TipHistory) {
        paymentAmount.text = Money.of(CurrencyUnit.USD, tipHistory.payment).toStringWithPrefix()
        tipAmount.text = Money.of(CurrencyUnit.USD, tipHistory.tipAmount).toStringWithPrefix()
        paymentDate.text = SimpleDateFormat("yyyy MMMM d").format(tipHistory.paymentDate)

        receiptPhoto.load(File(tipHistory.receiptPhotoPath)) {
            transformations(RoundedCornersTransformation(dpToPx(12f)))
        }
    }
}
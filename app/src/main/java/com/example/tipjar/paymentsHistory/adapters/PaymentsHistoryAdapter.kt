package com.example.tipjar.paymentsHistory.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.tipjar.R
import com.example.tipjar.core.base.BaseRecyclerViewAdapter
import com.example.tipjar.core.extensions.dpToPx
import com.example.tipjar.core.extensions.inflate
import com.example.tipjar.core.extensions.setDebounceClickListener
import com.example.tipjar.core.extensions.toStringWithPrefix
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.paymentsHistory.adapters.callbacks.PaymentsHistoryCallback
import kotlinx.android.synthetic.main.view_payments_history_item.view.*
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.io.File
import java.text.SimpleDateFormat

class PaymentsHistoryAdapter(
    override val items: MutableList<TipHistory> = mutableListOf(),
    private val onItemSelected: ((tipHistory: TipHistory) -> Unit)? = null
) : BaseRecyclerViewAdapter<TipHistory>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.view_payments_history_item))
    }

    override fun onBindViewHolder(view: View, item: TipHistory, position: Int) {
        with(view) {
            paymentAmount.text = Money.of(CurrencyUnit.USD, item.payment).toStringWithPrefix()
            paymentDate.text = SimpleDateFormat("yyyy MMMM d").format(item.paymentDate)
            tipAmount.text = Money.of(CurrencyUnit.USD, item.tipAmount).toStringWithPrefix()

            receiptPhoto.load(File(item.receiptPhotoPath)) {
                transformations(RoundedCornersTransformation(dpToPx(12f)))
            }

            setDebounceClickListener {
                onItemSelected?.invoke(item)
            }
        }
    }

    fun setListItems(items: List<TipHistory>) {
        val callback = DiffUtil.calculateDiff(PaymentsHistoryCallback(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        callback.dispatchUpdatesTo(this)
    }
}
package com.example.tipjar.paymentsHistory.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.tipjar.core.extensions.allTrue
import com.example.tipjar.database.entities.TipHistory

class PaymentsHistoryCallback(
    private val oldItems: List<TipHistory>,
    private val newItems: List<TipHistory>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return allTrue(
            oldItem.payment == newItem.payment,
            oldItem.receiptPhotoPath == newItem.receiptPhotoPath,
            oldItem.paymentDate == newItem.paymentDate,
            oldItem.tipAmount == newItem.tipAmount
        )
    }
}
package com.example.tipjar.paymentsHistory.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.tipjar.core.extensions.allTrue
import com.example.tipjar.paymentsHistory.entities.TipHistoryItem

class PaymentsHistoryCallback(
    private val oldItems: List<TipHistoryItem>,
    private val newItems: List<TipHistoryItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].tipHistory.id == newItems[newItemPosition].tipHistory.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return allTrue(
            oldItem.isOpened == newItem.isOpened,
            oldItem.isSelectable == newItem.isSelectable,
            oldItem.isSelected == newItem.isSelected,
            oldItem.tipHistory.payment == newItem.tipHistory.payment,
            oldItem.tipHistory.receiptPhotoPath == newItem.tipHistory.receiptPhotoPath,
            oldItem.tipHistory.paymentDate == newItem.tipHistory.paymentDate,
            oldItem.tipHistory.tipAmount == newItem.tipHistory.tipAmount
        )
    }
}
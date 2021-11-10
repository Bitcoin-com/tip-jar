package com.example.tipjar.paymentsHistory.adapters

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import coil.load
import coil.transform.RoundedCornersTransformation
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.example.tipjar.R
import com.example.tipjar.core.base.BaseRecyclerViewAdapter
import com.example.tipjar.core.extensions.dpToPx
import com.example.tipjar.core.extensions.inflate
import com.example.tipjar.core.extensions.setDebounceClickListener
import com.example.tipjar.core.extensions.toStringWithPrefix
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.paymentsHistory.adapters.callbacks.PaymentsHistoryCallback
import com.example.tipjar.paymentsHistory.entities.TipHistoryItem
import kotlinx.android.synthetic.main.view_payments_history_item.view.*
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.io.File
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

class PaymentsHistoryAdapter(
    override val items: MutableList<TipHistoryItem> = mutableListOf(),
    private val onItemSelected: ((tipHistory: TipHistory) -> Unit)? = null,
    private val onItemDelete: ((id: Int) -> Unit)? = null,
    private val onShowDeleteMenu: (() -> Unit)? = null
) : BaseRecyclerViewAdapter<TipHistoryItem>(items) {

    private var openedItem: WeakReference<SwipeRevealLayout>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.view_payments_history_item))
    }

    override fun onBindViewHolder(view: View, item: TipHistoryItem, position: Int) {
        with(view) {
            val tipHistory = item.tipHistory
            paymentAmount.text = Money.of(CurrencyUnit.USD, tipHistory.payment).toStringWithPrefix()
            paymentDate.text = SimpleDateFormat("yyyy MMMM d").format(tipHistory.paymentDate)
            tipAmount.text = Money.of(CurrencyUnit.USD, tipHistory.tipAmount).toStringWithPrefix()

            receiptPhoto.load(File(tipHistory.receiptPhotoPath)) {
                transformations(RoundedCornersTransformation(dpToPx(12f)))
            }

            checkbox.apply {
                visibility = if (item.isSelectable) VISIBLE else GONE
                setOnCheckedChangeListener { button, isChecked ->
                    items[position] = item.copy(isSelected = isChecked)
                }
                isChecked = item.isSelected
            }

            paymentContainer.apply {
                setDebounceClickListener {
                    if (item.isSelectable) {
                        checkbox.performClick()
                    } else {
                        when {
                            openedItem?.get() != null -> findOpenedItemsAndClose()
                            swipeContainer.isOpened -> swipeContainer.close(true)
                            else -> onItemSelected?.invoke(tipHistory)
                        }
                    }
                }
                setOnLongClickListener {
                    findOpenedItemsAndClose()
                    setListItems(items.map { it.copy(isSelectable = true) })
                    onShowDeleteMenu?.invoke()
                    return@setOnLongClickListener true
                }
                isLongClickable = !item.isSelectable
            }

            deletePayment.setDebounceClickListener {
                onItemDelete?.invoke(tipHistory.id)
            }

            swipeContainer.apply {
                if (item.isSelectable) {
                    findOpenedItemsAndClose()
                } else {
                    if (item.isOpened) {
                        open(false)
                    } else {
                        close(false)
                    }
                }
                setLockDrag(item.isSelectable)

                setSwipeListener(object : SwipeRevealLayout.SimpleSwipeListener() {
                    override fun onOpened(view: SwipeRevealLayout?) {
                        super.onOpened(view)
                        findOpenedItemsAndClose()
                        items[items.indexOf(item)] = item.copy(isOpened = true)
                        openedItem = WeakReference(view)
                    }
                })
            }
        }
    }

    fun setListItems(items: List<TipHistoryItem>) {
        val callback = DiffUtil.calculateDiff(PaymentsHistoryCallback(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        callback.dispatchUpdatesTo(this)
    }

    private fun findOpenedItemsAndClose() {
        val oldOpenedItem = items.firstOrNull { it.isOpened }
        oldOpenedItem?.let {
            items[items.indexOf(it)] = it.copy(isOpened = false)
        }
        openedItem?.get()?.close(true)
        openedItem = null
    }
}
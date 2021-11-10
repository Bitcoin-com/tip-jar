package com.example.tipjar.paymentsHistory.views

import android.content.Context
import com.example.tipjar.R
import com.example.tipjar.core.base.BaseFragmentView
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.paymentsHistory.adapters.PaymentsHistoryAdapter
import com.example.tipjar.paymentsHistory.entities.TipHistoryItem
import kotlinx.android.synthetic.main.view_app_bar.view.*
import kotlinx.android.synthetic.main.view_payments_history.view.*

interface PaymentsHistoryViewDelegate {
    fun onViewBackPressed()
    fun onItemSelected(tipHistory: TipHistory)
    fun onItemDelete(vararg ids: Int)
}

class PaymentsHistoryView(context: Context) : BaseFragmentView(context) {

    var delegate: PaymentsHistoryViewDelegate? = null

    private var adapter = PaymentsHistoryAdapter(
        onItemSelected = { tipHistory ->
            delegate?.onItemSelected(tipHistory)
        }, onItemDelete = { id ->
            delegate?.onItemDelete(id)
        }, onShowDeleteMenu = {
            toolbar.inflateMenu(R.menu.menu_payment_history)
            toolbar.isTitleCentered = false
        }
    )

    init {
        inflate(context, R.layout.view_payments_history, this)
        paymentsHistoryList.adapter = adapter

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_toolbar_back)
            setNavigationOnClickListener {
                delegate?.onViewBackPressed()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_delete -> {
                        val idsToDelete = adapter.items.filter { it.isSelected }
                            .map { it.tipHistory.id }
                            .toIntArray()
                        delegate?.onItemDelete(*idsToDelete)
                        menu.clear()
                        toolbar.isTitleCentered = true
                    }
                    R.id.menu_cancel -> {
                        setListItems(adapter.items.map { it.copy(isSelectable = false) })
                        menu.clear()
                        toolbar.isTitleCentered = true
                    }
                    R.id.menu_select_all -> {
                        setListItems(adapter.items.map { it.copy(isSelected = true) })
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    fun setListItems(items: List<TipHistoryItem>) {
        adapter.setListItems(items)
        emptyStateText.visibility = if (items.isEmpty()) VISIBLE else GONE
    }
}
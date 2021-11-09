package com.example.tipjar.paymentsHistory.views

import android.content.Context
import com.example.tipjar.R
import com.example.tipjar.core.base.BaseFragmentView
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.paymentsHistory.adapters.PaymentsHistoryAdapter
import kotlinx.android.synthetic.main.view_app_bar.view.*
import kotlinx.android.synthetic.main.view_payments_history.view.*

interface PaymentsHistoryViewDelegate {
    fun onViewBackPressed()
    fun onItemSelected(tipHistory: TipHistory)
}

class PaymentsHistoryView(context: Context) : BaseFragmentView(context) {

    var delegate: PaymentsHistoryViewDelegate? = null

    private var adapter = PaymentsHistoryAdapter { tipHistory ->
        delegate?.onItemSelected(tipHistory)
    }

    init {
        inflate(context, R.layout.view_payments_history, this)
        paymentsHistoryList.adapter = adapter

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener {
            delegate?.onViewBackPressed()
        }
    }

    fun setListItems(items: List<TipHistory>) {
        adapter.setListItems(items)
    }
}
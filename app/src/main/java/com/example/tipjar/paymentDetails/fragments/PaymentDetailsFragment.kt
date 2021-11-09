package com.example.tipjar.paymentDetails.fragments

import android.content.Context
import android.os.Bundle
import com.example.tipjar.R
import com.example.tipjar.TipJarApplication
import com.example.tipjar.core.base.BaseFragmentLifeCycle
import com.example.tipjar.core.extensions.runDelayed
import com.example.tipjar.core.extensions.showErrorSnackbar
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.paymentDetails.viewModels.PaymentDetailsViewModel
import com.example.tipjar.paymentDetails.views.PaymentDetailsView
import com.example.tipjar.paymentDetails.views.PaymentDetailsViewDelegate

class PaymentDetailsFragment :
    BaseFragmentLifeCycle<PaymentDetailsViewModel, PaymentDetailsView>(),
    PaymentDetailsViewDelegate {

    val tipHistoryId: Int
        get() {
            return arguments?.getInt("tip_history_id", -1) ?: -1
        }

    override fun inject() {
        TipJarApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        context: Context,
        savedInstanceState: Bundle?
    ) = PaymentDetailsView(context).also {
        it.delegate = this
    }

    override fun onViewCreated(contentView: PaymentDetailsView, savedInstanceState: Bundle?) {
        viewModel.getTipHistoryById(tipHistoryId)
    }

    override fun observerChanges() {
        viewModel.tipHistoryEvent.observe(this) {
            when (it) {
                is TaskStatus.SuccessWithResult -> contentView.setUpView(it.result)
                is TaskStatus.Failure -> {
                    contentView.showErrorSnackbar(getString(R.string.generic_error))
                    runDelayed(300) {
                        super.onBackPressed()
                    }
                }
            }
        }
    }

    // PaymentDetailsViewDelegate

    override fun onDismiss() {
        super.onBackPressed()
    }
}
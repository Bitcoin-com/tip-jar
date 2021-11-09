package com.example.tipjar.main.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.evernote.android.state.State
import com.example.tipjar.BuildConfig
import com.example.tipjar.R
import com.example.tipjar.TipJarApplication
import com.example.tipjar.core.base.BaseFragmentLifeCycle
import com.example.tipjar.core.bundlers.MainConfigurationBundler
import com.example.tipjar.core.extensions.addFragmentHorizontally
import com.example.tipjar.core.extensions.showErrorSnackbar
import com.example.tipjar.core.helpers.FileHelper
import com.example.tipjar.core.helpers.ToastHelper
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.TipDatabase
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.main.configurations.MainConfiguration
import com.example.tipjar.main.viewModels.MainViewModel
import com.example.tipjar.main.views.MainView
import com.example.tipjar.main.views.MainViewDelegate
import com.example.tipjar.paymentsHistory.fragments.PaymentsHistoryFragment
import kotlinx.android.synthetic.main.view_main.*
import org.joda.money.Money
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

class MainFragment : BaseFragmentLifeCycle<MainViewModel, MainView>(), MainViewDelegate {

    @Inject lateinit var database: TipDatabase

    @State(MainConfigurationBundler::class)
    var configuration: MainConfiguration? = null
        get() {
            return field ?: MainConfiguration()
        }
    @State var receiptPhotoPath: String? = null

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        receiptPhotoCheckbox.isChecked = if (result.resultCode == Activity.RESULT_OK) {
            receiptPhotoPath?.let {
                configuration = configuration?.copy(receiptPhotoPath = receiptPhotoPath)
                configureSaveButton()
                true
            } ?: false
        } else {
            false
        }
    }

    override fun inject() {
        TipJarApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        context: Context,
        savedInstanceState: Bundle?
    ) = MainView(context).also {
        it.delegate = this
    }

    override fun onViewCreated(contentView: MainView, savedInstanceState: Bundle?) {
        configuration?.let {
            contentView.setUpView(it)
        }
    }

    override fun observerChanges() {
        viewModel.saveTipEvent.observe(this) {
            when (it) {
                is TaskStatus.Success -> {
                    ToastHelper.showSuccessToast(
                        contentView.context,
                        R.string.save_payment_success_message
                    )
                    configuration = MainConfiguration()
                    receiptPhotoPath = null
                    contentView.setUpView(configuration!!)
                }
                is TaskStatus.Failure -> {
                    contentView.showErrorSnackbar(getString(R.string.generic_error))
                }
            }
        }
    }

    // MainViewDelegate

    override fun onPeopleCountChanged(count: Int) {
        configuration = configuration?.copy(tipPeopleCount = count)
    }

    override fun onTakePhotoOfReceipt() {
        dispatchTakePictureIntent()
    }

    override fun onPaymentChanged(payment: Money) {
        configuration = configuration?.copy(payment = payment.amount.toDouble())
        configureSaveButton()
    }

    override fun onTotalTipAmountChanged(tipAmount: Money) {
        configuration = configuration?.copy(totalTipAmount = tipAmount.amount.toDouble())
    }

    override fun onSavePayment() {
        viewModel.saveTip(
            TipHistory(
                paymentDate = Date(),
                payment = configuration?.payment!!,
                tipAmount = configuration?.totalTipAmount!!,
                receiptPhotoPath = configuration?.receiptPhotoPath!!
            )
        )
    }

    override fun onTipPercentChanged(percent: Int) {
        configuration = configuration?.copy(tipPercent = percent)
    }

    override fun openPaymentsHistory() {
        childFragmentManager.beginTransaction().addFragmentHorizontally(
            containerId = R.id.mainFragmentContainer,
            fragmentClass = PaymentsHistoryFragment::class.java,
            addToBackStack = true
        ).commit()
    }

    private fun configureSaveButton() {
        val isEnable = configuration?.payment!! > 0.01 && configuration?.receiptPhotoPath != null
        contentView.setSavePaymentButtonEnabled(isEnable)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(contentView.context.packageManager)?.also {
                val photoFile: File? = try {
                    FileHelper.createImageFile(contentView.context).also {
                        receiptPhotoPath = it.absolutePath
                    }
                } catch (e: IOException) {
                    contentView.showErrorSnackbar(e.message ?: getString(R.string.generic_error))
                    null
                }

                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        contentView.context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }
}
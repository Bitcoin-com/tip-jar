package com.example.tipjar.di

import com.example.tipjar.main.activities.MainActivity
import com.example.tipjar.TipJarApplication
import com.example.tipjar.main.fragments.MainFragment
import com.example.tipjar.paymentDetails.fragments.PaymentDetailsFragment
import com.example.tipjar.paymentsHistory.fragments.PaymentsHistoryFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DatabaseModule::class])
@Singleton
interface AppComponent {
    fun inject(tipJarApplication: TipJarApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(paymentsHistoryFragment: PaymentsHistoryFragment)
    fun inject(paymentDetailsFragment: PaymentDetailsFragment)
}
package com.example.tipjar

import android.app.Application
import com.example.tipjar.di.AppComponent
import com.example.tipjar.di.AppModule
import com.example.tipjar.di.DaggerAppComponent
import com.example.tipjar.di.DatabaseModule
import timber.log.Timber

class TipJarApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .databaseModule(DatabaseModule(this))
            .build().also {
                it.inject(this)
            }
    }
}
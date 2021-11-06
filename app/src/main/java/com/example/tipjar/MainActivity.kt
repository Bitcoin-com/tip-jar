package com.example.tipjar

import android.os.Bundle
import com.example.tipjar.core.base.BaseActivity
import com.example.tipjar.database.TipDatabase
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject lateinit var database: TipDatabase

    override fun inject() {
        TipJarApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("")
    }
}
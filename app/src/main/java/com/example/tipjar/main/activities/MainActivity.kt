package com.example.tipjar.main.activities

import android.os.Bundle
import com.example.tipjar.R
import com.example.tipjar.TipJarApplication
import com.example.tipjar.core.base.BaseActivity
import com.example.tipjar.core.extensions.addFragmentHorizontally
import com.example.tipjar.core.extensions.getFragmentTag
import com.example.tipjar.main.fragments.MainFragment

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun inject() {
        TipJarApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.apply {
                beginTransaction()
                    .addFragmentHorizontally(
                        containerId = R.id.mainContainer,
                        fragmentClass = MainFragment::class.java,
                        addToBackStack = false
                    ).commit()
            }
            rootFragmentTag = getFragmentTag(MainFragment::class.java)
        }
    }
}
package com.example.tipjar.core.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.tipjar.core.extensions.getLastFragmentTag
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException
import javax.inject.Inject

abstract class BaseActivity(
    @LayoutRes contentLayoutId: Int
) : AppCompatActivity(contentLayoutId) {

    @Inject lateinit var eventBus: EventBus

    open lateinit var rootFragmentTag: String

    abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("root_fragment_tag", rootFragmentTag)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        rootFragmentTag = savedInstanceState.getString("root_fragment_tag", null)
    }

    override fun onStart() {
        super.onStart()
        try {
            eventBus.register(this)
        } catch (e: EventBusException) {
        }
    }

    override fun onStop() {
        super.onStop()
        eventBus.unregister(this)
    }

    override fun onBackPressed() {
        val rootFragmentManager = supportFragmentManager
            .findFragmentByTag(rootFragmentTag)
            ?.childFragmentManager
            ?: run {
                super.onBackPressed()
                return
            }
        if (rootFragmentManager.backStackEntryCount > 0) {
            var childFragment = rootFragmentManager.findFragmentByTag(
                rootFragmentManager.getLastFragmentTag()
            ) as? BaseFragment<*>
            while (childFragment?.onBackPressed() == false) {
                childFragment = childFragment.childFragmentManager
                    .findFragmentByTag(
                        childFragment.childFragmentManager.getLastFragmentTag()
                    ) as? BaseFragment<*>
            }
        } else {
            super.onBackPressed()
        }
    }
}
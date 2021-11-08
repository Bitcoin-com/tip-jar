package com.example.tipjar.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.evernote.android.state.StateSaver
import com.example.tipjar.core.extensions.getLastFragmentTag
import com.example.tipjar.core.extensions.logDebug
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException
import javax.inject.Inject

abstract class BaseFragment<V : BaseFragmentView> : Fragment() {

    @Inject lateinit var eventBus: EventBus
    lateinit var contentView: V

    abstract fun inject()
    abstract fun onCreateView(context: Context, savedInstanceState: Bundle?): V
    abstract fun onViewCreated(contentView: V, savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        return onCreateView(inflater.context, savedInstanceState).also {
            contentView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        StateSaver.restoreInstanceState(this, savedInstanceState)
        onViewCreated(contentView, savedInstanceState)
    }

    open fun onBackPressed(): Boolean {
        val lastFragment = childFragmentManager.findFragmentByTag(
            childFragmentManager.getLastFragmentTag()
        )
        return when {
            childFragmentManager.backStackEntryCount == 0 -> {
                logDebug("popped")
                parentFragmentManager.popBackStack()
                true
            }
            lastFragment?.childFragmentManager?.backStackEntryCount ?: 0 > 0 -> false
            else -> (lastFragment as? BaseFragment<*>)?.onBackPressed() ?: false
        }
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

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }
}
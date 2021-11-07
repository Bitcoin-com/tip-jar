package com.example.tipjar.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.tipjar.core.utils.ViewModelUtils
import javax.inject.Inject

abstract class BaseFragmentLifeCycle<VM : ViewModel, V : BaseFragmentView> : BaseFragment<V>(),
    LifecycleOwner {

    @Inject lateinit var viewModel: VM

    abstract fun observerChanges()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        viewModel = ViewModelProvider(ViewModelStore(), ViewModelUtils.createFor(viewModel))
            .get(viewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(contentView, savedInstanceState)
        observerChanges()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }
}
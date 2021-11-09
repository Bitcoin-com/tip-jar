package com.example.tipjar.core.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tipjar.R
import java.util.*

fun FragmentTransaction.addFragmentHorizontally(
    @IdRes containerId: Int,
    fragmentClass: Class<out Fragment>,
    bundle: Bundle? = null,
    tag: String? = fragmentClass.simpleName.toLowerCase(Locale.US),
    addToBackStack: Boolean = true
): FragmentTransaction {
    return addFragment(
        this,
        containerId,
        fragmentClass,
        bundle,
        tag,
        addToBackStack
    )
}

fun FragmentTransaction.addFragmentVertically(
    @IdRes containerId: Int,
    fragmentClass: Class<out Fragment>,
    bundle: Bundle? = null,
    tag: String? = fragmentClass.simpleName.toLowerCase(Locale.US),
    addToBackStack: Boolean = true
): FragmentTransaction {
    return addFragment(
        this,
        containerId,
        fragmentClass,
        bundle,
        tag,
        addToBackStack,
        false
    )
}

fun FragmentTransaction.replaceFragment(
    @IdRes containerId: Int,
    fragmentClass: Class<out Fragment>,
    bundle: Bundle? = null,
    tag: String? = fragmentClass.simpleName.toLowerCase(Locale.US),
    addToBackStack: Boolean = true
): FragmentTransaction {
    if (addToBackStack) {
        this.addToBackStack(tag)
    }
    return this.replace(
        containerId,
        fragmentClass,
        bundle,
        tag
    )
}

private fun addFragment(
    fragmentTransaction: FragmentTransaction,
    @IdRes containerId: Int,
    fragmentClass: Class<out Fragment>,
    bundle: Bundle? = null,
    tag: String? = fragmentClass.simpleName.toLowerCase(Locale.US),
    addToBackStack: Boolean = true,
    isHorizontal: Boolean = true
): FragmentTransaction {
    if (addToBackStack) {
        fragmentTransaction.addToBackStack(tag)
    }
    fragmentTransaction.setCustomAnimations(
        if (isHorizontal) R.anim.slide_in else R.anim.slide_up,
        R.anim.fade_out,
        R.anim.fade_in,
        if (isHorizontal) R.anim.slide_out else R.anim.slide_down
    )
    return fragmentTransaction.add(
        containerId,
        fragmentClass,
        bundle,
        tag
    )
}
package com.example.tipjar.core.bundlers

import android.os.Bundle
import com.evernote.android.state.Bundler
import com.example.tipjar.main.configurations.MainConfiguration

class MainConfigurationBundler : Bundler<MainConfiguration> {

    override fun put(key: String, value: MainConfiguration, bundle: Bundle) {
        bundle.putString("receiptPhotoPath", value.receiptPhotoPath)
        bundle.putDouble("payment", value.payment)
        bundle.putInt("tipPeopleCount", value.tipPeopleCount)
        bundle.putInt("tipPercent", value.tipPercent)
        bundle.putDouble("totalTipAmount", value.totalTipAmount)
    }

    override fun get(key: String, bundle: Bundle): MainConfiguration? {
        return MainConfiguration(
            bundle.getString("receiptPhotoPath"),
            bundle.getDouble("payment"),
            bundle.getInt("tipPeopleCount"),
            bundle.getInt("tipPercent"),
            bundle.getDouble("totalTipAmount")
        )
    }
}
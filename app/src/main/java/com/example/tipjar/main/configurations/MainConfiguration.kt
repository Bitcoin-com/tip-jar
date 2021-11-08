package com.example.tipjar.main.configurations

data class MainConfiguration(
    var receiptPhotoPath: String? = null,
    var payment: Double = 0.0,
    var tipPeopleCount: Int = 0,
    var tipPercent: Int = 0,
    var totalTipAmount: Double = 0.0
)
package com.example.tipjar.paymentsHistory.entities

import com.example.tipjar.database.entities.TipHistory

data class TipHistoryItem(
    val tipHistory: TipHistory,
    val isOpened: Boolean = false,
    val isSelectable: Boolean = false,
    val isSelected: Boolean = false
)
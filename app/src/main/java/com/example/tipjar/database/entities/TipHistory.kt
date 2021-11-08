package com.example.tipjar.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tip_history")
data class TipHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "payment") val payment: Double,
    @ColumnInfo(name = "tip_amount") val tipAmount: Double,
    @ColumnInfo(name = "receipt_photo_path") val receiptPhotoPath: String
) : RoomEntity
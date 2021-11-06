package com.example.tipjar.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tip_history")
data class TipHistory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "pax_count") val paxCount: Int,
    @ColumnInfo(name = "tip_percentage") val tipPercentage: Float
) : RoomEntity
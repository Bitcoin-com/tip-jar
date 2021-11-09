package com.example.tipjar.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tipjar.database.converters.DateConverter
import com.example.tipjar.database.dao.TipHistoryDao
import com.example.tipjar.database.entities.TipHistory

@Database(entities = [TipHistory::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TipDatabase : RoomDatabase() {
    abstract fun getTipHistoryDao() : TipHistoryDao
}
package com.example.tipjar.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.tipjar.database.base.BaseRoomDao
import com.example.tipjar.database.entities.TipHistory

@Dao
interface TipHistoryDao : BaseRoomDao<TipHistory> {

    @Query("SELECT * FROM tip_history")
    suspend fun findAll(): List<TipHistory>

    @Query("SELECT * FROM tip_history WHERE id = :id")
    suspend fun find(id: Int): TipHistory?
}
package com.example.tipjar.database.repositories

import com.example.tipjar.database.base.BaseRoomRepository
import com.example.tipjar.database.dao.TipHistoryDao
import com.example.tipjar.database.entities.TipHistory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipHistoryRepository @Inject constructor(
    private val dao: TipHistoryDao
) : BaseRoomRepository<TipHistory, TipHistoryDao>(dao) {
    override suspend fun findAll(): List<TipHistory> = dao.findAll()
    override suspend fun find(id: Int) = dao.find(id)

    suspend fun findAllSortByDate() = dao.findAllSortByDate()
    suspend fun delete(ids: List<Int>) = dao.delete(ids)
}
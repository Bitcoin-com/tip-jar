package com.example.tipjar.database.base

import com.example.tipjar.database.entities.RoomEntity

abstract class BaseRoomRepository<T : RoomEntity, D : BaseRoomDao<T>>(
    private val dao: D
) {
    abstract suspend fun findAll(): List<T>
    abstract suspend fun find(id: Int): T?
    suspend fun insert(vararg args: T) = dao.insert(*args)
    suspend fun update(vararg args: T) = dao.update(*args)
    suspend fun delete(vararg args: T) = dao.delete(*args)
}
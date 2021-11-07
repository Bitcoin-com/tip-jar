package com.example.tipjar.di

import android.content.Context
import androidx.room.Room
import com.example.tipjar.database.TipDatabase
import com.example.tipjar.database.dao.TipHistoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {

    @Singleton
    @Provides
    fun providesTipDatabase(): TipDatabase {
        return Room.databaseBuilder(
            context,
            TipDatabase::class.java,
            "tip_jar_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesTipHistoryRepository(database: TipDatabase): TipHistoryDao {
        return database.getTipHistoryDao()
    }
}
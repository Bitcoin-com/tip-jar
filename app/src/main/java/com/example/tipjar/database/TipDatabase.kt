package com.example.tipjar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tipjar.database.dao.TipJarDaos
import com.example.tipjar.database.entity.TipHistory

@Database(entities = [TipHistory::class], version = 1, exportSchema = false)
abstract class TipDatabase : RoomDatabase(), TipJarDaos {

    companion object {

        @Volatile
        private var INSTANCE: TipDatabase? = null

        fun getInstance(context: Context): TipDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TipDatabase::class.java,
                        "tip_jar_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
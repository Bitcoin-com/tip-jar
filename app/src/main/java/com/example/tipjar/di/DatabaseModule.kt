package com.example.tipjar.di

import android.content.Context
import com.example.tipjar.database.TipDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun providesTipDatabase(
        @ApplicationContext context: Context
    ): TipDatabase {
        return TipDatabase.getInstance(context)
    }

}
package com.example.tipjar.di

import com.example.tipjar.core.providers.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesCoroutineContextProvider() = CoroutineContextProvider()

    @Provides
    @Singleton
    fun providesEventBus() = EventBus.getDefault()
}
package com.example.tipjar.di

import com.example.tipjar.MainActivity
import com.example.tipjar.TipJarApplication
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DatabaseModule::class])
@Singleton
interface AppComponent {
    fun inject(tipJarApplication: TipJarApplication)
    fun inject(mainActivity: MainActivity)
}